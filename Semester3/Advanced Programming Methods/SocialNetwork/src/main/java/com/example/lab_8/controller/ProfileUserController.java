package com.example.lab_8.controller;

import com.example.lab_8.controller.alerts.MessageAlert;
import com.example.lab_8.domain.*;
import com.example.lab_8.domain.validators.ValidationException;
import com.example.lab_8.repository.RepositoryException;
import com.example.lab_8.repository.paging.Page;
import com.example.lab_8.repository.paging.Pegeable;
import com.example.lab_8.service.MainService;
import com.example.lab_8.service.MessageService;
import com.example.lab_8.service.ServiceException;
import com.example.lab_8.utils.events.FriendshipChangeEvent;
import com.example.lab_8.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class ProfileUserController implements Observer<FriendshipChangeEvent> {
    private MainService service;
    private MessageService messageService;
    private Stage dialogStage;
    private User user;
    private int currentPage = 0;
    private int numberOfRecordsPerPage = 0;
    private int totalNumberOfElements;
    public ObservableList<FriendDTO> model = FXCollections.observableArrayList();

    @FXML
    private TableView<FriendDTO> friendsTable;
    @FXML
    private TableColumn<FriendDTO, String> usernameColumn;
    @FXML
    private TableColumn<FriendDTO, String> firstNameColumn;
    @FXML
    private TableColumn<FriendDTO, String> lastNameColumn;
    @FXML
    private TableColumn<FriendDTO, FriendRequest> statusColumn;
    @FXML
    private TableColumn<FriendDTO, LocalDateTime> friendsFromColumn;
    @FXML
    private Label userLabel;
    @FXML
    private TextField textFieldRecordsOnPage;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;
    @FXML
    private Label currentPageLabel;

    @FXML
    private void initialize(){
        usernameColumn.setCellValueFactory(new PropertyValueFactory<FriendDTO, String>("username"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<FriendDTO, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<FriendDTO, String>("lastName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<FriendDTO, FriendRequest>("status"));
        friendsFromColumn.setCellValueFactory(new PropertyValueFactory<FriendDTO, LocalDateTime>("friendsFrom"));
        friendsTable.setItems(model);
        listenRecordsOnPageTextField();
    }

    public void setService(MainService service, MessageService messageService, Stage stage, User user){
        this.service = service;
        this.messageService = messageService;
        this.dialogStage = stage;
        this.user = user;
        userLabel.setText(user.getFirstName() + " " + user.getLastName());
        service.FriendshipChange().addObserver(this);
        initializeTableData();
    }

    public void initializeTableData() {
        numberOfRecordsPerPage = Integer.parseInt(textFieldRecordsOnPage.getText());
        Page<FriendDTO> friendsOnCurrentPage = service.getUserFriends(new Pegeable(currentPage, numberOfRecordsPerPage), this.user);
        totalNumberOfElements = friendsOnCurrentPage.getTotalNumberOfElements();
        List<FriendDTO> friendsList = StreamSupport
                .stream(friendsOnCurrentPage.getElementsOnPage().spliterator(), false)
                .toList();
        model.setAll(friendsList);
        pageNavigationChecks();
        int maxPage = (int) Math.ceil((double) totalNumberOfElements / numberOfRecordsPerPage) - 1;
        currentPageLabel.setText((currentPage + 1) + "/" + (maxPage + 1));
    }

    private void listenRecordsOnPageTextField() {
        textFieldRecordsOnPage.textProperty().addListener(o -> {
            currentPage = 0;
            try {
                initializeTableData();
            } catch (NumberFormatException e) {
                return;
            }
        });
    }

    private void pageNavigationChecks() {
        previousButton.setDisable(currentPage == 0);
        nextButton.setDisable((currentPage + 1) * numberOfRecordsPerPage >= totalNumberOfElements);
    }

    @FXML
    public void handleNext(ActionEvent ev) {
        currentPage++;
        initializeTableData();
    }

    @FXML
    public void handlePrevious(ActionEvent ev) {
        currentPage--;
        initializeTableData();
    }

    @Override
    public void update(FriendshipChangeEvent friendshipChangeEvent) {
        initializeTableData();
    }

    @FXML
    public void handleSendRequest(ActionEvent ev) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/lab_8/views/friendrequest-view.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Friend Request");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            FriendRequestController friendRequestController = loader.getController();
            friendRequestController.setService(service, dialogStage, this.user);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAccept(ActionEvent ev){
        FriendDTO selected = (FriendDTO) friendsTable.getSelectionModel().getSelectedItem();
        if (selected != null){
            if (selected.getStatus() == FriendRequest.PENDING) {
                try {
                    Tuple<Long, Long> id = new Tuple<>(selected.getId(), this.user.getId());
                    Optional<Friendship> oldFriendship = this.service.updateFriendship(id, LocalDateTime.now(),
                            FriendRequest.APPROVED);

                    if (oldFriendship.isPresent()){
                        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,
                                "Accept friend request", "Friend request accepted!");
                    }
                    else {
                        MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION,
                                "Wait Response", "Friend request sent! Waiting for a response!");
                    }
                } catch (ValidationException | RepositoryException | ServiceException e) {
                    MessageAlert.showErrorMessage(null, e.getMessage());
                }
            } else {
                MessageAlert.showErrorMessage(null, "You did not select a pending request");
            }
        }
        else {
            MessageAlert.showErrorMessage(null, "You did not select a friend!");
        }
    }

    @FXML
    public void handleReject(ActionEvent ev) {
        FriendDTO selected = (FriendDTO) friendsTable.getSelectionModel().getSelectedItem();
        if (selected != null){
            if (selected.getStatus() == FriendRequest.PENDING) {
                try {
                    Tuple<Long, Long> id = new Tuple<>(selected.getId(), this.user.getId());
                    Optional<Friendship> oldFriendship = this.service.updateFriendship(id, LocalDateTime.now(),
                            FriendRequest.REJECTED);

                    if (oldFriendship.isPresent()){
                        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,
                                "Reject friend request", "Friend request rejected!");
                    }
                    else {
                        MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION,
                                "Wait Response", "Friend request sent! Waiting for a response!");
                    }
                } catch (ValidationException | RepositoryException | ServiceException e) {
                    MessageAlert.showErrorMessage(null, e.getMessage());
                }
            } else {
                MessageAlert.showErrorMessage(null, "You did not select a pending request");
            }
        }
        else {
            MessageAlert.showErrorMessage(null, "You did not select a friend!");
        }
    }

    @FXML
    public void handleDeleteFriendship(ActionEvent ev) {
        FriendDTO selected = (FriendDTO) friendsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Optional<Friendship> deleted = service.deleteFriendship(this.user.getId(), selected.getId());
            if (deleted.isPresent()) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "Friendship deleted!");
            }
        }
        else {
            MessageAlert.showErrorMessage(null, "You did not select a friend!");
        }
    }

    @FXML
    public void handleMessage(ActionEvent ev) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/lab_8/views/message-view.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Messages");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            MessageController messageController = loader.getController();
            messageController.setService(service, messageService, this.user);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
