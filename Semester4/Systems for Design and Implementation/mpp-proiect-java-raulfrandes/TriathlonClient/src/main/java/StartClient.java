import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import triathlon.client.gui.LoginController;
import triathlon.client.gui.RefereeController;
import triathlon.model.Participant;
import triathlon.networking.protocol.ServicesProxy;
import triathlon.services.IServices;

import java.io.IOException;
import java.util.Properties;

public class StartClient extends Application {
    private Stage primaryStage;
    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set.");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("triathlon.server.host", defaultServer);
        int serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("triathlon.server.port"));
        } catch (NumberFormatException e) {
            System.err.println("Wrong port number " + e.getMessage());
            System.out.println("Using default port: " + defaultPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IServices services = new ServicesProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/login-view.fxml"));
        Parent root = loader.load();

        LoginController loginController = loader.getController();
        loginController.setService(services);

        FXMLLoader refereeLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/referee-view.fxml"));
        Parent refereeRoot = refereeLoader.load();

        RefereeController refereeController = refereeLoader.getController();
        refereeController.setService(services);

        loginController.setRefereeController(refereeController);
        loginController.setParent(refereeRoot);

        primaryStage.setTitle("Triathlon");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
