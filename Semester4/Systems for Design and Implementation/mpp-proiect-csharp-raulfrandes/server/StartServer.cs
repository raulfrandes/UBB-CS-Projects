using System;
using System.Collections.Generic;
using System.Configuration;
using System.Net.Sockets;
using System.Threading;
using log4net.Config;
using model;
using model.validator;
using persistence.dbRepositories;
using persistence.interfaces;
using services;
using networking;
using networking2;

namespace server;

public class StartServer
{
    private static int DEFAULT_PORT = 55556;
    private static String DEFAULT_IP = "127.0.0.1";

    static void Main(string[] args)
    {
        XmlConfigurator.Configure(new System.IO.FileInfo("log4net.config"));
        Console.WriteLine("Reading properties from app.config ...");
        int port = DEFAULT_PORT;
        String ip = DEFAULT_IP;
        String? portS = ConfigurationManager.AppSettings["port"];
        if (portS == null)
        {
            Console.WriteLine("Port property not set. Using default value " + DEFAULT_PORT);
        }
        else
        {
            bool result = Int32.TryParse(portS, out port);
            if (!result)
            {
                Console.WriteLine("Port property not a number. Using default value " + DEFAULT_PORT);
                port = DEFAULT_PORT;
                Console.WriteLine("The port" + port);
            }

            String? ipS = ConfigurationManager.AppSettings["ip"];

            if (ipS == null)
            {
                Console.WriteLine("Ip property not set. Using default value " + DEFAULT_IP);
            }
            Console.WriteLine("Configuration Settings for database {0}", GetConnectionStringByName("triathlonDB"));
            IDictionary<String, string> props = new SortedList<string, string>();
            props.Add("ConnectionString", GetConnectionStringByName("triathlonDB"));
            IValidator<Referee> refereeValidator = new RefereeValidator();
            IRefereeRepository refereeRepository = new RefereeDbRepository(props, refereeValidator);
            IValidator<Trial> trialValidator = new TrialValidator();
            ITrialRepository trialRepository = new TrialDbRepository(props, trialValidator);
            IValidator<Participant> participantValidator = new ParticipantValidator();
            IParticipantRepository participantRepository = new ParticipantDbRepository(props, participantValidator);
            IValidator<Result> resultValidator = new ResultValidator();
            IResultRepository resultRepository = new ResultDbRepository(props, resultValidator);
            IServices services = new ServerImplementation(refereeRepository, trialRepository, participantRepository,
                resultRepository);
            
            Console.WriteLine("Starting server on IP {0} and port {1}", ip, port);
            SerialServer server = new SerialServer(ip, port, services);
            server.Start();
            Console.WriteLine("Server started ...");
            Console.ReadLine();
        }
    }

    static String GetConnectionStringByName(string name)
    {
        string returnValue = null!;

        ConnectionStringSettings settings = ConfigurationManager.ConnectionStrings[name];

        if (settings != null)
        {
            returnValue = settings.ConnectionString;
        }

        return returnValue;
    }
}

public class SerialServer : ConcurrentServer
{
    private IServices _server;
    private ProtoWorker _worker;

    public SerialServer(string host, int port, IServices server) : base(host, port)
    {
        _server = server;
        Console.WriteLine("SerialServer...");
    }


    protected override Thread CreateWorker(TcpClient client)
    {
        _worker = new ProtoWorker(_server, client);
        return new Thread(new ThreadStart(_worker.Run));
    }
}