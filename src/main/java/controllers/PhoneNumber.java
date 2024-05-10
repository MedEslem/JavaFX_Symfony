package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import models.SmsSender;

import java.io.IOException;

public class PhoneNumber {
    private SmsSender sms = new SmsSender();

    @FXML
    private TextField PhoneIN;

    @FXML
    private Button sendBTN;

    @FXML
    void SendCode(ActionEvent event) throws IOException {
        String phone = PhoneIN.getText().trim();
        String Code = sms.SmsPwd(phone);
        SmsSender.setCode(Code);
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Code.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        showAlert("Check your phone You will Receive a Code");

    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
