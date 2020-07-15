/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import entity.UserVisible;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Hoang IT
 */
public class ListRegisterController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private UserVisible userVisible;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setUser(UserVisible user){
        this.userVisible = user;
    }
    
}
