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
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Hoang IT
 */
public class ProfilePageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private TextField txtFieldName;
    
    @FXML
    private TextField txtFieldEmail;
    
    @FXML
    private TextField txtFieldUsername;
    
    @FXML
    private PasswordField pwFieldOldpw;
    
    @FXML
    private PasswordField pwFieldNewpw;
    
    @FXML
    private PasswordField pwFieldCfpw;
    
    
    @FXML
    public void UpDateProfile(MouseEvent event){
        if(validateForm()){
            Tbluser user = new Tbluser();
            user.setName(txtFieldName.getText());
            user.setEmail(txtFieldEmail.getText());
            user.setId(GlobalData.currentUser.getId());
            if(pwFieldNewpw.getText().equals("")){
                user.setPassword("");
            }
            else{
                String mySecurePassword = PasswordUtils.generateSecurePassword(pwFieldNewpw.getText(), Config.salt);
                user.setPassword(mySecurePassword);
            }
            TblUserDAO.update(user);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update success");
            alert.setHeaderText("Update success");
            alert.setContentText("Update success!");
            alert.showAndWait();
            GlobalData.currentUser.setName(user.getName());
            GlobalData.currentUser.setEmail(user.getEmail());
            if(!user.getPassword().equals("")){
                String mySecurePassword = PasswordUtils.generateSecurePassword(pwFieldNewpw.getText(), Config.salt);
                GlobalData.currentUser.setPassword(user.getPassword());
            }
            GlobalData.mainController.reload();
        }
    }
    
    @FXML
    public void DoCancel(MouseEvent event){
        txtFieldName.setText(GlobalData.currentUser.getName());
        txtFieldEmail.setText(GlobalData.currentUser.getEmail());
        pwFieldOldpw.setText("");
        pwFieldNewpw.setText("");
        pwFieldCfpw.setText("");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        txtFieldName.setText(GlobalData.currentUser.getName());
        txtFieldEmail.setText(GlobalData.currentUser.getEmail());
        txtFieldUsername.setText(GlobalData.currentUser.getUsername());
    }   
    
    private boolean validateForm(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error has occur");
        
        if(txtFieldName.getText().trim().equals("")){
            alert.setContentText("Name can not be null");
            alert.showAndWait();
            return false;
        }
        
        if(txtFieldEmail.getText().trim().equals("")){
            alert.setContentText("Email can not be null");
            alert.showAndWait();
            return false;
        }
        
        if(!pwFieldNewpw.getText().trim().equals("")){
            boolean passwordMatch = PasswordUtils.verifyUserPassword(pwFieldOldpw.getText(), GlobalData.currentUser.getPassword(), Config.salt);
            if(!passwordMatch){
                alert.setContentText("Old password is incorrect");
                alert.showAndWait();
                return false;
            }      
        }
        
        if(!pwFieldNewpw.getText().trim().equals(pwFieldCfpw.getText().trim())){
            alert.setContentText("Confirm password does not match");
            alert.showAndWait();
            return false;
        }
        
        return true;
    }
    
}
