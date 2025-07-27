import triathlon.model.Participant;
import triathlon.model.Referee;
import triathlon.model.Result;
import triathlon.model.Trial;
import triathlon.model.validator.*;
import triathlon.networking.utils.AbstractServer;
import triathlon.networking.utils.ConcurrentServer;
import triathlon.networking.utils.ServerException;
import triathlon.persistence.dbRepositories.ParticipantDBRepository;
import triathlon.persistence.dbRepositories.RefereeDBRepository;
import triathlon.persistence.dbRepositories.ResultDBRepository;
import triathlon.persistence.dbRepositories.TrialDBRepository;
import triathlon.persistence.hibernateRepositories.ParticipantHibernateRepository;
import triathlon.persistence.hibernateRepositories.RefereeHibernateRepository;
import triathlon.persistence.hibernateRepositories.ResultHibernateRepository;
import triathlon.persistence.hibernateRepositories.TrialHibernateRepository;
import triathlon.persistence.interfaces.IParticipantRepository;
import triathlon.persistence.interfaces.IRefereeRepository;
import triathlon.persistence.interfaces.IResultRepository;
import triathlon.persistence.interfaces.ITrialRepository;
import triathlon.server.ServicesImplementation;
import triathlon.services.IServices;

import java.io.IOException;
import java.util.Properties;

public class StartServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set." + serverProps.getProperty("triathlon.jdbc.url"));
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties " + e);
            return;
        }
        Validator<Referee> refereeValidator = new RefereeValidator();
        IRefereeRepository refereeRepository = new RefereeHibernateRepository(refereeValidator);
        Validator<Trial> trialValidator = new TrialValidator();
        ITrialRepository trialRepository = new TrialHibernateRepository(trialValidator);
        Validator<Participant> participantValidator = new ParticipantValidator();
        IParticipantRepository participantRepository = new ParticipantHibernateRepository(participantValidator);
        Validator<Result> resultValidator = new ResultValidator();
        IResultRepository resultRepository = new ResultHibernateRepository(resultValidator);

        refereeRepository.save(new Referee("Alex Johnson", "alex", "alex"));
        refereeRepository.save(new Referee("David Johnson", "david", "david"));
        trialRepository.save(new Trial("Swimming",
                "Participants swim a designated distance in open water.",
                refereeRepository.findByUsername("alex")));
        trialRepository.save(new Trial("Running Segment",
                "Following the cycling, competitors embark on a running course.",
                refereeRepository.findByUsername("alex")));
        participantRepository.save(new Participant("Alex Johnson", "1234"));

        IServices services = new ServicesImplementation(refereeRepository, trialRepository, participantRepository,
                resultRepository);
        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("triathlon.server.port"));
        } catch (NumberFormatException e) {
            System.err.println("Wrong Port Number " + e.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + serverPort);
        AbstractServer server = new ConcurrentServer(serverPort, services);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server " + e.getMessage());
        }
    }
}
