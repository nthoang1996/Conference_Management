/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import dao.TblLocationDAO;
import dao.TblregisterconferenceDAO;
import java.util.ArrayList;

/**
 *
 * @author Hoang IT
 */
public class ConferenceVisible extends Tblconference{
    private ArrayList<Tblregisterconference> register;
    private String locationName;
    private int locationLimit;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getLocationLimit() {
        return locationLimit;
    }

    public void setLocationLimit(int locationLimit) {
        this.locationLimit = locationLimit;
    }
    

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
        this.locationId = conference.getLocationId();
        this.startTime = conference.getStartTime();
        this.endTime = conference.getEndTime();
        this.register = TblregisterconferenceDAO.allByConference(this.id);
        Tbllocation myLocation = TblLocationDAO.singleById(this.locationId);
        this.locationName = myLocation.getName();
        this.locationLimit = myLocation.getMyLimit();
    }
    
    public ConferenceVisible(Tblconference conference, boolean admin){
        this.id = conference.getId();
        this.name = conference.getName();
        this.overview = conference.getOverview();
        this.description = conference.getDescription();
        this.locationId = conference.getLocationId();
        this.startTime = conference.getStartTime();
        this.endTime = conference.getEndTime();
        this.register = TblregisterconferenceDAO.allByConferenceIncludeDeny(this.id);
        Tbllocation myLocation = TblLocationDAO.singleById(this.locationId);
        this.locationName = myLocation.getName();
        this.locationLimit = myLocation.getMyLimit();
    }
    
    public ConferenceVisible(Tblregisterconference register){
        
    }
}
