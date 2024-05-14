package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utils.MyDataBase;

public class ChangePWD {

    @FXML
    private Button ChangePwdBTN;

    @FXML
    private PasswordField CpwdIN;

    @FXML
    private TextField emailIN;

    @FXML
    private PasswordField pwdIN;

    @FXML
    void changePWD(ActionEvent event) throws SQLException {
        String email = emailIN.getText().trim();
        String password = pwdIN.getText().trim();
        String confirmedPassword = CpwdIN.getText().trim();

        // Validate input
        if (email.isEmpty() || password.isEmpty() || confirmedPassword.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmedPassword)) {
            showAlert("Passwords must match.");
            return;
        }


        Connection connection = MyDataBase.getInstance().getConnection();


        String sql = "UPDATE User SET password = ? WHERE email = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,password);
        preparedStatement.setString(2, email);

        int rowsUpdated = preparedStatement.executeUpdate();

        if (rowsUpdated == 0) {
            // Handle user not found scenario
            showAlert("User not found in the database.");
        } else {
            showAlert("Password changed successfully!");
        }

        preparedStatement.close();
        connection.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
