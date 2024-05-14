package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import utils.DButils;
import utils.SessionManager;


import java.io.IOException;
import java.net.URL;
import java.security.PrivateKey;
import java.util.ResourceBundle;
import models.User;

public class Connected implements Initializable {
   private User U = new User();

    @FXML
    private Button HomeBTN;

    @FXML
    private Button compBTN;


    @FXML
    private Label connectedTxt;

    @FXML
    private Button eventBTN;

    @FXML
    private Button logoutIN;

    @FXML
    private Button profileBTN;

    @FXML
    private Button shopBTN;
    @FXML
    private Button adminBTN;

    @FXML
    private Button forum;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoutIN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            DButils.changeScene(actionEvent,"login.fxml",null,"Log IN");
            }
        });

    }
    public void set_info_connected(){
        connectedTxt.setText(SessionManager.getUsername());

    }

    @FXML
    void ProfilePage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Parent root = loader.load();
            Profile P = loader.getController();
            P.set_info_connected();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void adminPage(ActionEvent event) {

        if (U.buildRoleStringFromRoles(SessionManager.getRoles()).equals("Admin")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserTable.fxml"));
                Parent root = loader.load();
                Scene currentScene = ((Node) event.getSource()).getScene();

                currentScene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
           showAlert("You Must Be An Admin");
       }


    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Or use appropriate Alert type (e.g., ERROR)
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void allerVersBtnMessagerie() {
        changeScene2("/listThreads.fxml");
    }

    private void changeScene2(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            forum.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void ShopPage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Asma/Shop.fxml"));
            Parent root = loader.load();
            Shop P = loader.getController();
            P.set_info_connected();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void EventsPage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Lina/FrontEvent.fxml"));
            Parent root = loader.load();
            FrontEvent Fe = loader.getController();
            Fe.set_info_connected();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void CompPage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Nour/EquipeGUI.fxml"));
            Parent root = loader.load();
            EquipeController E = loader.getController();
            E.set_info_connected();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
