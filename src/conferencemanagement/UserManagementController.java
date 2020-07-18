/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import conferencemanagement.utils.GlobalData;
import dao.TblConferenceDAO;
import dao.TblUserDAO;
import entity.MyConferenceItem;
import entity.UserVisible;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hoang IT
 */
public class UserManagementController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TableView<UserVisible> tblUserVisible;

    ObservableList<UserVisible> list;

    private ChangeListener<UserVisible> listener;
    
    @FXML
    private ComboBox<String> cbSearchType;

    @FXML
    private TextField textFieldSearchQuery;

    int check = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cbSearchType.getItems().addAll("Name", "Username");
        cbSearchType.getSelectionModel().selectFirst();
        TableColumn<UserVisible, String> IdCol //
                = new TableColumn<UserVisible, String>("ID");

        TableColumn<UserVisible, String> nameCol//
                = new TableColumn<UserVisible, String>("Name");

        TableColumn<UserVisible, String> emailCol//
                = new TableColumn<UserVisible, String>("Email");

        TableColumn<UserVisible, String> usernameCol //
                = new TableColumn<UserVisible, String>("Username");

        TableColumn<UserVisible, String> roleNameCol //
                = new TableColumn<UserVisible, String>("Role");

        TableColumn<UserVisible, String> statusCol //
                = new TableColumn<UserVisible, String>("Status");

        IdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleNameCol.setCellValueFactory(new PropertyValueFactory<>("roleNameString"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("statusName"));

        IdCol.prefWidthProperty().bind(tblUserVisible.widthProperty().multiply(0.05));
        nameCol.prefWidthProperty().bind(tblUserVisible.widthProperty().multiply(0.25));
        emailCol.prefWidthProperty().bind(tblUserVisible.widthProperty().multiply(0.3));
        usernameCol.prefWidthProperty().bind(tblUserVisible.widthProperty().multiply(0.15));
        roleNameCol.prefWidthProperty().bind(tblUserVisible.widthProperty().multiply(0.15));
        statusCol.prefWidthProperty().bind(tblUserVisible.widthProperty().multiply(0.1));

        list = FXCollections.observableList(TblUserDAO.allMember());
        tblUserVisible.setItems(list);

        listener = (obs, oldSelection, newSelection) -> {
            try {
                if (newSelection == null) {
                    return;
                }
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UserDetail.fxml"));
                Parent parent = fxmlLoader.load();
                UserDetailController controller = fxmlLoader.<UserDetailController>getController();
                controller.setUser(newSelection);
                Scene scene = new Scene(parent, 600, 350);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                reloadTable();

            } catch (IOException ex) {
                Logger.getLogger(ConferenceDetailController.class.getName()).log(Level.SEVERE, null, ex);
            }
        };

        tblUserVisible.getSelectionModel().selectedItemProperty().addListener(listener);

        tblUserVisible.getColumns().addAll(IdCol, nameCol, emailCol, usernameCol, roleNameCol, statusCol);
    }

    public void reloadTable() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                list = FXCollections.observableList(TblUserDAO.allMember());
                tblUserVisible.setItems(list);
            }
        });

    }
    
    @FXML
    private void SearchItem(MouseEvent event) {
        if (textFieldSearchQuery.getText().trim().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error has occur");
            alert.setContentText("Query search is null");
            alert.showAndWait();
            return;
        }
        if (cbSearchType.getValue().equals("Name")) {
            searchByName();
        }
        else {
            searchByUsername();
        }
    }

    public void searchByName() {
        ArrayList<UserVisible> listQuery =TblUserDAO.allMember();
        ArrayList<UserVisible> listResult = new ArrayList<>();
        for (int i = 0; i < listQuery.size(); i++) {
            if (listQuery.get(i).getName().toLowerCase().indexOf(textFieldSearchQuery.getText().toLowerCase())>=0) {
                listResult.add(listQuery.get(i));
            }
        }
        list.setAll(listResult);
        tblUserVisible.setItems(list);
    }
    
    public void searchByUsername() {
        ArrayList<UserVisible> listQuery = TblUserDAO.allMember();
        ArrayList<UserVisible> listResult = new ArrayList<>();
        for (int i = 0; i < listQuery.size(); i++) {
            if (listQuery.get(i).getUsername().toLowerCase().indexOf(textFieldSearchQuery.getText().toLowerCase())>=0) {
                listResult.add(listQuery.get(i));
            }
        }
        list.setAll(listResult);
        tblUserVisible.setItems(list);
    }

}
