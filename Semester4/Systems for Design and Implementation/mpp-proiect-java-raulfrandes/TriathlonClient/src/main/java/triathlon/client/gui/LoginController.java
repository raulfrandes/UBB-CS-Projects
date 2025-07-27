package triathlon.client.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import triathlon.client.gui.alerts.MessageAlert;
import triathlon.model.Referee;
import triathlon.services.IServices;
import triathlon.services.ServiceException;

import java.io.IOException;

public class LoginController {
    private IServices service;
    private RefereeController refereeController;
    private Referee referee;
    Parent mainParent;

    @FXML
    private TextField textFieldUsername;
    @FXML
    private PasswordField passwordField;

    @FXML
    public void initialize() {
    }

    public void setService(IServices service) {
        this.service = service;
    }

    public void setRefereeController(RefereeController refereeController) {
        this.refereeController = refereeController;
    }

    public void setParent(Parent parent) {
        this.mainParent = parent;
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = textFieldUsername.getText();
        String password = passwordField.getText();
        textFieldUsername.clear();
        passwordField.clear();
        if (username.isEmpty() || password.isEmpty()) {
            MessageAlert.showMessage(null, Alert.AlertType.WARNING, "Empty fields",
                    "Please enter credentials!");
        }
        else {
            try {
                this.referee = service.login(username, refereeController);
                if (referee.getPassword().equals(password)) {
                    Stage stage = new Stage();
                    stage.setTitle("Referee " + referee.getName());
                    stage.setScene(new Scene(mainParent));

                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent windowEvent) {
                            refereeController.logout();
                            System.exit(0);
                        }
                    });

                    stage.show();
                    refereeController.setReferee(referee);
                    refereeController.initParticipantTable();
                    refereeController.initComboBox();
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                }
                else {
                    MessageAlert.showErrorMessage(null, "Wrong credentials!");
                }
            } catch (ServiceException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
    }
}
