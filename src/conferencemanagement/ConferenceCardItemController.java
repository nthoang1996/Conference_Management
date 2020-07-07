/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import com.sun.imageio.plugins.common.I18N;
import conferencemanagement.utils.GlobalData;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hoang IT
 */
public class ConferenceCardItemController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Tblconference conferenceItem;
    
    @FXML
    private ImageView imgAvatar;
    
    @FXML
    private Label lblNameConference;
    
    @FXML
    private Label lblStarttTime;
    
    @FXML
    private Label lblTakeTime;
    
    @FXML
    private Label lblAddress;
    
    @FXML
    private Label lblLimit;
    
    @FXML
    private Label lblNumRegis;
    
    @FXML
    private Label lblOverview;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Platform.runLater(() -> {
            String imagePath = "file:///"+ System.getProperty("user.dir") + "/src/asset/picture/"+this.conferenceItem.getId()+"/main.png";
            Image image = new Image(imagePath);
            imgAvatar.setImage(image);
            lblNameConference.setText(this.conferenceItem.getName());
            Date startTime = this.conferenceItem.getStartTime();
            Calendar calStart = Calendar.getInstance();
            calStart.setTime(startTime);
            calStart.add(Calendar.HOUR, -7);
            Date endTime = this.conferenceItem.getEndTime();
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(endTime);
            calEnd.add(Calendar.HOUR, -7);
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            lblStarttTime.setText("Start time: " + format.format(calStart.getTime()));
            lblTakeTime.setText("End time: " + format.format(calEnd.getTime()));
            lblLimit.setText("Limit: " + this.conferenceItem.getLimit());
            if(this.conferenceItem.getParticipant().equals("")){
                lblNumRegis.setText("Number registed: 0");
            }
            else{
                String [] numRegister = this.conferenceItem.getParticipant().split(",");
                lblNumRegis.setText("Number registed: " + (numRegister.length));
            }
            lblAddress.setText("Address: " + this.conferenceItem.getAddress());
            lblOverview.setText("Overview: "+ this.conferenceItem.getOverview());
        });
    }    
    
    public void setConference(Tblconference conference){
        this.conferenceItem = conference;
    }
    
    @FXML
    public void DoDetailItem(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConferenceDetail.fxml"));
            Parent parent = fxmlLoader.load();
            ConferenceDetailController controller = fxmlLoader.<ConferenceDetailController>getController();
            controller.setConference(this.conferenceItem, GlobalData.currentUser);
            Scene scene = new Scene(parent, 600, 400);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ConferenceDetailController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
