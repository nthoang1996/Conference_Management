/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import conferencemanagement.utils.Config;
import conferencemanagement.utils.GlobalData;
import conferencemanagement.utils.PasswordUtils;
import dao.TblUserDAO;
import entity.Tbluser;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hoang IT
 */
public class SignInDialogController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button btnCancle;
    
    @FXML
    private Button btnSignUp;
    
    @FXML 
    private TextField textFieldUsername;
    
    @FXML
    private PasswordField pwFieldPassword;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void SignUp(MouseEvent event){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("SignUpDialog.fxml"));
            Parent parent = fXMLLoader.load();
            SignUpDialogController signInDialogController = fXMLLoader.<SignUpDialogController>getController();
            Scene scene = new Scene(parent, 600, 400);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
//            DoCancle(event);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void DoCancle(MouseEvent event){
        Stage stage = (Stage)btnCancle.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void DoSignIn(MouseEvent event){
        Tbluser user = TblUserDAO.singleByUsername(textFieldUsername.getText());
        if(user == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sign in failed");
            alert.setHeaderText("Sign in failed");
            alert.setContentText("Sign in failed. Username does not exist!");
            alert.showAndWait();
            return;
        }
        boolean passwordMatch = PasswordUtils.verifyUserPassword(pwFieldPassword.getText(), user.getPassword(), Config.salt);
        if(passwordMatch) 
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sign in success");
            alert.setHeaderText("Sign in success");
            alert.setContentText("Sign in success. Hello " + user.getName() + "!");
            GlobalData.currentUser = user;
            alert.showAndWait();
            GlobalData.mainController.reload();
            DoCancle(event);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sign in failed");
            alert.setHeaderText("Sign in failed");
            alert.setContentText("Sign in failed. Incorrect password");
            alert.showAndWait();
        }
    }
}
