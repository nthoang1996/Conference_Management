/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author Hoang IT
 */
public class UserVisible extends Tbluser{
    private String roleNameString;
    private String statusName;

    public String getRoleNameString() {
        return roleNameString;
    }

    public void setRoleNameString(String roleNameString) {
        this.roleNameString = roleNameString;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    
    public UserVisible(Tbluser user){
       this.id = user.id;
       this.name = user.name;
       this.email = user.email;
       this.username = user.username;
       this.password = user.password;
       this.roleId = user.roleId;
       this.isDisabled = user.isDisabled;
       this.isDeleted = user.isDeleted;
       switch(this.roleId){
           case 1:
               roleNameString = "Member";
               break;
           case 2:
               roleNameString = "Admin";
               break;
       }
       
       if(this.isDisabled){
           statusName = "Disabled";
       }
       else{
           statusName = "Enabled";
       }
    }
}
