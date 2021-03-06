/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import dao.TblConferenceDAO;
import entity.MyConferenceItem;
import entity.UserVisible;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hoang IT
 */
public class ListRegisterController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private UserVisible userVisible;

    @FXML
    private TableView tblConference;

    ObservableList<MyConferenceItem> list;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Platform.runLater(() -> {
            TableColumn<MyConferenceItem, String> IdCol //
                    = new TableColumn<MyConferenceItem, String>("ID");

            TableColumn<MyConferenceItem, String> nameCol//
                    = new TableColumn<MyConferenceItem, String>("Name");

            TableColumn<MyConferenceItem, String> addressCol//
                    = new TableColumn<MyConferenceItem, String>("Address");

            TableColumn<MyConferenceItem, String> startTimeCol //
                    = new TableColumn<MyConferenceItem, String>("Start time");

            TableColumn<MyConferenceItem, String> endTimeCol //
                    = new TableColumn<MyConferenceItem, String>("End time");

            TableColumn<MyConferenceItem, String> statusCol //
                    = new TableColumn<MyConferenceItem, String>("Status");

            IdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            addressCol.setCellValueFactory(new PropertyValueFactory<>("locationName"));
            startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

            IdCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.05));
            nameCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.25));
            addressCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.3));
            startTimeCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.15));
            startTimeCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.15));
            statusCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.1));

            list = FXCollections.observableList(TblConferenceDAO.allByUserID(this.userVisible.getId()));
            tblConference.setItems(list);

            tblConference.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                try {
                    if (newSelection == null) {
                        newSelection = oldSelection;
                        return;
                    }
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConferenceByUser.fxml"));
                    Parent parent = fxmlLoader.load();
                    ConferenceByUserController controller = fxmlLoader.<ConferenceByUserController>getController();
                    MyConferenceItem item = (MyConferenceItem) newSelection;
                    controller.setConference(TblConferenceDAO.singleByIdConAndIDUser(item.getId(), this.userVisible.getId()));
                    controller.setUser(this.userVisible);
                    Scene scene = new Scene(parent, 600, 400);
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(scene);
                    stage.showAndWait();
                    reloadTable();
                } catch (IOException ex) {
                    Logger.getLogger(ConferenceDetailController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            tblConference.getColumns().addAll(IdCol, nameCol, addressCol, startTimeCol, endTimeCol, statusCol);
        });
    }

    public void setUser(UserVisible user) {
        this.userVisible = user;
    }

    public void reloadTable() {
        Platform.runLater(() -> {
            list.setAll(FXCollections.observableList(TblConferenceDAO.allByUserID(this.userVisible.getId())));
            list.setAll(FXCollections.observableList(TblConferenceDAO.allByUserID(this.userVisible.getId())));
            tblConference.setItems(list);
        });
    }

}
