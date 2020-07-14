/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement.utils;

import entity.ConferenceVisible;
import entity.Tblregisterconference;
import java.util.ArrayList;

/**
 *
 * @author Hoang IT
 */
public class Helper {
    public static int checkInclude(ArrayList<Tblregisterconference> listConference){
        for(int i=0; i<listConference.size();i++){
            if(GlobalData.currentUser.getId() == listConference.get(i).getIdUser()){
                return listConference.get(i).getStatus();
            }
        }
        return 0;
    }
}
