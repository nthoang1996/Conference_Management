/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import conferencemanagement.utils.GlobalData;
import conferencemanagement.utils.Helper;
import dao.TblConferenceDAO;
import entity.Tblconference;
import entity.Tbluser;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
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

    @FXML
    private Button btnRegister;

    @FXML
    private Label lblNotify;

    @FXML
    private Button btnSignIn;

    @FXML
    private Button btnCancle;

    Tblconference conferenceItem;
    Tbluser currentUser;
    int type;

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
            String[] numRegister = this.conferenceItem.getParticipant().split(",");
            if (this.conferenceItem.getParticipant().equals("")) {
                lblNumRegis.setText("0");
            } else {
                lblNumRegis.setText((numRegister.length) + "");
            }

            lblAddress.setText(this.conferenceItem.getAddress());
            txtAreaDescription.setText(this.conferenceItem.getDescription());
            if (this.currentUser == null) {
                btnRegister.setVisible(false);
                lblNotify.setText("(*)You must Sign in to be able to Register this conference");
                btnSignIn.setVisible(true);
            } else {
                String idString = this.currentUser.getId() + "";
                btnSignIn.setVisible(false);
                btnRegister.setVisible(true);
                if (Helper.checkInclude(idString, numRegister)) {
                    btnRegister.setText("Cancel Registration");
                    lblNotify.setText("(*)You have already register this conference!");
                    type = 2;
                } else {
                    btnRegister.setText("Registration");
                    type = 1;
                    lblNotify.setText("");
                }
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
            String[] numRegister = this.conferenceItem.getParticipant().split(",");
            String idString = this.currentUser.getId() + "";
            btnSignIn.setVisible(false);
            btnRegister.setVisible(true);
            lblNotify.setText("");
            if (Helper.checkInclude(idString, numRegister)) {
                btnRegister.setText("Cancel Registration");
                type = 2;
                lblNotify.setText("(*)You have already register this conference!");
            } else {
                btnRegister.setText("Registration");
                type = 1;
                lblNotify.setText("");
            }

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void DoRegister(MouseEvent event) {
        if (type == 1) {
            String dataQuery = this.conferenceItem.getParticipant() + GlobalData.currentUser.getId() + ",";
            TblConferenceDAO.register(this.conferenceItem.getId(), dataQuery);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration success");
            alert.setHeaderText("Registration success");
            alert.setContentText("You have registered for this conference!");
            alert.showAndWait();
            btnRegister.setText("Cancel Registration");
            type = 2;
            lblNotify.setText("(*)You have already register this conference!");
            this.conferenceItem.setParticipant(dataQuery);
            String[] numRegister = this.conferenceItem.getParticipant().split(",");
            if (this.conferenceItem.getParticipant().equals("")) {
                lblNumRegis.setText("0");
            } else {
                lblNumRegis.setText((numRegister.length) + "");
            }
        } else {
            String[] arrStr = this.conferenceItem.getParticipant().split(",");
            ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(arrStr));
            arrayList.remove(GlobalData.currentUser.getId() + "");
            StringBuilder sbString = new StringBuilder("");

            //iterate through ArrayList
            for (String participant : arrayList) {
                sbString.append(participant).append(",");
            }

            //convert StringBuffer to String
            String strList = sbString.toString();
            TblConferenceDAO.unRegister(this.conferenceItem.getId(), strList);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cancel registration success");
            alert.setHeaderText("Cancel registration success");
            alert.setContentText("You have canceled register for this conference!");
            alert.showAndWait();
            this.conferenceItem.setParticipant(strList);
            String[] numRegister = this.conferenceItem.getParticipant().split(",");
            if (this.conferenceItem.getParticipant().equals("")) {
                lblNumRegis.setText("0");
            } else {
                lblNumRegis.setText((numRegister.length) + "");
            }
            btnRegister.setText("Registration");
            type = 1;
            lblNotify.setText("");
        }
        GlobalData.mainController.reloadContainer();
    }

    @FXML
    private void DoClose(MouseEvent event) {
        Stage stage = (Stage) btnCancle.getScene().getWindow();
        stage.close();
    }

    public void setConference(Tblconference conference, Tbluser currentUser) {
        this.conferenceItem = conference;
        this.currentUser = currentUser;
    }

}
