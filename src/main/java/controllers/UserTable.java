package controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;
import services.UserService;
import utils.DButils;
import utils.SessionManager;
import javafx.scene.image.ImageView;

public class UserTable implements Initializable {

    private final UserService us = new UserService();
    private ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    private TableView<User> userTable;

    @FXML
    private Button deleteBTN;
    @FXML
    private Button addBTN;
    @FXML
    private ImageView mailIMG;

    @FXML
    private AnchorPane table;
    @FXML
    private TextField RechercheIN;

    @FXML
    private TableColumn<User, String> usernameCol;

    @FXML
    private TableColumn<User, String> emailCol;
    @FXML
    private TableColumn<User, Integer> AccessCol;

    @FXML
    private TextField usernameIN;

    @FXML
    private TextField emailIN;

    @FXML
    private TextField imageIN;
    @FXML
    private ChoiceBox<String> roleIN;
    @FXML
    private Button logoutIN;
    @FXML
    private Label connectedTXT;
    @FXML
    private Button banBTN;
    @FXML
    private Button memberBTN;
    @FXML
    private ImageView pdfIMG;
    @FXML
    private Button ShopBTN;
    @FXML
    private Button eventsBTN;
    @FXML
    private Button CompBTN;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Retrieve users and populate the ObservableList
            users.setAll(us.getAll());

            // Set cell value factories (after data is available in users)
            usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
            emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
            AccessCol.setCellValueFactory(new PropertyValueFactory<>("access"));




            // Bind TableView to the ObservableList
            userTable.setItems(users);
            recherche_avance();

        } catch (SQLException e) {
            // Handle database errors
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Failed to retrieve users: " + e.getMessage());
            alert.showAndWait();
        }

        // Implement user deletion on button click
        deleteBTN.setOnAction(event -> {
            try {
                User userToDelete = userTable.getSelectionModel().getSelectedItem();

                if (userToDelete != null) {
                    us.delete(userToDelete.getId());

                    // Remove user from ObservableList and refresh table
                    users.remove(userToDelete);
                    showAlert("User Removed successfully!");
                    userTable.refresh();
                } else {
                    // Handle no selection case
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("Please select a user to delete.");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                // Handle database errors during deletion
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Failed to delete user: " + e.getMessage());
                alert.showAndWait();
            }
        });
        logoutIN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DButils.changeScene(actionEvent,"login.fxml",null,"Log IN");
            }
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Or use appropriate Alert type (e.g., ERROR)
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    void fillforum(MouseEvent event) {
        User U = userTable.getSelectionModel().getSelectedItem();
        if (U != null) {
            usernameIN.setText(U.getUsername());
            emailIN.setText(U.getEmail());
            imageIN.setText(U.getImage_path());
            roleIN.setValue(U.buildRoleStringFromRoles(U.getRoles()));
        }
    }

    @FXML
    void UpdateUser(ActionEvent event) throws SQLException {
        String erreur = controleDeSaisire();
        if (erreur.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ouups Invalide Form!");
            alert.setContentText(erreur);
            alert.showAndWait();
        } else {
            User U = userTable.getSelectionModel().getSelectedItem();
            U.setUsername(usernameIN.getText());
            U.setEmail(emailIN.getText());
            U.setImage_path(imageIN.getText());
            U.setRoles(U.parseRolesFromString(roleIN.getValue()));
            us.update(U);
            showAlert("User Updated successfully!");
            userTable.refresh();
        }
    }


    public String controleDeSaisire () {
        String erreur = "";
        if (usernameIN.getText().isEmpty()) {
            erreur += "-Username vide\n";
        }

        if (emailIN.getText().isEmpty()) {
            erreur += "-Email vide\n";
        }
        if (imageIN.getText().isEmpty()) {
            erreur += "-Image vide\n";
        }
        if (roleIN.getValue() == null) {
            erreur += "-tags vide\n";
        }


        return erreur;
    }
    @FXML
    void addUserPage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addUser.fxml"));
            Parent root = loader.load();
            addUser c = loader.getController();
            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void memberPage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/connected.fxml"));
            Parent root = loader.load();
            Connected c = loader.getController();
            c.set_info_connected();
            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void set_info_connected(){
        connectedTXT.setText(SessionManager.getUsername());
    }
    void recherche_avance() throws SQLException {
        ObservableList<User> data= FXCollections.observableArrayList(us.getAll());
        FilteredList<User> filteredList=new FilteredList<>(data, u->true);
        RechercheIN.textProperty().addListener(
                (observable,oldValue,newValue)->{
                    filteredList.setPredicate(u->{
                        if(newValue.isEmpty()|| newValue==null){
                            return true;
                        }
                        if(u.getEmail().toLowerCase().contains(newValue.toLowerCase())){
                            return true;
                        } else if (u.getUsername().toLowerCase().contains(newValue.toLowerCase())) {
                            return true;
                        } else if (u.getEmail().toLowerCase().contains(newValue.toLowerCase())) {
                            return true;

                        }
                        else{
                            return false;
                        }
                    });
                    userTable.getItems().setAll(filteredList);
                }
        );
    }
    @FXML
    void PDF(MouseEvent event) {
    }
    @FXML
    void banUser(ActionEvent event) throws SQLException {
        User U = userTable.getSelectionModel().getSelectedItem();
        if (U.getAccess() == 1) {
            U.setAccess(0);
            us.add(U);
            showAlert("User Unbanned!");
            userTable.refresh();
        }
        if (U.getAccess() == 0) {
            U.setAccess(1);
            us.add(U);
            showAlert("User Banned!");
            userTable.refresh();
        }
    }
    @FXML
    void maillingPage(MouseEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Mailling.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void ShopPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Asma/afficherProduit_client.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void eventsPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Lina/afficherEvent.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void CompPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Nour/CompetitionGUI.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}