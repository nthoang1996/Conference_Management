/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conferencemanagement;

import conferencemanagement.utils.Config;
import conferencemanagement.utils.GlobalData;
import dao.TblConferenceDAO;
import entity.ConferenceVisible;
import entity.Tblconference;
import java.awt.Dimension;
import java.awt.Scrollbar;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author Hoang IT
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private BorderPane bpBody;

    @FXML
    private Button btnSignin;

    @FXML
    private Label lblCurrentUser;

    @FXML
    private AnchorPane apContainer;

    @FXML
    private ListView<ConferenceVisible> lvConference;

    private ObservableList<ConferenceVisible> conferenceObservableList;

    final ToggleGroup groupType = new ToggleGroup();

    @FXML
    private RadioButton radioViewList;

    @FXML
    private RadioButton radioViewCard;

    @FXML
    private BorderPane bpListconference;
    
    @FXML
    private Button btnProfile;
    
    @FXML
    private Button btnMyConference;
    
    @FXML
    private Button btnManageUser;

    @FXML
    private void ToHome(MouseEvent event) {
        bpBody.setCenter(apContainer);
    }

    private ScrollPane spContainer = new ScrollPane();

    private VBox vbContainer = new VBox();

    @FXML
    private void ProfilePage(MouseEvent event) {
        loadPage("ProfilePage");
    }
    
    @FXML
    private void MyConference(MouseEvent event) {
        loadPage("MyConference");
    }
    
    @FXML
    private void ManageUser(MouseEvent event) {
        loadPage("UserManagment");
    }

    @FXML
    private void SignIn(MouseEvent event) {
        try {
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("SignInDialog.fxml"));
            Parent parent = fXMLLoader.load();
            SignInDialogController signInDialogController = fXMLLoader.<SignInDialogController>getController();
            Scene scene = new Scene(parent, 600, 400);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            if (GlobalData.currentUser != null) {
                btnSignin.setVisible(false);
                lblCurrentUser.setText("Hi " + GlobalData.currentUser.getName() + "!");
                lblCurrentUser.setVisible(true);
            }
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void checkAuthorize(){
        if(GlobalData.currentUser == null){
            btnProfile.setVisible(false);
            btnMyConference.setVisible(false);
        }
        else if(GlobalData.currentUser.getRoleId() == 1){
            btnProfile.setVisible(true);
            btnMyConference.setVisible(true);
        }
        else if(GlobalData.currentUser.getRoleId() == 2){
            btnManageUser.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        GlobalData.mainController = this;
        checkAuthorize();
        GridPane gpConference = new GridPane();
        lblCurrentUser.setVisible(false);
        lvConference.setItems(conferenceObservableList);
        lvConference.setCellFactory(new Callback<ListView<ConferenceVisible>, ListCell<ConferenceVisible>>() {
            @Override
            public ListCell<ConferenceVisible> call(ListView<ConferenceVisible> studentListView) {
                return new ConferenceItemController();
            }
        });
        int colCnt = 0, rowCnt = 0;
        for (int i = 0; i < conferenceObservableList.size(); i++) {
            Parent root = null;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConferenceCardItem.fxml"));
                root = (Parent) fxmlLoader.load();
                ConferenceCardItemController controller = fxmlLoader.<ConferenceCardItemController>getController();
                controller.setConference(conferenceObservableList.get(i));
                fxmlLoader.setController(controller);
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            GridPane.setMargin(root, new Insets(20, 35, 20, 35));
            gpConference.add(root, colCnt, rowCnt);
            colCnt++;
            if (colCnt >= Config.Columns) {
                rowCnt++;
                colCnt = 0;
            }
        }

        spContainer.setContent(gpConference);
        vbContainer.getChildren().add(spContainer);
        VBox.setMargin(this.spContainer, new Insets(20, 20, 20, 20));

        radioViewList.setToggleGroup(groupType);
        radioViewCard.setToggleGroup(groupType);
        groupType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ob,
                    Toggle o, Toggle n) {

                RadioButton rb = (RadioButton) groupType.getSelectedToggle();

                if (rb != null) {
                    if (rb.getText().equals(radioViewList.getText())) {
                        bpListconference.setCenter(lvConference);
                    } else {
                        bpListconference.setCenter(vbContainer);
                    }
                }
            }
        });
    }

    public FXMLDocumentController() {
        List<ConferenceVisible> listConference = TblConferenceDAO.all();
        conferenceObservableList = FXCollections.observableArrayList(listConference);
    }

    private void loadPage(String page) {
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource(page + ".fxml"));
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        bpBody.setCenter(root);
    }

    public void reload() {
        if (GlobalData.currentUser != null) {
            btnSignin.setVisible(false);
            lblCurrentUser.setText("Hi " + GlobalData.currentUser.getName() + "!");
            lblCurrentUser.setVisible(true);
            checkAuthorize();
        }
    }
    
    public void reloadContainer(){
        ArrayList<ConferenceVisible> listConference = (ArrayList < ConferenceVisible >)TblConferenceDAO.all();
        conferenceObservableList = FXCollections.observableArrayList(listConference);
        GridPane gpConference = new GridPane();
        lvConference.setItems(conferenceObservableList);
        lvConference.setCellFactory(new Callback<ListView<ConferenceVisible>, ListCell<ConferenceVisible>>() {
            @Override
            public ListCell<ConferenceVisible> call(ListView<ConferenceVisible> studentListView) {
                return new ConferenceItemController();
            }
        });
        int colCnt = 0, rowCnt = 0;
        for (int i = 0; i < conferenceObservableList.size(); i++) {
            Parent root = null;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConferenceCardItem.fxml"));
                root = (Parent) fxmlLoader.load();
                ConferenceCardItemController controller = fxmlLoader.<ConferenceCardItemController>getController();
                controller.setConference(conferenceObservableList.get(i));
                fxmlLoader.setController(controller);
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            GridPane.setMargin(root, new Insets(20, 35, 20, 35));
            gpConference.add(root, colCnt, rowCnt);
            colCnt++;
            if (colCnt >= Config.Columns) {
                rowCnt++;
                colCnt = 0;
            }
        }

        spContainer.setContent(gpConference);
        vbContainer.getChildren().clear();
        vbContainer.getChildren().add(spContainer);
        VBox.setMargin(this.spContainer, new Insets(20, 20, 20, 20));
        
    }
}
