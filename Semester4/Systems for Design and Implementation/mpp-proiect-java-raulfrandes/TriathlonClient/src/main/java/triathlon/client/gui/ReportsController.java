package triathlon.client.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import triathlon.client.gui.alerts.MessageAlert;
import triathlon.model.DTOs.ParticipantDTO;
import triathlon.model.DTOs.TrialDTO;
import triathlon.services.IObserver;
import triathlon.services.IServices;
import triathlon.services.ServiceException;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReportsController implements Initializable, IObserver {
    private IServices service;
    private Stage stage;
    public ObservableList<ParticipantDTO> participantModel = FXCollections.observableArrayList();
    public ObservableList<TrialDTO> trialModel = FXCollections.observableArrayList();
    private Long currentFilter;

    @FXML
    private ComboBox<TrialDTO> trialCombo;
    @FXML
    private TableView<ParticipantDTO> participantTable;
    @FXML
    private TableColumn<ParticipantDTO, String> nameColumn;
    @FXML
    private TableColumn<ParticipantDTO, String> codeColumn;
    @FXML
    private TableColumn<ParticipantDTO, Integer> pointsColumn;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){
        nameColumn.setCellValueFactory(new PropertyValueFactory<ParticipantDTO, String>("name"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<ParticipantDTO, String>("code"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<ParticipantDTO, Integer>("points"));
        participantTable.setItems(participantModel);

        trialCombo.setItems(trialModel);

        initListenerComboBox();
    }

    public void setService(IServices service, Stage stage) {
        this.service = service;
        this.stage = stage;
    }

    private void initTable() {
        TrialDTO trial = trialCombo.getSelectionModel().getSelectedItem();
        if (trial == null) {
            MessageAlert.showErrorMessage(null, "Select a trial!");
        }
        else {
            List<ParticipantDTO> participants = service.filterParticipantsByTrial(trial.getId());
            participantModel.setAll(participants);
        }
    }

    private void initListenerComboBox() {
        trialCombo.getSelectionModel().selectedItemProperty().addListener((observableValue, trialDTO, t1) -> {
            if (t1 != null) {
                currentFilter = t1.getId();
                service.sendFilter(currentFilter);
                initTable();
            }
        });
    }

    public void initComboBox() {
        List<TrialDTO> trialNames = service.getTrialNames();
        trialModel.setAll(trialNames);
    }

    @FXML
    private void handleClose() {
        closeStage();
    }

    public void closeStage() {
        stage.close();
    }

    @Override
    public void pointsReceived(List<ParticipantDTO> participants) throws ServiceException {}

    @Override
    public void pointsReceivedFiltered(List<ParticipantDTO> participants) throws ServiceException {
        Platform.runLater(() -> {
            participantModel.setAll(participants);
        });
    }

    @Override
    public Long getFilter() {
        return currentFilter;
    }
}
