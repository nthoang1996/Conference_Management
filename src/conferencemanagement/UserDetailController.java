/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import dao.TblUserDAO;
import entity.UserVisible;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hoang IT
 */
public class UserDetailController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private UserVisible user;

    @FXML
    private Label lblName;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblRoleName;

    @FXML
    private Label lblStatus;

    @FXML
    private Button btnCancle;

    @FXML
    private Button btnDisable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Platform.runLater(() -> {
            if (this.user != null) {
                lblName.setText(this.user.getName());
                lblEmail.setText(this.user.getEmail());
                lblUsername.setText(this.user.getUsername());
                lblRoleName.setText(this.user.getRoleNameString());
                lblStatus.setText(this.user.getStatusName());
                if (this.user.isIsDisabled()) {
                    btnDisable.setText("Enable");
                } else {
                    btnDisable.setText("Disable");
                }
            }
        });
    }

    @FXML
    private void DoClose(MouseEvent event) {
        Stage stage = (Stage) btnCancle.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void DoDisable(MouseEvent event) {
        if (TblUserDAO.updateStatus(this.user.getId(), !this.user.isIsDisabled())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(btnDisable.getText() + " success");
            alert.setHeaderText(btnDisable.getText() + " success");
            alert.setContentText("Action success!");
            alert.showAndWait();
            this.user.setIsDisabled(!this.user.isIsDisabled());
            if (this.user.isIsDisabled()) {
                this.user.setStatusName("Disabled");
            } else {
                this.user.setStatusName("Enabled");
            }
            reload();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(btnDisable.getText() + " error");
            alert.setHeaderText(btnDisable.getText() + " error");
            alert.setContentText("An error has occured! Please try again later.");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void ShowListRegister(MouseEvent event) {
        try {
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("ListRegister.fxml"));
            Parent parent = fXMLLoader.load();
            ListRegisterController listRegisterDialogController = fXMLLoader.<ListRegisterController>getController();
            listRegisterDialogController.setUser(this.user);
            Scene scene = new Scene(parent, 1020, 720);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setUser(UserVisible user) {
        this.user = user;
    }

    public void reload() {
        if (this.user.isIsDisabled()) {
            btnDisable.setText("Enable");
        } else {
            btnDisable.setText("Disable");
        }
        lblStatus.setText(this.user.getStatusName());
    }

}
