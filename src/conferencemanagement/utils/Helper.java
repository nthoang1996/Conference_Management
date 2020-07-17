/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement.utils;

import conferencemanagement.NewConferenceController;
import entity.ConferenceVisible;
import entity.Tblregisterconference;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author Hoang IT
 */
public class Helper {

    public static int checkInclude(ArrayList<Tblregisterconference> listConference) {
        for (int i = 0; i < listConference.size(); i++) {
            if (GlobalData.currentUser.getId() == listConference.get(i).getIdUser()) {
                return listConference.get(i).getStatus();
            }
        }
        return 0;
    }

    public static String decodeFilePathValue(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    public static boolean createImage(String output, String pathImage) {
        Image image = new Image("file:///" + pathImage);
        BufferedImage bimage = SwingFXUtils.fromFXImage(image, null);
        File outputfile = new File(output);
        try {
            ImageIO.write(bimage, "png", outputfile);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(NewConferenceController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
