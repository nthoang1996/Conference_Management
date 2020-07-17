/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import conferencemanagement.utils.GlobalData;
import entity.ConferenceVisible;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hoang IT
 */
public class ConferenceItemController extends ListCell<ConferenceVisible>{

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private ImageView imgAvatar;
    
    @FXML
    private Label lblNameConference;
    
    @FXML
    private Label lblStarttTime;
    
    @FXML
    private Label lblTakeTime;
    
    @FXML
    private Label lblLimit;
    
    @FXML
    private Label lblNumRegis;
    
    @FXML
    private Label lblAddress;
    
    @FXML
    private Label lblOverview;
    
    @FXML
    private HBox hBContainerConference;
    
    private FXMLLoader mLLoader;
    
    private ConferenceVisible conference;
    
    @Override
    protected void updateItem(ConferenceVisible conference, boolean empty) {
        super.updateItem(conference, empty);
        this.conference = conference;

        if(empty || conference == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("ConferenceItem.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            String imagePath = "file:///"+ System.getProperty("user.dir") + "/src/asset/picture/"+conference.getId()+"/main.png";
            Image image = new Image(imagePath);
            imgAvatar.setImage(image);
            lblNameConference.setText(conference.getName());
            Date startTime = conference.getStartTime();
            Calendar calStart = Calendar.getInstance();
            calStart.setTime(startTime);
            calStart.add(Calendar.HOUR, -7);
            Date endTime = conference.getEndTime();
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(endTime);
            calEnd.add(Calendar.HOUR, -7);
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            lblStarttTime.setText("Start time: " + format.format(calStart.getTime()));
            lblTakeTime.setText("End time: " + format.format(calEnd.getTime()));
            lblLimit.setText("Limit: " + conference.getLocationLimit());
            if(conference.getRegister() == null){
                lblNumRegis.setText("Number registed: 0");
            }
            else{
                lblNumRegis.setText("Number registed: "+ conference.getRegister().size());
            }
            lblAddress.setText("Address: " + conference.getLocationName());
            lblOverview.setText("Overview: "+ conference.getOverview());


            setText(null);
            setGraphic(hBContainerConference);
        }

    }
    
    @FXML
    public void DoDetailItem(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConferenceDetail.fxml"));
            Parent parent = fxmlLoader.load();
            ConferenceDetailController controller = fxmlLoader.<ConferenceDetailController>getController();
            controller.setConference(this.conference, GlobalData.currentUser);
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
