/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import dao.TblregisterconferenceDAO;
import java.util.ArrayList;

/**
 *
 * @author Hoang IT
 */
public class ConferenceVisible extends Tblconference{
    private ArrayList<Tblregisterconference> register;

    public ArrayList<Tblregisterconference> getRegister() {
        return register;
    }

    public void setRegister(ArrayList<Tblregisterconference> register) {
        this.register = register;
    }
    
    public ConferenceVisible(Tblconference conference){
        this.id = conference.getId();
        this.name = conference.getName();
        this.overview = conference.getOverview();
        this.description = conference.getDescription();
        this.address = conference.getAddress();
        this.limit = conference.getLimit();
        this.startTime = conference.getStartTime();
        this.endTime = conference.getEndTime();
        this.register = TblregisterconferenceDAO.allByConference(this.id);  
    }
    
    public ConferenceVisible(Tblregisterconference register){
        
    }
}
