/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import entity.Tblconference;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hoang IT
 */
public class ConferenceDetailController implements Initializable {

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
    
    Tblconference conferenceItem;
    
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
            lblLimit.setText(this.conferenceItem.getLimit() + "");
            String [] numRegister = this.conferenceItem.getParticipant().split(",");
            lblNumRegis.setText((numRegister.length-1) + "");
            lblAddress.setText(this.conferenceItem.getAddress());
            txtAreaDescription.setText(this.conferenceItem.getDescription());
//            txtAreaDescription.setWrapText(true);
        });
    }    
    
    public void setConference(Tblconference conference){
        this.conferenceItem = conference;
    }
    
}
