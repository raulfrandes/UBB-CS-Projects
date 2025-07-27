package triathlon.client.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import triathlon.client.gui.alerts.MessageAlert;
import triathlon.model.DTOs.ParticipantDTO;
import triathlon.model.DTOs.TrialDTO;
import triathlon.model.Participant;
import triathlon.model.Referee;
import triathlon.model.Result;
import triathlon.model.Trial;
import triathlon.model.validator.ValidatorException;
import triathlon.services.IObserver;
import triathlon.services.IServices;
import triathlon.services.ServiceException;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RefereeController implements Initializable, IObserver {
    private IServices service;
    private Referee referee;
    public ObservableList<ParticipantDTO> participantModel = FXCollections.observableArrayList();
    public ObservableList<TrialDTO> trialModel = FXCollections.observableArrayList();
    private ReportsController reportsController;

    @FXML
    private Label labelName;

    @FXML
    private TableView<ParticipantDTO> participantTable;
    @FXML
    private TableColumn<ParticipantDTO, String> nameColumn;
    @FXML
    private TableColumn<ParticipantDTO, String> codeColumn;
    @FXML
    private TableColumn<ParticipantDTO, Integer> pointsColumn;

    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldCode;
    @FXML
    private TextField textFieldPoints;
    @FXML
    private ComboBox<TrialDTO> comboTrial;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){
        nameColumn.setCellValueFactory(new PropertyValueFactory<ParticipantDTO, String>("name"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<ParticipantDTO, String>("code"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<ParticipantDTO, Integer>("points"));
        participantTable.setItems(participantModel);

        initListenerTable();

        comboTrial.setItems(trialModel);
    }

    public void setService(IServices service) {
        this.service = service;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
        labelName.setText(labelName.getText() + this.referee.getName() + "!");
    }

    public void initParticipantTable() {
        List<ParticipantDTO> participants = service.getParticipantsAndPoints();
        participantModel.setAll(participants);
    }

    private void initListenerTable() {
        participantTable.getSelectionModel().selectedItemProperty().addListener((observableValue, participantDTO, t1) -> {
            if (t1 != null) {
                textFieldName.setText(t1.getName());
                textFieldCode.setText(t1.getCode());
            }
            else {
                textFieldName.clear();
                textFieldName.clear();
            }
        });
    }

    public void initComboBox() {
        List<TrialDTO> trialNames = service.getTrialNames();
        trialModel.setAll(trialNames);
    }

    @FXML
    public void handleAddPoints(ActionEvent event) {
        ParticipantDTO selectedParticipant = participantTable.getSelectionModel().getSelectedItem();
        if (selectedParticipant == null) {
            MessageAlert.showErrorMessage(null, "Select a participant!");
        }
        else {
            TrialDTO selectedTrial = comboTrial.getSelectionModel().getSelectedItem();
            if (selectedTrial == null) {
                MessageAlert.showErrorMessage(null, "Select a trial!");
            }
            else {
                try {
                    Participant participant = new Participant(selectedParticipant.getName(), selectedParticipant.getCode());
                    participant.setId(selectedParticipant.getId());
                    Trial trial = new Trial(selectedTrial.getName(), null, null);
                    trial.setId(selectedTrial.getId());
                    Integer points = Integer.parseInt(textFieldPoints.getText());
                    Result result = new Result(participant, trial, points);
                    service.addResult(result);
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Add points",
                            "Points added to " + participant.getName() + " for the " + trial.getName() + " trial");
                    textFieldName.clear();
                    textFieldCode.clear();
                    textFieldPoints.clear();
                } catch (ServiceException | ValidatorException e) {
                    MessageAlert.showErrorMessage(null, e.getMessage());
                }
            }
        }
    }

    @FXML
    public void handleReports(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/reports-view.fxml"));
            AnchorPane root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Referee " + referee.getName() + " reports");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            reportsController = loader.getController();
            reportsController.setService(service, dialogStage);
            reportsController.initComboBox();
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        logout();
        if (reportsController != null) {
            reportsController.closeStage();
        }
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    public void logout() {
        try {
            service.logout(referee.getId(), this);
        } catch (ServiceException e) {
            System.out.println("Logout error " + e);
        }
    }

    @Override
    public void pointsReceived(List<ParticipantDTO> participants) throws ServiceException {
        Platform.runLater(() -> {
            participantModel.setAll(participants);
        });
    }

    @Override
    public void pointsReceivedFiltered(List<ParticipantDTO> participants) throws ServiceException {
        reportsController.pointsReceivedFiltered(participants);
    }

    @Override
    public Long getFilter() {
        return reportsController.getFilter();
    }
}
