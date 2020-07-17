/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import entity.ConferenceVisible;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hoang IT
 */
public class AdminConferenceDetailController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label lblName;

    @FXML
    private Label lblStartTime;

    @FXML
    private Label lblEndtime;

    @FXML
    private Label lblAddress;

    @FXML
    private Label lblLimit;

    @FXML
    private Label lblNumRegis;

    @FXML
    private TextArea txtAreaDescription;

    @FXML
    private Button btnListRegister;

    @FXML
    private Button btnCancle;

    ConferenceVisible conferenceItem;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Platform.runLater(() -> {
            lblName.setText(this.conferenceItem.getName());
            Date startTime = this.conferenceItem.getStartTime();
            Calendar calStart = Calendar.getInstance();
            calStart.setTime(startTime);
            calStart.add(Calendar.HOUR, -7);
            Date endTime = this.conferenceItem.getEndTime();
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(endTime);
            calEnd.add(Calendar.HOUR, -7);
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            lblStartTime.setText(format.format(calStart.getTime()));
            lblEndtime.setText(format.format(calEnd.getTime()));
            lblLimit.setText(this.conferenceItem.getLocationLimit() + "");
            if (this.conferenceItem.getRegister() == null) {
                lblNumRegis.setText("0");
            } else {
                lblNumRegis.setText(this.conferenceItem.getRegister().size() + "");
            }
            lblAddress.setText(this.conferenceItem.getLocationName());
            txtAreaDescription.setText(this.conferenceItem.getDescription());
        });
    }    
    
    @FXML
    private void ShowListRegister(MouseEvent event){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("ListRegisterUser.fxml"));
            Parent parent = fXMLLoader.load();
            ListRegisterUserController listRegisterDialogController = fXMLLoader.<ListRegisterUserController>getController();
            listRegisterDialogController.setConference(this.conferenceItem);
            Scene scene = new Scene(parent, 1000, 700);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void DoClose(MouseEvent event) {
        Stage stage = (Stage) btnCancle.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void EditConference(MouseEvent event) {
        Date startTime = this.conferenceItem.getStartTime();
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(startTime);
        calStart.add(Calendar.HOUR, -7);
        Date currentDate = new Date();
        if (calStart.getTime().compareTo(currentDate) < 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration Error");
            alert.setHeaderText("Registration error");
            alert.setContentText("The conference has occured!");
            alert.showAndWait();
            return;
        }
    }
    
    public void setConference(ConferenceVisible conference){
        this.conferenceItem = conference;
    }
    
}
