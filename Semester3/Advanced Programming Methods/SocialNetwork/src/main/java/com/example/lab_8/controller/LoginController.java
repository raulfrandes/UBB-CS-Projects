package com.example.lab_8.controller;

import com.example.lab_8.controller.alerts.MessageAlert;
import com.example.lab_8.domain.User;
import com.example.lab_8.service.MainService;
import com.example.lab_8.service.MessageService;
import com.example.lab_8.utils.security.PasswordEncoder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class LoginController {
    private MainService service;
    private MessageService messageService;

    @FXML
    private TextField textFieldUsername;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void initialize(){
    }

    public void setService(MainService service, MessageService messageService) {
        this.service = service;
        this.messageService = messageService;
    }

    @FXML
    public void handleLogin(ActionEvent ev) {
        String username = textFieldUsername.getText();
        String password = passwordField.getText();
        if (username.equals("admin") && password.equals("admin")){
            showAdminDialog();
        } else if (username.isEmpty() || password.isEmpty()) {
            MessageAlert.showMessage(null, Alert.AlertType.WARNING, "Empty fields", "Please enter username and/or password!");
        } else {
            Optional<User> user = service.findOneUsername(username);
            if (user.isPresent()) {
                if (PasswordEncoder.verifyPassword(password, user.get().getPassword(), user.get().getSalt())) {
                    showUserDialog(user.get());
                }
                else {
                    MessageAlert.showErrorMessage(null, "Wrong password!");
                }
            }
            else {
                MessageAlert.showErrorMessage(null, "Username does not exist!");
            }
        }
    }

    private void showAdminDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/lab_8/views/user-view.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Admin");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            UserController userController = loader.getController();
            userController.setService(service, messageService);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showUserDialog(User user) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/lab_8/views/profileuser-view.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Profile User");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            ProfileUserController profileUserController = loader.getController();
            profileUserController.setService(service, messageService, dialogStage, user);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleCreateAccount(ActionEvent ev) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/lab_8/views/createaccount-view.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create account");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            CreateAccountController createAccountController = loader.getController();
            createAccountController.setService(service, dialogStage);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
