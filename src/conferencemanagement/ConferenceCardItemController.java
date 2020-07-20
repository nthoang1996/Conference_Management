/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import conferencemanagement.utils.GlobalData;
import dao.TblConferenceDAO;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    ConferenceVisible conferenceItem;

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
            String imagePath = "file:///" + System.getProperty("user.dir") + "/src/asset/picture/" + this.conferenceItem.getId() + "/main.png";
            Image image = new Image(imagePath);
            imgAvatar.setImage(image);
            lblNameConference.setText(this.conferenceItem.getName());
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            lblStarttTime.setText("Start time: " + format.format(this.conferenceItem.getStartTime()));
            lblTakeTime.setText("End time: " + format.format(this.conferenceItem.getEndTime()));
            lblLimit.setText("Limit: " + this.conferenceItem.getLocationLimit());
            if (this.conferenceItem.getRegister() == null) {
                lblNumRegis.setText("Number registed:0");
            } else {
                lblNumRegis.setText("Number registed:" + this.conferenceItem.getRegister().size());
            }
            lblAddress.setText("Address: " + this.conferenceItem.getLocationName());
            lblOverview.setText("Overview: " + this.conferenceItem.getOverview());
        });
    }

    public void setConference(ConferenceVisible conference) {
        this.conferenceItem = conference;
    }

    public void reload() {
        Platform.runLater(() -> {
            String imagePath = "file:///" + System.getProperty("user.dir") + "/src/asset/picture/" + this.conferenceItem.getId() + "/main.png";
            Image image = new Image(imagePath);
            imgAvatar.setImage(image);
            lblNameConference.setText(this.conferenceItem.getName());
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            lblStarttTime.setText("Start time: " + format.format(this.conferenceItem.getStartTime()));
            lblTakeTime.setText("End time: " + format.format(this.conferenceItem.getEndTime()));
            lblLimit.setText("Limit: " + this.conferenceItem.getLocationLimit());
            if (this.conferenceItem.getRegister() == null) {
                lblNumRegis.setText("Number registed:0");
            } else {
                lblNumRegis.setText("Number registed:" + this.conferenceItem.getRegister().size());
            }
            lblAddress.setText("Address: " + this.conferenceItem.getLocationName());
            lblOverview.setText("Overview: " + this.conferenceItem.getOverview());
        });
    }

    @FXML
    public void DoDetailItem(ActionEvent event) {
        if (GlobalData.currentUser != null && GlobalData.currentUser.getRoleId() == 2) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminConferenceDetail.fxml"));
                Parent parent = fxmlLoader.load();
                AdminConferenceDetailController controller = fxmlLoader.<AdminConferenceDetailController>getController();
                controller.setConference(this.conferenceItem);
                Scene scene = new Scene(parent, 600, 400);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                this.conferenceItem = new ConferenceVisible(TblConferenceDAO.singleById(this.conferenceItem.getId()));
                reload();
            } catch (IOException ex) {
                Logger.getLogger(ConferenceItemController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
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
                this.conferenceItem = new ConferenceVisible(TblConferenceDAO.singleById(this.conferenceItem.getId()));
                reload();
            } catch (IOException ex) {
                Logger.getLogger(ConferenceDetailController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
