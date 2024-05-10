package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.Mailing;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class Mailling {

    @FXML
    private TextArea contenuTXT;

    @FXML
    private TextField emailIN;

    @FXML
    private ImageView logoIMG;

    @FXML
    private Button sendBTN;

    @FXML
    private TextField subjectIN;

    @FXML
    void Email(ActionEvent event) throws MessagingException, GeneralSecurityException, IOException {
        Mailing Mail = new Mailing();
        Mail.sendMail(subjectIN.getText(),contenuTXT.getText(),emailIN.getText());
        showAlert("Email Sent Successfully !");
        clearFields();

    }

    @FXML
    void userPage(MouseEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserTable.fxml"));
            Parent root = loader.load();
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
    private void clearFields() {
        emailIN.clear();
        subjectIN.clear();
        contenuTXT.clear();
    }

}
