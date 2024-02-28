package com.example.lab_8.controller;

import com.example.lab_8.controller.alerts.MessageAlert;
import com.example.lab_8.domain.Message;
import com.example.lab_8.domain.User;
import com.example.lab_8.domain.validators.ValidationException;
import com.example.lab_8.repository.RepositoryException;
import com.example.lab_8.service.MainService;
import com.example.lab_8.service.MessageService;
import com.example.lab_8.service.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ComposeController {
    private MessageService messageService;
    private MainService service;
    private User user;

    @FXML
    private TextField textFieldFrom;
    @FXML
    private TextField textFieldTo;
    @FXML
    private TextArea textAreaMessage;

    @FXML
    private void initialize() {
    }

    public void setService(MainService service, MessageService messageService, User user) {
        this.service = service;
        this.messageService = messageService;
        this.user = user;
        textFieldFrom.setText(user.getUsername());
    }

    @FXML
    public void handleSend(ActionEvent ev) {
        String to = textFieldTo.getText();
        List<String> toUsername = Arrays.stream(to.split(", ")).toList();
        List<Long> id_receivers = new ArrayList<>();
        toUsername.forEach(username -> id_receivers.add(service.findOneUsername(username).get().getId()));
        String messageText = textAreaMessage.getText();
        try {
            Optional<Message> addedMessage = messageService.addMessage(this.user.getId(), id_receivers, messageText, LocalDateTime.now());
        } catch (ValidationException | RepositoryException | ServiceException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
        textAreaMessage.clear();
    }
}
