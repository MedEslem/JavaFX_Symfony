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
import javafx.scene.image.Image;
import models.LocationInfo;
import models.SmsSender;
import models.User;
import utils.DButils;
import utils.SessionManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;


public class Profile implements Initializable {

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
    private Text emailTXT;
    @FXML
    private ImageView imageProf;

    @FXML
    private Text locationTXT;
    @FXML
    private Text usernameTXT;
    @FXML
    private Button forum;
    @FXML
    private Button modifyBTN;
    LocationInfo Location = new LocationInfo();
    SmsSender SMS = new SmsSender();

    @FXML
    void HomePage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/connected.fxml"));
            Parent root = loader.load();
            Connected C = loader.getController();
            C.set_info_connected();
            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void set_info_connected(){
        connectedTxt.setText(SessionManager.getUsername());
        emailTXT.setText(SessionManager.getEmail());
        usernameTXT.setText(SessionManager.getUsername());
        Location=SMS.getLocalisation();
        locationTXT.setText(Location.getCountry()+" , "+Location.getCity());
        File file = new File("C:/xampp/htdocs/ProfileIMG/"+SessionManager.getImage_path());
        Image image = new Image(file.toURI().toString());
        imageProf.setImage(image);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoutIN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DButils.changeScene(actionEvent,"login.fxml",null,"Log IN");
            }
        });

    }
    @FXML
    void adminPage(ActionEvent event) {
        User U = new User();
        if (U.buildRoleStringFromRoles(SessionManager.getRoles()).equals("Admin")) {
            try{
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
    @FXML
    void modifyProfile(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifyProfile.fxml"));
            Parent root = loader.load();
            ModifyProfile Mprofile = loader.getController();
            Mprofile.fillforum();
            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
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