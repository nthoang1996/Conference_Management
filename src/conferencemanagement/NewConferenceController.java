/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import conferencemanagement.utils.Helper;
import dao.TblConferenceDAO;
import dao.TblLocationDAO;
import entity.ConferenceVisible;
import entity.Tblconference;
import entity.Tbllocation;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Hoang IT
 */
public class NewConferenceController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ComboBox<Tbllocation> cbBoxLocation;

    @FXML
    private TextField txtFieldName;

    @FXML
    private TextField txtFieldOverview;

    @FXML
    private TextField txtFieldAvatar;

    @FXML
    private DatePicker dpDate;

    @FXML
    private TextField textFieldStartTime;

    @FXML
    private TextField textFieldEndTime;

    @FXML
    private TextArea txtAreaDescription;

    @FXML
    private AnchorPane acMain;

    @FXML
    private Button btnCancel;

    ObservableList<Tbllocation> list;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        list = FXCollections.observableList(TblLocationDAO.all());
        cbBoxLocation.setItems(list);

        Callback<ListView<Tbllocation>, ListCell<Tbllocation>> factory = lv -> new ListCell<Tbllocation>() {

            @Override
            protected void updateItem(Tbllocation item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };

        cbBoxLocation.setCellFactory(factory);
        cbBoxLocation.setButtonCell(factory.call(null));

        cbBoxLocation.getSelectionModel().selectFirst();
    }

    @FXML
    private void CreateConference(MouseEvent event) {
        if (validate()) {
            Date start = combineDateTime(textFieldStartTime.getText().trim());
            Date end = combineDateTime(textFieldEndTime.getText().trim());
            if (start == null || end == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error has occur");
                alert.setContentText("Time can not be null");
                alert.showAndWait();
                return;
            }
            if (start.compareTo(end) > 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error has occur");
                alert.setContentText("Start time can not be larger than end time!");
                alert.showAndWait();
                return;
            }
            Calendar calStart = Calendar.getInstance();
            calStart.setTime(start);
            calStart.add(Calendar.HOUR, +7);
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(end);
            calEnd.add(Calendar.HOUR, +7);
            if(!validateLocation(start, end)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error has occur");
                alert.setContentText("There is a conference is going to occured in this location at this time!");
                alert.showAndWait();
                return;
            }
            Tblconference conference = new Tblconference(txtFieldName.getText(), txtFieldOverview.getText(), txtAreaDescription.getText(), cbBoxLocation.getSelectionModel().getSelectedItem().getId(), calStart.getTime(), calEnd.getTime());
            if (TblConferenceDAO.insert(conference, txtFieldAvatar.getText())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Create conference success");
                alert.setHeaderText("Create conference success");
                alert.setContentText("Create conference success!");
                alert.showAndWait();
                DoCancel(event);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error has occur");
                alert.setContentText("An error has occured. Please try again later!");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void BrowseFile(MouseEvent event) {
        Stage stage = (Stage) acMain.getScene().getWindow();
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose an image:");
        FileChooser.ExtensionFilter fileFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp", "*.gif");
        fc.getExtensionFilters().add(fileFilter);
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            txtFieldAvatar.setText(Helper.decodeFilePathValue(file.toURI().toString().substring(6)));
        }
    }

    public Date combineDateTime(String time) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            LocalDate localDate = dpDate.getValue();
            String sDate = localDate + " " + time + ":00";
            Date dateConvert = formatter.parse(sDate);
            return dateConvert;
        } catch (ParseException ex) {
            Logger.getLogger(NewConferenceController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean validateTime(String time) {
        String[] arr = time.split(":");
        if (arr.length < 2) {
            return false;
        }
        try {
            if (Integer.parseInt(arr[0]) < 0 || Integer.parseInt(arr[0]) > 23) {
                return false;
            }
            if (Integer.parseInt(arr[1]) < 0 || Integer.parseInt(arr[1]) > 59) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean validateLocation(Date startTime, Date endTime){
        int id = cbBoxLocation.getSelectionModel().getSelectedItem().getId();
        ArrayList<ConferenceVisible> listConference = TblConferenceDAO.allByLocationId(id);
        for(int i = 0 ; i< listConference.size(); i++){
            if(startTime.compareTo(listConference.get(i).getStartTime()) >= 0 && startTime.compareTo(listConference.get(i).getEndTime()) <= 0){
                return false;
            }
            if(endTime.compareTo(listConference.get(i).getStartTime()) >= 0 && endTime.compareTo(listConference.get(i).getEndTime()) <= 0){
                return false;
            }
            if(endTime.compareTo(listConference.get(i).getStartTime()) >= 0 && endTime.compareTo(listConference.get(i).getEndTime()) <= 0){
                return false;
            }
            if(listConference.get(i).getStartTime().compareTo(startTime) >= 0 && listConference.get(i).getEndTime().compareTo(endTime) <= 0){
                return false;
            }
            if(listConference.get(i).getEndTime().compareTo(endTime) >= 0 && listConference.get(i).getEndTime().compareTo(endTime) <= 0){
                return false;
            }
        }
        return true;
    }

    public boolean validate() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error has occur");
        if (txtFieldName.getText().trim().equals("")) {
            alert.setContentText("Name can not be null");
            alert.showAndWait();
            return false;
        }
        if (txtFieldAvatar.getText().trim().equals("")) {
            alert.setContentText("Please choose avatar!");
            alert.showAndWait();
            return false;
        }
        if (dpDate.getValue().equals("")) {
            alert.setContentText("Please choose date open!");
            alert.showAndWait();
            return false;
        }
        if (textFieldStartTime.getText().trim().equals("")) {
            alert.setContentText("Start time can not be null!");
            alert.showAndWait();
            return false;
        }

        if (textFieldEndTime.getText().trim().equals("")) {
            alert.setContentText("End time can not be null!");
            alert.showAndWait();
            return false;
        }

        if (!validateTime(textFieldStartTime.getText().trim()) || !validateTime(textFieldEndTime.getText().trim())) {
            alert.setContentText("Plese input correctly time format!");
            alert.showAndWait();
            return false;
        }
        if (dpDate.getValue().compareTo(LocalDate.now()) < 0) {
            alert.setContentText("Date can not be smaller than current date!");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    @FXML
    private void DoCancel(MouseEvent event) {
        acMain.getChildren().removeAll();
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("ManageConference.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        acMain.getChildren().add(root);
    }
}
