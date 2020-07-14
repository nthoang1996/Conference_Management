/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import conferencemanagement.utils.GlobalData;
import dao.TblConferenceDAO;
import entity.ConferenceVisible;
import entity.MyConferenceItem;
import entity.Tblconference;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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
public class MyConferenceController implements Initializable {

    /**
     * Initializes the controller class.
     */

    @FXML
    private TableView<MyConferenceItem> tblConference;
    
    ObservableList<MyConferenceItem> list;
    
    int check = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        TableColumn<MyConferenceItem, String> IdCol //
                = new TableColumn<MyConferenceItem, String>("ID");

        // Tạo cột Email (Kiểu dữ liệu String)
        TableColumn<MyConferenceItem, String> nameCol//
                = new TableColumn<MyConferenceItem, String>("Name");

        // Tạo 2 cột con cho cột FullName
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
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        IdCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.05));
        nameCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.25));
        addressCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.3));
        startTimeCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.15));
        startTimeCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.15));
        statusCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.1));

        list = FXCollections.observableList(TblConferenceDAO.allByUserID(GlobalData.currentUser.getId()));
        if(list ==null){
            tblConference.getColumns().addAll(IdCol, nameCol, addressCol, startTimeCol, endTimeCol, statusCol);
            return;
        }
        tblConference.setItems(list);

        tblConference.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConferenceDetail.fxml"));
                Parent parent = fxmlLoader.load();
                ConferenceDetailController controller = fxmlLoader.<ConferenceDetailController>getController();
                controller.setConference(TblConferenceDAO.singleByIdConAndIDUser(newSelection.getId(), GlobalData.currentUser.getId()), GlobalData.currentUser);
                Scene scene = new Scene(parent, 600, 400);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                if(check == 0){
                    stage.showAndWait();
                    check =1;
                }
                else{
                    check = 0;
                }
                
                reloadTable();
            } catch (IOException ex) {
                Logger.getLogger(ConferenceDetailController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        tblConference.getColumns().addAll(IdCol, nameCol, addressCol, startTimeCol, endTimeCol, statusCol);
    }
    
    public void reloadTable(){
        list.setAll(FXCollections.observableList(TblConferenceDAO.allByUserID(GlobalData.currentUser.getId())));
        tblConference.setItems(list);
    }
}
