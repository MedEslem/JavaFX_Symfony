package utils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import controllers.*;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.*;


import javafx.event.ActionEvent;
import models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class DButils {
   public static User connectedUser = new User();
    public static void changeScene(ActionEvent event, String fxmlFile, String username, String title){
        Parent root = null;
        if(username != null){
            try {
                FXMLLoader loader = new FXMLLoader(DButils.class.getResource("/"+fxmlFile)); // Assuming "resources" is at the project root
                root = loader.load();
                Connected loggedIN = loader.getController();
                loggedIN.set_info_connected();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                FXMLLoader loader = new FXMLLoader(DButils.class.getResource("/"+fxmlFile)); // Assuming "resources" is at the project root
                root = loader.load();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Stage stage= (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }
    public static void logInUser (ActionEvent event, String email, String password){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PasswordEncoder symfonyPasswordEncoder = new BCryptPasswordEncoder();
        PasswordEncoder javaPasswordEncoder = new BCryptPasswordEncoder(13);
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/al3abgames","root","");
                    preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE email = ?");
                    preparedStatement.setString( 1, email);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database!");
                Alert alert = new Alert (Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect!");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("password");
                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(13));
                    String retrievedUsername = resultSet.getString("username");
                    String retrievedEmail = resultSet.getString("email");
                    int retrievedId = resultSet.getInt("id");
                    String retrievedImage = resultSet.getString("image_path");
                    String retrievedRole = resultSet.getString("roles");
                    double retrievedWallet = resultSet.getDouble("wallet");
                    int retrievedAccess = resultSet.getInt("access");
                    if (symfonyPasswordEncoder.matches(password, retrievedPassword )|| javaPasswordEncoder.matches(password, retrievedPassword)) {
                        SessionManager.setUsername(retrievedUsername);
                        SessionManager.setEmail(retrievedEmail);
                        SessionManager.setId(retrievedId);
                        SessionManager.setRoles(retrievedRole);
                        SessionManager.setImage_path(retrievedImage);
                        SessionManager.setWallet(retrievedWallet);
                        SessionManager.setAccess(retrievedAccess);
                        SessionManager.setPassword(password);
                        User conUser = new User(retrievedUsername,retrievedId);
                        connectedUser=conUser;
                        if (SessionManager.getAccess()==0){
                        changeScene(event,"connected.fxml",retrievedUsername,"Welcome");
                            }
                            else {
                            changeScene(event,"Banned.fxml",null,"BAN");
                        }
                    } else {
                        System.out.println("Passwords did not match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect!");
                        System.out.println(hashedPassword);
                        System.out.println(retrievedPassword);
                        System.out.println(password);
                        alert.show();
                    }
                }
            }
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
                if (connection != null ) {
                    try {
                        connection.close();
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
        }
     }

    }
