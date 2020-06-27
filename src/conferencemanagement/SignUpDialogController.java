/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import conferencemanagement.utils.Config;
import conferencemanagement.utils.PasswordUtils;
import entity.Tbluser;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import dao.TblUserDAO;

/**
 * FXML Controller class
 *
 * @author Hoang IT
 */
public class SignUpDialogController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField textFieldName;
    
    @FXML
    private TextField textFieldEmail;
    
    @FXML
    private TextField textFieldUsername;
    
    @FXML
    private PasswordField pwFieldPassword;
    
    @FXML
    private PasswordField pwFieldConfirmPassword;
    
    @FXML
    private Button btnCancle;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void DoCancle(MouseEvent event){
        Stage stage = (Stage)btnCancle.getScene().getWindow();
        stage.close();
    }
    
    private boolean validateForm(){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error has occur");
        
        if(textFieldName.getText().trim().equals("")){
            alert.setContentText("Name can not be null");
            alert.showAndWait();
            return false;
        }
        
        if(textFieldEmail.getText().trim().equals("")){
            alert.setContentText("Email can not be null");
            alert.showAndWait();
            return false;
        }
        
        if(textFieldUsername.getText().trim().equals("")){
            alert.setContentText("Username can not be null");
            alert.showAndWait();
            return false;
        }
        
        if(TblUserDAO.singleByUsername(textFieldUsername.getText()) != null){
            alert.setContentText("Username is exist. Please chose order username!");
            alert.showAndWait();
            return false;
        }
        
        if(pwFieldPassword.getText().trim().equals("")){
            alert.setContentText("Password can not be null");
            alert.showAndWait();
            return false;
        }
        
        if(!pwFieldPassword.getText().trim().equals(pwFieldConfirmPassword.getText().trim())){
            alert.setContentText("Confirm password does not match");
            alert.showAndWait();
            return false;
        }
        
        return true;
    }
    
    @FXML
    private void DoSignUp(MouseEvent event){
        if(validateForm()){
            String name = textFieldName.getText().trim();
            String email = textFieldEmail.getText().trim();
            String username = textFieldUsername.getText().trim();
            String password = pwFieldPassword.getText().trim();
            String mySecurePassword = PasswordUtils.generateSecurePassword(password, Config.salt);
            int roleId = 3;
            Tbluser userRegister = new Tbluser(name, email, username, mySecurePassword, roleId, false);
            TblUserDAO.insert(userRegister);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Sign up success");
            alert.setHeaderText("Sign up success");
            alert.setContentText("Sign up success. Now you can use this to login!");
            alert.showAndWait();
            DoCancle(event);
        }     
    }  
}
