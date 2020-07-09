/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement.utils;

/**
 *
 * @author Hoang IT
 */
public class Helper {
    public static boolean checkInclude(String str, String [] arrStr){
        for (String element : arrStr) 
        {
            if (element.equals(str)) {
                return true;
            }
        }
        return false;
    }
}
