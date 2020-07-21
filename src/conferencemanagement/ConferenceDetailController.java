/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import conferencemanagement.utils.GlobalData;
import conferencemanagement.utils.Helper;
import dao.TblregisterconferenceDAO;
import entity.ConferenceVisible;
import entity.Tblregisterconference;
import entity.Tbluser;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
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
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

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

    @FXML
    private Button btnRegister;

    @FXML
    private Label lblNotify;

    @FXML
    private Button btnSignIn;

    @FXML
    private Button btnCancle;
    
    @FXML
    private Button btnShowListRegister;

    ConferenceVisible conferenceItem;
    Tbluser currentUser;
    int type;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Platform.runLater(() -> {
            lblName.setText(this.conferenceItem.getName());
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            lblStartTime.setText(format.format(this.conferenceItem.getStartTime()));
            lblEndtime.setText(format.format(this.conferenceItem.getEndTime()));
            lblLimit.setText(this.conferenceItem.getLocationLimit() + "");
            if (this.conferenceItem.getRegister() == null) {
                lblNumRegis.setText("0");
            } else {
                lblNumRegis.setText(this.conferenceItem.getRegister().size() + "");
            }
            lblAddress.setText(this.conferenceItem.getLocationName());
            txtAreaDescription.setText(this.conferenceItem.getDescription());
            if (this.currentUser == null) {
                btnRegister.setVisible(false);
                btnShowListRegister.setVisible(false);
                lblNotify.setText("(*)You must Sign in to be able to Register this conference");
                btnSignIn.setVisible(true);
            } else {
                btnSignIn.setVisible(false);
                btnRegister.setVisible(true);
                btnShowListRegister.setVisible(true);
                reload();
            }
        });
    }

    @FXML
    private void SignIn(MouseEvent event) {
        try {
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("SignInDialog.fxml"));
            Parent parent = fXMLLoader.load();
            SignInDialogController signInDialogController = fXMLLoader.<SignInDialogController>getController();
            Scene scene = new Scene(parent, 600, 400);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            this.currentUser = GlobalData.currentUser;
            btnSignIn.setVisible(false);
            btnRegister.setVisible(true);
            btnShowListRegister.setVisible(true);
            reload();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void DoRegister(MouseEvent event) {
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
        if (type == 1) {
            if (TblregisterconferenceDAO.allByConferenceAccepted(this.conferenceItem.getId()) != null && TblregisterconferenceDAO.allByConferenceAccepted(this.conferenceItem.getId()).size() >= this.conferenceItem.getLocationLimit()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Registration Error");
                alert.setHeaderText("Registration error");
                alert.setContentText("The participant has reached the limit");
                alert.showAndWait();
                return;
            }
            if (TblregisterconferenceDAO.singleByConferenceAndUser(this.conferenceItem.getId(), GlobalData.currentUser.getId()) == null) {
                TblregisterconferenceDAO.insert(this.conferenceItem.getId(), GlobalData.currentUser.getId());
            } else {
                TblregisterconferenceDAO.updateStatus(this.conferenceItem.getId(), GlobalData.currentUser.getId(), 1);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration success");
            alert.setHeaderText("Registration success");
            alert.setContentText("You have registered for this conference!");
            alert.showAndWait();
            btnRegister.setText("Cancel Registration");
            type = 2;
            lblNotify.setText("(*)You have already register this conference!");
            this.conferenceItem.setRegister(TblregisterconferenceDAO.allByConferenceAccepted(this.conferenceItem.getId()));
            reload();
        } else {
            TblregisterconferenceDAO.updateStatus(this.conferenceItem.getId(), GlobalData.currentUser.getId(), 0);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cancel registration success");
            alert.setHeaderText("Cancel registration success");
            alert.setContentText("You have canceled register for this conference!");
            alert.showAndWait();
            this.conferenceItem.setRegister(TblregisterconferenceDAO.allByConferenceAccepted(this.conferenceItem.getId()));
            reload();
        }
        GlobalData.mainController.reloadContainer();
    }

    @FXML
    private void DoClose(MouseEvent event) {
        Stage stage = (Stage) btnCancle.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void ShowListRegister(MouseEvent event) {
         try {
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("ListRegisterUser.fxml"));
            Parent parent = fXMLLoader.load();
            ListRegisterUserController listRegisterDialogController = fXMLLoader.<ListRegisterUserController>getController();
            listRegisterDialogController.setConference(this.conferenceItem, false);
            Scene scene = new Scene(parent, 1000, 700);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            reload();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setConference(ConferenceVisible conference, Tbluser currentUser) {
        this.conferenceItem = conference;
        this.currentUser = currentUser;
    }

    public void reload() {
        Tblregisterconference register = TblregisterconferenceDAO.singleByConferenceAndUser(this.conferenceItem.getId(), GlobalData.currentUser.getId());
        if (register == null) {
            btnRegister.setText("Registration");
            type = 1;
            lblNotify.setText("");
        } else {
            switch (register.getStatus()) {
                case 0:
                    btnRegister.setText("Registration");
                    type = 1;
                    lblNotify.setText("");
                    break;
                case 1:
                    btnRegister.setText("Cancel Registration");
                    lblNotify.setText("(*)You have already register this conference! The request is waiting for aprroved.");
                    type = 2;
                    break;
                case 2:
                    btnRegister.setText("Cancel Registration");
                    lblNotify.setText("(*)You have already register this conference! Admin has accepted your request.");
                    type = 2;
                    break;
                case 3:
                    btnRegister.setText("Registration");
                    btnRegister.setDisable(true);
                    lblNotify.setText("(*)You have already register this conference! Admin has denied your request.");
                    type = 1;
                    break;
            }
        }
        if (this.conferenceItem.getRegister() == null) {
            lblNumRegis.setText("0");
        } else {
            lblNumRegis.setText(this.conferenceItem.getRegister().size() + "");
        }
    }

}
