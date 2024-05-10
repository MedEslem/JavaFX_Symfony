package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;
import services.UserService;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class addUser  { // Use clear and descriptive names

    private final UserService userService = new UserService(); // Use proper variable naming
    public PasswordField PwdIN;

    @FXML
    private TextField usernameIN;

    @FXML
    private TextField emailIN;

    @FXML
    private TextField imageIN;
    @FXML
    private ImageView logoIMG;



    @FXML
    private  ChoiceBox<String> roleIN; // Assuming roles are stored as a single string

    @FXML
    void addUser(ActionEvent event) throws SQLException {
        String role =roleIN.getValue();
        String username = usernameIN.getText().trim(); // Trim leading/trailing whitespace
        String email = emailIN.getText().trim();
        String image = imageIN.getText().trim();
        String password = PwdIN.getText().trim(); // Use clearer variable name

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
        if (image.isEmpty()) {
            showAlert("image cannot be empty");
            imageIN.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            showAlert("Email cannot be empty");
            PwdIN.requestFocus();
            return;
        }
        if (!isValidEmailAddress(email)) {
            showAlert("Please enter a valid email address");
            emailIN.requestFocus();
            return;
        }

        // Consider additional validation (e.g., email format, password strength)

        try {
            User user = new User(-1, email, image,null, password, 0.0, username, 0, 0);
            user.setRoles(user.parseRolesFromString(role));
            userService.add(user);
            showAlert("User added successfully!");
            clearFields(); // Clear input fields after successful addition (optional)
        } catch (SQLException e) {
            // Handle database errors gracefully
            showAlert("Error adding user: " + e.getMessage());
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
        imageIN.clear();
        PwdIN.clear();
    }
    @FXML
    void uploadimage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(imageIN.getScene().getWindow());
        if (file != null) {
            String filename = file.getName();
            imageIN.setText(filename);
        }
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
    private boolean isValidEmailAddress(String email) {
        String emailRegex = "^[\\w!#$%&'*+/=?^`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?$";
        return email.matches(emailRegex);
    }
}