/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import conferencemanagement.utils.GlobalData;
import dao.TblregisterconferenceDAO;
import entity.ConferenceVisible;
import entity.Tblregisterconference;
import entity.UserVisible;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hoang IT
 */
public class ConferenceByUserController implements Initializable {

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
    private Button btnAccept;

    @FXML
    private Button btnDeny;

    @FXML
    private Button btnCancle;

    ConferenceVisible conferenceItem;

    UserVisible user;

    Tblregisterconference register;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Platform.runLater(() -> {
            register = TblregisterconferenceDAO.singleByConferenceAndUser(this.conferenceItem.getId(), this.user.getId());
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
            reset();
        });
    }

    public void setUser(UserVisible user) {
        this.user = user;
    }

    public void setConference(ConferenceVisible conference) {
        this.conferenceItem = conference;
    }

    public void reset() {
        switch (register.getStatus()) {
            case 1:
                btnAccept.setDisable(false);
                btnDeny.setDisable(false);
                break;
            case 2:
                btnAccept.setDisable(true);
                btnDeny.setDisable(false);
                break;
            case 3:
                btnAccept.setDisable(false);
                btnDeny.setDisable(true);
                break;
        }
    }

    @FXML
    private void DoClose(MouseEvent event) {
        Stage stage = (Stage) btnCancle.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void DoAccept(MouseEvent event) {
        if (TblregisterconferenceDAO.updateStatus(this.conferenceItem.getId(), this.user.getId(), 2)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Accept success");
            alert.setHeaderText("Accept success");
            alert.setContentText("You have accepted this request of user for this conference!");
            alert.showAndWait();
            this.register.setStatus(2);
            reset();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Accept Error");
            alert.setHeaderText("Accept error");
            alert.setContentText("Some error has occured. Please try again later!");
            alert.showAndWait();
        }
    }

    @FXML
    private void DoDeny(MouseEvent event) {
        if (TblregisterconferenceDAO.updateStatus(this.conferenceItem.getId(), this.user.getId(), 3)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deny success");
            alert.setHeaderText("Deny success");
            alert.setContentText("You have denied this request of user for this conference!");
            alert.showAndWait();
            this.register.setStatus(3);
            reset();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Accept Error");
            alert.setHeaderText("Accept error");
            alert.setContentText("Some error has occured. Please try again later!");
            alert.showAndWait();
        }
    }
}
