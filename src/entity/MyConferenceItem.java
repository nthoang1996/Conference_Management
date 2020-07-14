/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import conferencemanagement.utils.Helper;
import dao.TblregisterconferenceDAO;

/**
 *
 * @author Hoang IT
 */
public class MyConferenceItem extends Tblconference {
    private String status;

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
        this.address = conference.getAddress();
        this.limit = conference.getLimit();
        this.startTime = conference.getStartTime();
        this.endTime = conference.getEndTime();
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
