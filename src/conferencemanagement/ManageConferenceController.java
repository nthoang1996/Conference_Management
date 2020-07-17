/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import dao.TblConferenceDAO;
import entity.ConferenceVisible;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML ManageConferenceController class
 *
 * @author Hoang IT
 */
public class ManageConferenceController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private AnchorPane acContainer;
    
    @FXML
    private TableView<ConferenceVisible> tblConference;

    ObservableList<ConferenceVisible> list;

    private ChangeListener<ConferenceVisible> listener;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        TableColumn<ConferenceVisible, String> IdCol //
                = new TableColumn<ConferenceVisible, String>("ID");

        TableColumn<ConferenceVisible, String> nameCol//
                = new TableColumn<ConferenceVisible, String>("Name");

        TableColumn<ConferenceVisible, String> addressCol//
                = new TableColumn<ConferenceVisible, String>("Address");

        TableColumn<ConferenceVisible, String> limitCol //
                = new TableColumn<ConferenceVisible, String>("Limit");

        TableColumn<ConferenceVisible, String> startTimeCol //
                = new TableColumn<ConferenceVisible, String>("Start time");

        TableColumn<ConferenceVisible, String> endTimeCol //
                = new TableColumn<ConferenceVisible, String>("End time");

        IdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("locationName"));
        limitCol.setCellValueFactory(new PropertyValueFactory<>("locationLimit"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        IdCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.05));
        nameCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.25));
        addressCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.3));
        limitCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.1));
        startTimeCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.15));
        endTimeCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.15));

        list = FXCollections.observableList(TblConferenceDAO.allIncludeDeny());
        tblConference.setItems(list);

        listener = (obs, oldSelection, newSelection) -> {
            try {
                if (newSelection == null) {
                    return;
                }
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminConferenceDetail.fxml"));
                Parent parent = fxmlLoader.load();
                AdminConferenceDetailController controller = fxmlLoader.<AdminConferenceDetailController>getController();
                controller.setConference(newSelection);
                Scene scene = new Scene(parent, 600, 400);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
//                reloadTable();

            } catch (IOException ex) {
                Logger.getLogger(ConferenceDetailController.class.getName()).log(Level.SEVERE, null, ex);
            }
        };

        tblConference.getSelectionModel().selectedItemProperty().addListener(listener);

        tblConference.getColumns().addAll(IdCol, nameCol, addressCol, limitCol, startTimeCol, endTimeCol);
    }    
    
    @FXML
    public void NewConference(MouseEvent event){
        acContainer.getChildren().removeAll();
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("NewConference.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        acContainer.getChildren().add(root);
    }
    
}
