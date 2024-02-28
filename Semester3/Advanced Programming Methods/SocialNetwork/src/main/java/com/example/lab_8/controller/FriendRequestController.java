package com.example.lab_8.controller;

import com.example.lab_8.controller.alerts.MessageAlert;
import com.example.lab_8.domain.Friendship;
import com.example.lab_8.domain.User;
import com.example.lab_8.domain.validators.ValidationException;
import com.example.lab_8.repository.RepositoryException;
import com.example.lab_8.service.MainService;
import com.example.lab_8.service.ServiceException;
import com.example.lab_8.utils.events.UserChangeEvent;
import com.example.lab_8.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class FriendRequestController implements Observer<UserChangeEvent> {
    private MainService service;
    private Stage dialogStage;
    private User user;

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    public ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    private void initialize(){
        usernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        userTable.setItems(model);
    }

    public void setService(MainService service, Stage stage, User user){
        this.service = service;
        this.dialogStage = stage;
        this.user = user;
        initializeTableData();
    }

    public void initializeTableData(){
        Iterable<User> users = service.getStrangers(this.user);
        List<User> userList = StreamSupport.stream(users.spliterator(), false)
                .filter(user -> !this.user.equals(user))
                .toList();
        model.setAll(userList);
    }

    @FXML
    public void handleSendRequest(ActionEvent ev) {
        User selected = (User) userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                Optional<Friendship> addedFriendship = this.service.addFriendship(this.user.getId(), selected.getId());
                if (addedFriendship.isEmpty()) {
                    dialogStage.close();
                }
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Send Request", "Friendship request sent!");
            } catch (ValidationException | RepositoryException | ServiceException ex) {
                MessageAlert.showErrorMessage(null, ex.getMessage());
            }
        }
        else {
            MessageAlert.showErrorMessage(null, "You did not select a user!");
        }
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }

    @Override
    public void update(UserChangeEvent userChangeEvent) {
        initializeTableData();
    }
}
