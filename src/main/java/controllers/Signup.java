package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import services.UserService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import models.Mailing;

import javax.mail.MessagingException;

public class Signup implements Initializable {
    private final UserService userService = new UserService();

    @FXML
    private CheckBox checkIN;

    @FXML
    private Button confirmIN;

    @FXML
    private PasswordField confirmPWDIN;

    @FXML
    private TextField emailIN;

    @FXML
    private TextField imgIN;

    @FXML
    private Label loginTxt;

    @FXML
    private PasswordField passwordIN;

    @FXML
    private Button uploadBTN;

    @FXML
    private TextField usernameIN;


    @FXML
    void uploadIMG(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(imgIN.getScene().getWindow());
        if (file != null) {
            String filename = file.getName();
            imgIN.setText(filename);
        }
    }
    @FXML
    void LoginPage(MouseEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    void signUp(ActionEvent event) throws MessagingException, GeneralSecurityException, IOException {
        Mailing M = new Mailing();
        String username = usernameIN.getText().trim();
        String email = emailIN.getText().trim();
        String image = imgIN.getText().trim();
        String password = passwordIN.getText().trim();
        String ConfimPassword = confirmPWDIN.getText().trim();
        String hashedPassword = BCrypt.hashpw(passwordIN.getText(), BCrypt.gensalt(13));

        if (username.isEmpty()) {
            showAlert("Username cannot be empty");
            usernameIN.requestFocus(); // Set focus for user correction
            return;
        }

        if (email.isEmpty()) {
            showAlert("Email cannot be empty");
            emailIN.requestFocus();
            return;
        }
        if (!isValidEmailAddress(email)) {
            showAlert("Please enter a valid email address");
            emailIN.requestFocus();
            return;
        }
        if (image.isEmpty()) {
            showAlert("image cannot be empty");
            imgIN.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            showAlert("Email cannot be empty");
            passwordIN.requestFocus();
            return;
        }
        if (!password.equals(ConfimPassword)) {
            showAlert("Passwords Must Match");
            confirmPWDIN.requestFocus();
            return;
        }
        if (!checkIN.isSelected()){
            showAlert("Must Check");
            checkIN.requestFocus();
            return;
        }




        try {
            User user = new User(-1, email, image,null, hashedPassword, 0.0, username, 0, 0);
            user.setRoles(user.parseRolesFromString("Client"));
            userService.add(user);
            showAlert("Account Created");
            M.sendMail("Welcome To Al3ab Games","Hello Our New Member",email);
            clearFields(); // Clear input fields after successful addition (optional)
        } catch (SQLException e) {
            // Handle database errors gracefully
            showAlert("Error Creating An Account: " + e.getMessage());
        } catch (Exception e) { // Catch broader exceptions for unexpected issues
            showAlert("An unexpected error occurred: " + e.getMessage());
        }

    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Or use appropriate Alert type (e.g., ERROR)
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        usernameIN.clear();
        emailIN.clear();
        imgIN.clear();
        passwordIN.clear();
        confirmPWDIN.clear();
    }
    private boolean isValidEmailAddress(String email) {
        String emailRegex = "^[\\w!#$%&'*+/=?^`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?$";
        return email.matches(emailRegex);
    }
}