package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import utils.DButils;
import javafx.scene.Node;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Login implements Initializable {

    @FXML
    private Button ConnectBTN;

    @FXML
    private TextField emailIN;

    @FXML
    private PasswordField pwdIN;
    @FXML
    private Label ForgetPasswordTxt;

    @FXML
    private Label signupTxt;
    @FXML
    private Button helpBTN;

    @FXML
    void SignUpPage(MouseEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signup.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ConnectBTN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DButils.logInUser(actionEvent,emailIN.getText(),pwdIN.getText());
            }
        });

    }
    @FXML
    void ForgetPwdPage(MouseEvent event) {
        try {
            // Load login.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PhoneNumber.fxml"));
            Parent loginRoot = loader.load();

            // Create a new Stage for the login window
            Stage loginStage = new Stage();
            loginStage.setTitle("Forget Password"); // Set the window title

            // Set the scene on the new Stage
            loginStage.setScene(new Scene(loginRoot));

            // Show the login window
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void helpPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Help.fxml"));
        Parent root = loader.load();


        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }


}