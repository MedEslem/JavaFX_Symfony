package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import models.Mailing;
import models.User;
import services.UserService;
import utils.SessionManager;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

public class ModifyProfile {
    private final UserService us = new UserService();

    @FXML
    private Button confirmIN;

    @FXML
    private PasswordField  CpasswordIN;

    @FXML
    private TextField emailIN;

    @FXML
    private TextField imgIN;

    @FXML
    private ImageView logoIMG;

    @FXML
    private PasswordField  passwordIN;

    @FXML
    private Button uploadBTN;

    @FXML
    private TextField usernameIN;


    @FXML
    void ModifierProfile(ActionEvent event) throws SQLException, MessagingException, GeneralSecurityException, IOException {
        String erreur = controleDeSaisire();
        Mailing M = new Mailing();
        if (erreur.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ouups Invalide Form!");
            alert.setContentText(erreur);
            alert.showAndWait();
        } else {
            User U = SessionManager.UpdateUser();
            U.setUsername(usernameIN.getText());
            U.setEmail(emailIN.getText());
            U.setImage_path(imgIN.getText());
            U.setPassword(passwordIN.getText());
            us.update(U);
            showAlert("User Updated successfully!");
            SessionManager.setEmail(emailIN.getText());
            SessionManager.setPassword(passwordIN.getText());
            SessionManager.setUsername(usernameIN.getText());
            SessionManager.setImage_path(imgIN.getText());
            M.sendMail("Your Profile ","Your profile has been updated",SessionManager.getEmail());
        }
    }
    void fillforum() {
        usernameIN.setText(SessionManager.getUsername());
        emailIN.setText(SessionManager.getEmail());
        imgIN.setText(SessionManager.getImage_path());
        passwordIN.setText(SessionManager.getPassword());

    }


    public String controleDeSaisire () {
        String erreur = "";
        if (usernameIN.getText().isEmpty()) {
            erreur += "-You must Fill Username Text Field\n";
        }

        if (emailIN.getText().isEmpty()) {
            erreur += "-You must Fill Email Text Field\n";
        }
        if (imgIN.getText().isEmpty()) {
            erreur += "-You must Put Image \n";
        }
        if (passwordIN.getText().isEmpty()){
            erreur += "-You must Fill Password Text Field\n";
        }
        if (CpasswordIN.getText().isEmpty()){
            erreur += "-You must Confirm Your Password\n";
        }
        if (!passwordIN.getText().equals(CpasswordIN.getText())){
            erreur += "-You must Must Your Password\n";
        }



        return erreur;
    }



    @FXML
    void ProfilePage(MouseEvent event) {
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
    void uploadIMG(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(imgIN.getScene().getWindow());
        if (file != null) {
            String filename = file.getName();
            imgIN.setText(filename);
        }

    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Or use appropriate Alert type (e.g., ERROR)
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}