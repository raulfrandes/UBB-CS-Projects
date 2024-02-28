package com.example.lab_8.controller;

import com.example.lab_8.controller.alerts.MessageAlert;
import com.example.lab_8.domain.User;
import com.example.lab_8.domain.validators.ValidationException;
import com.example.lab_8.repository.RepositoryException;
import com.example.lab_8.service.MainService;
import com.example.lab_8.service.ServiceException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;

public class EditUserController {

    @FXML
    private TextField textFieldFirstName;

    @FXML
    private TextField textFieldLastName;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private TextField textFieldPassword;

    private MainService service;

    private Stage dialogStage;

    private User user;

    @FXML
    private void initialize() {
    }

    public void setService(MainService service, Stage stage, User user) {
        this.service = service;
        this.dialogStage = stage;
        this.user = user;
        if (user != null){
            setFields(user);
        }
    }

    private void setFields(User user) {
        textFieldFirstName.setText(user.getFirstName());
        textFieldLastName.setText(user.getLastName());
        textFieldUsername.setText(user.getUsername());
        textFieldPassword.setText(user.getPassword());
    }

    @FXML
    public void handleSave() {
        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();
        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();
        User newUser = new User(username, password, firstName, lastName);
        if (this.user == null){
            saveUser(newUser);
        }
        else {
            updateUser(newUser);
        }
    }

    private void saveUser(User newUser) {
        try {
            Optional<User> addedUser = this.service.addUser(newUser.getUsername(), newUser.getPassword(), newUser.getFirstName(), newUser.getLastName());
            if (addedUser.isEmpty()) {
                dialogStage.close();
            }
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Save user", "User saved!");
        } catch (ValidationException | RepositoryException | ServiceException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
        dialogStage.close();
    }

    private void updateUser(User newUser) {
        try {
            Optional<User> oldUser = this.service.updateUser(this.user.getId(), newUser.getUsername(), newUser.getPassword(), newUser.getFirstName(), newUser.getLastName());
            if (oldUser.isEmpty()){
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Update user", "User updated!");
            }
        } catch (ValidationException | RepositoryException | ServiceException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
        dialogStage.close();
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }
}
