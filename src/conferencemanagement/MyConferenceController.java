/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import dao.TblConferenceDAO;
import entity.Tblconference;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private ComboBox<String> cbSortItem;
    
    @FXML
    private TableView<Tblconference> tblConference;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cbSortItem.getItems().add("Name");
        cbSortItem.getItems().add("Date");
        cbSortItem.setValue("Name");

        TableColumn<Tblconference, String> IdCol //
                = new TableColumn<Tblconference, String>("ID");

        // Tạo cột Email (Kiểu dữ liệu String)
        TableColumn<Tblconference, String> nameCol//
                = new TableColumn<Tblconference, String>("Name");

        // Tạo 2 cột con cho cột FullName
        TableColumn<Tblconference, String> addressCol//
                = new TableColumn<Tblconference, String>("Address");

        TableColumn<Tblconference, String> startTimeCol //
                = new TableColumn<Tblconference, String>("Start time");
        
        TableColumn<Tblconference, String> endTimeCol //
                = new TableColumn<Tblconference, String>("End time");

        IdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        
        IdCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.05));
        nameCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.25));
        addressCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.4));
        startTimeCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.15));
        startTimeCol.prefWidthProperty().bind(tblConference.widthProperty().multiply(0.15));
        
        ObservableList<Tblconference> list = FXCollections.observableList(TblConferenceDAO.allByUser());
        tblConference.setItems(list);
        
        tblConference.getColumns().addAll(IdCol, nameCol, addressCol, startTimeCol, endTimeCol);
    }
}
