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
import javafx.application.Platform;
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
public class ConferenceItemController extends ListCell<ConferenceVisible> {

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
        

        if (empty || conference == null) {

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
            this.conference = conference;
            String imagePath = "file:///" + System.getProperty("user.dir") + "/src/asset/picture/" + conference.getId() + "/main.png";
            Image image = new Image(imagePath);
            imgAvatar.setImage(image);
            lblNameConference.setText(conference.getName());
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            lblStarttTime.setText("Start time: " + format.format(conference.getStartTime()));
            lblTakeTime.setText("End time: " + format.format(conference.getEndTime()));
            lblLimit.setText("Limit: " + conference.getLocationLimit());
            if (conference.getRegister() == null) {
                lblNumRegis.setText("Number registed: 0");
            } else {
                lblNumRegis.setText("Number registed: " + conference.getRegister().size());
            }
            lblAddress.setText("Address: " + conference.getLocationName());
            lblOverview.setText("Overview: " + conference.getOverview());

            setText(null);
            setGraphic(hBContainerConference);
        }

    }

    public void reload() {
        Platform.runLater(() -> {
            String imagePath = "file:///" + System.getProperty("user.dir") + "/src/asset/picture/" + this.conference.getId() + "/main.png";
            Image image = new Image(imagePath);
            imgAvatar.setImage(image);
            lblNameConference.setText(this.conference.getName());
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            lblStarttTime.setText("Start time: " + format.format(this.conference.getStartTime()));
            lblTakeTime.setText("End time: " + format.format(this.conference.getEndTime()));
            lblLimit.setText("Limit: " + this.conference.getLocationLimit());
            if (this.conference.getRegister() == null) {
                lblNumRegis.setText("Number registed: 0");
            } else {
                lblNumRegis.setText("Number registed: " + this.conference.getRegister().size());
            }
            lblAddress.setText("Address: " + this.conference.getLocationName());
            lblOverview.setText("Overview: " + this.conference.getOverview());
        });
    }

    @FXML
    public void DoDetailItem(ActionEvent event) {
        if (GlobalData.currentUser != null && GlobalData.currentUser.getRoleId() == 2) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminConferenceDetail.fxml"));
                Parent parent = fxmlLoader.load();
                AdminConferenceDetailController controller = fxmlLoader.<AdminConferenceDetailController>getController();
                controller.setConference(this.conference);
                Scene scene = new Scene(parent, 600, 400);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                this.conference = new ConferenceVisible(TblConferenceDAO.singleById(this.conference.getId()));
                reload();
            } catch (IOException ex) {
                Logger.getLogger(ConferenceItemController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
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
                this.conference = new ConferenceVisible(TblConferenceDAO.singleById(this.conference.getId()));
                reload();
            } catch (IOException ex) {
                Logger.getLogger(ConferenceDetailController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
