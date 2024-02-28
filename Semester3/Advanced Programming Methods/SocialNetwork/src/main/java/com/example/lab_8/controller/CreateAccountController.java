package com.example.lab_8.controller;

import com.example.lab_8.controller.alerts.MessageAlert;
import com.example.lab_8.domain.User;
import com.example.lab_8.domain.validators.ValidationException;
import com.example.lab_8.repository.RepositoryException;
import com.example.lab_8.service.MainService;
import com.example.lab_8.service.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.util.Optional;

public class CreateAccountController {
    private MainService service;
    private Stage dialogStage;

    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void initialize(){
    }

    public void setService(MainService service, Stage dialogStage) {
        this.service = service;
        this.dialogStage = dialogStage;
    }

    @FXML
    public void handleSignUp(ActionEvent ev) {
        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();
        String username = textFieldUsername.getText();
        String password = passwordField.getText();
        try {
            Optional<User> user = service.addUser(username, password, firstName, lastName);
            if (user.isEmpty()) {
                dialogStage.close();
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Account created", "Account created successfully");
            }
        } catch (ValidationException | RepositoryException | ServiceException ex) {
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
        dialogStage.close();
    }

    @FXML
    public void handleExistentAccount(ActionEvent ev) {
        dialogStage.close();
    }
}
