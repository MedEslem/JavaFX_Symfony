package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.SmsSender;

import java.io.IOException;

public class Code {

    @FXML
    private Button VerifyCodeBTN;

    @FXML
    private TextField CodeTXT;

    @FXML
    void Verify(ActionEvent event) {
        String CodeSMS = CodeTXT.getText().trim();

        if (!CodeSMS.equals(SmsSender.getCode())){
            showAlert("Code Not Correct");
        }
        else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChangePWD.fxml"));
                Parent root = loader.load();

                Scene currentScene = ((Node) event.getSource()).getScene();

                currentScene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
