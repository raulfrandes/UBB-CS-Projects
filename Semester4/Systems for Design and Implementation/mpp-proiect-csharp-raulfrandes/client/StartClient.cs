using System;
using System.Configuration;
using System.Windows.Forms;
using services;
using networking;

namespace client;

public class StartClient
{
    private static int DEFAULT_PORT = 55556;
    private static String DEFAULT_IP = "127.0.0.1";
    
    [STAThread]
    static void Main(string[] args)
    {
        Application.EnableVisualStyles();
        Application.SetCompatibleTextRenderingDefault(false);
        
        Console.WriteLine(@"Reading properties from app.config ...");
        int port = DEFAULT_PORT;
        String ip = DEFAULT_IP;
        String? portS = ConfigurationManager.AppSettings["port"];
        if (portS == null)
        {
            Console.WriteLine(@"Port property not set. Using default value " + DEFAULT_PORT);
        }
        else
        {
            bool result = Int32.TryParse(portS, out port);
            if (!result)
            {
                Console.WriteLine(@"Port property nor set. Using default value " + DEFAULT_PORT);
                port = DEFAULT_PORT;
                Console.WriteLine(@"The port " + port);
            }
        }

        String? ipS = ConfigurationManager.AppSettings["ip"];
        if (ipS == null)
        {
            Console.WriteLine(@"Ip property not set. Using default value " + DEFAULT_IP);
        }
        Console.WriteLine(@"Using server on IP {0} and port {1}", ip, port);

        IServices server = new ServerProxy(ip, port);
        TriathlonController controller = new TriathlonController(server);
        Login login = new Login(controller);
        Application.Run(login);
    }
}