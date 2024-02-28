package com.example.lab_8;

import com.example.lab_8.controller.LoginController;
import com.example.lab_8.controller.UserController;
import com.example.lab_8.domain.Friendship;
import com.example.lab_8.domain.Message;
import com.example.lab_8.domain.Tuple;
import com.example.lab_8.domain.User;
import com.example.lab_8.domain.validators.FriendshipValidator;
import com.example.lab_8.domain.validators.UserValidator;
import com.example.lab_8.domain.validators.Validator;
import com.example.lab_8.repository.*;
import com.example.lab_8.repository.paging.PagingRepository;
import com.example.lab_8.service.MainService;
import com.example.lab_8.service.MessageService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application {

    private PagingRepository<Long, User> userRepository;
    private PagingFriendshipRepository friendshipRepository;
    private Repository<Long, Message> messageRepository;
    private MainService service;
    private MessageService messageService;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Validator<User> userValidator = new UserValidator();
        userRepository = new UserDBRepository(userValidator);
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        friendshipRepository = new FriendshipDBRepository(friendshipValidator);
        messageRepository = new MessageDBRepository();
        service = new MainService(userRepository, friendshipRepository);
        messageService = new MessageService(messageRepository, userRepository);

        initView(primaryStage);
        primaryStage.setTitle("Social Network");
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader loginLoader = new FXMLLoader();
        loginLoader.setLocation(getClass().getResource("views/login-view.fxml"));
        AnchorPane loginLayout = loginLoader.load();
        primaryStage.setScene(new Scene(loginLayout));

        LoginController loginController = loginLoader.getController();
        loginController.setService(service, messageService);
    }
}
