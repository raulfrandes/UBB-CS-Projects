package com.example.lab_8.controller;

import com.example.lab_8.controller.alerts.MessageAlert;
import com.example.lab_8.domain.Message;
import com.example.lab_8.domain.User;
import com.example.lab_8.domain.validators.ValidationException;
import com.example.lab_8.repository.RepositoryException;
import com.example.lab_8.service.MainService;
import com.example.lab_8.service.MessageService;
import com.example.lab_8.service.ServiceException;
import com.example.lab_8.utils.events.ChangeEventType;
import com.example.lab_8.utils.events.MessageChangeEvent;
import com.example.lab_8.utils.observer.Observer;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MessageController implements Observer<MessageChangeEvent> {
    private MessageService messageService;
    private MainService service;
    private User user;
    private ObservableList<User> model = FXCollections.observableArrayList();
    private ObservableList<Message> modelChat = FXCollections.observableArrayList();

    @FXML
    private Label chatLabel;
    @FXML
    private ListView<User> userList;
    @FXML
    private ListView<Message> messageList;
    @FXML
    private TextArea textAreaMessage;

    @FXML
    private void initialize(){
        userList.setItems(model);
        messageList.setItems(modelChat);
    }

    public void setService(MainService service, MessageService messageService, User user) {
        this.service = service;
        this.messageService = messageService;
        this.user = user;
        chatLabel.setText("No chat selected");
        messageService.addObserver(this);
        initListData();
        listenList();
    }

    private void initListData() {
        List<User> chats = messageService.getUserChats(this.user);
        model.setAll(chats);
    }

    private void initListChat(User user1) {
        List<Message> chat = messageService.getUsersConvo(this.user, user1);
        modelChat.setAll(chat);
    }

    private void listenList() {
        userList.getSelectionModel().selectedItemProperty().addListener((ChangeListener<User>) (observable, oldValue, newValue) -> {
            if (newValue != null) {
                chatLabel.setText(newValue.getUsername());
                initListChat(newValue);
                userList.requestFocus();
            }
        } );
    }

    @Override
    public void update(MessageChangeEvent messageChangeEvent) {
        User selected = userList.getSelectionModel().getSelectedItem();
        initListData();
        if (selected != null) {
            userList.getSelectionModel().select(selected);
            initListChat(selected);
        }
    }

    @FXML
    public void handleCompose(ActionEvent ev) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/lab_8/views/compose-view.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Compose message");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            ComposeController composeController = loader.getController();
            composeController.setService(service, messageService, this.user);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSend(ActionEvent ev) {
        User selected = userList.getSelectionModel().getSelectedItem();
        String messageText = textAreaMessage.getText();
        Message selectedMessage = messageList.getSelectionModel().getSelectedItem();
        if (selectedMessage != null) {
            try {
                Optional<Message> addedMessage = messageService.addReplyMessage(this.user.getId(), selected.getId(), messageText, LocalDateTime.now(), selectedMessage);
            } catch (ValidationException | RepositoryException | ServiceException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
        else {
            try {
                Optional<Message> addedMessage = messageService.addMessage(this.user.getId(), selected.getId(), messageText, LocalDateTime.now());
            } catch (ValidationException | RepositoryException | ServiceException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
        textAreaMessage.clear();
        userList.requestFocus();
    }
}
