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
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.util.Date;
import java.util.Locale;
import javafx.scene.image.Image;

/**
 * FXML Controller class
 *
 * @author Hoang IT
 */
public class ConferenceItemController extends ListCell<Tblconference>{

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
    
    @Override
    protected void updateItem(Tblconference conference, boolean empty) {
        super.updateItem(conference, empty);

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
            String str = format.format( startTime   );
            lblStarttTime.setText("Start time: " + format.format(calStart.getTime()));
            lblTakeTime.setText("End time: " + format.format(calEnd.getTime()));
            lblLimit.setText("Limit: " + conference.getLimit());
            String [] numRegister = conference.getParticipant().split(",");
            lblNumRegis.setText("Number registed: " + (numRegister.length-1));
            lblAddress.setText("Address: " + conference.getAddress());
            lblOverview.setText("Overview: "+ conference.getOverview());


            setText(null);
            setGraphic(hBContainerConference);
        }

    }
    
}
