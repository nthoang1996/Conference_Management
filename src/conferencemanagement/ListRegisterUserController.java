/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import dao.TblUserDAO;
import entity.ConferenceVisible;
import entity.UserVisible;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hoang IT
 */
public class ListRegisterUserController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private ConferenceVisible conference;

    @FXML
    private TableView tblUser;

    ObservableList<UserVisible> list;

    boolean isAdmin = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Platform.runLater(() -> {
            TableColumn<UserVisible, String> IdCol //
                    = new TableColumn<UserVisible, String>("ID");

            TableColumn<UserVisible, String> nameCol//
                    = new TableColumn<UserVisible, String>("Name");

            TableColumn<UserVisible, String> emailCol//
                    = new TableColumn<UserVisible, String>("Email");

            TableColumn<UserVisible, String> usernameCol //
                    = new TableColumn<UserVisible, String>("Username");

            IdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
            usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

            IdCol.prefWidthProperty().bind(tblUser.widthProperty().multiply(0.05));
            nameCol.prefWidthProperty().bind(tblUser.widthProperty().multiply(0.25));
            emailCol.prefWidthProperty().bind(tblUser.widthProperty().multiply(0.35));
            usernameCol.prefWidthProperty().bind(tblUser.widthProperty().multiply(0.35));

            if (isAdmin) {
                list = FXCollections.observableList(TblUserDAO.allByConferenceId(this.conference.getId()));
                tblUser.setItems(list);
                tblUser.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                    try {
                        if (newSelection == null) {
                            newSelection = oldSelection;
                            return;
                        }
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConferenceByUser.fxml"));
                        Parent parent = fxmlLoader.load();
                        ConferenceByUserController controller = fxmlLoader.<ConferenceByUserController>getController();
                        UserVisible item = (UserVisible) newSelection;
                        controller.setConference(conference);
                        controller.setUser(item);
                        Scene scene = new Scene(parent, 600, 400);
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setScene(scene);
                        stage.showAndWait();
                        reloadTable();
                    } catch (IOException ex) {
                        Logger.getLogger(ConferenceDetailController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
            else{
                list = FXCollections.observableList(TblUserDAO.allAcceptedByConferenceId(this.conference.getId()));
                tblUser.setItems(list); 
            }
            tblUser.getColumns().addAll(IdCol, nameCol, emailCol, usernameCol);
        });
    }

    public void setConference(ConferenceVisible conference, boolean isAdmin) {
        this.conference = conference;
        this.isAdmin = isAdmin;
    }

    public void reloadTable() {
        Platform.runLater(() -> {
            list = FXCollections.observableList(TblUserDAO.allByConferenceId(this.conference.getId()));
            tblUser.setItems(list);
        });

    }

}
