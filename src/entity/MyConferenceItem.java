/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import conferencemanagement.utils.Helper;
import dao.TblLocationDAO;
import dao.TblregisterconferenceDAO;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Hoang IT
 */
public class MyConferenceItem extends Tblconference {
    private String status;
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
    
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public MyConferenceItem(Tblconference conference, int status){
        this.id = conference.getId();
        this.name = conference.getName();
        this.overview = conference.getOverview();
        this.description = conference.getDescription();
        this.locationId = conference.getLocationId();
        Tbllocation myLocation = TblLocationDAO.singleById(this.locationId);
        this.locationName = myLocation.getName();
        this.locationLimit = myLocation.getMyLimit();
        Date startTime = conference.getStartTime();
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(startTime);
        calStart.add(Calendar.HOUR, -7);
        Date endTime = conference.getEndTime();
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endTime);
        calEnd.add(Calendar.HOUR, -7);
        this.startTime = calStart.getTime();
        this.endTime = calEnd.getTime();
        switch (status) {
                case 1:
                    this.status = "Aprroving";
                    break;
                case 2:
                    this.status = "Accepted";
                    break;
                case 3:
                    this.status = "Denied";
                    break;
            }
    }
    
}
