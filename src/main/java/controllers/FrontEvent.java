package controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.util.Callback;
import models.*;
import services.ReservationpService;
import services.EventService;
import services.CategoryService;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Category;
import services.ReservationpService;
import services.EventService;
import services.CategoryService;
import utils.DButils;
import utils.SessionManager;


import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
public class FrontEvent {
    @FXML
    private Button HomeBTN;

    @FXML
    private Button adminBTN;

    @FXML
    private Button compBTN;

    @FXML
    private Label connectedTxt;


    @FXML
    private Button forum;
    @FXML
    private ListView<Event> listevents;
    @FXML
    private Button logoutIN;

    @FXML
    private Button profileBTN;

    @FXML
    private Button reservationBTN;

    @FXML
    private Button shopBTN;
    private User U = new User();

    EventService sp = new EventService();

    ObservableList<Event> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        refresh();
        logoutIN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DButils.changeScene(actionEvent, "login.fxml", null, "Log IN");
            }
        });

        // Définir une cell factory personnalisée pour la ListView
        listevents.setCellFactory(new Callback<ListView<Event>, ListCell<Event>>() {
            @Override
            public ListCell<Event> call(ListView<Event> listView) {
                return new ListCell<Event>() {
                    @Override
                    protected void updateItem(Event event, boolean empty) {
                        super.updateItem(event, empty);
                        if (empty || event == null) {
                            setText(null);
                        } else {
                            // Personnalisez l'affichage de la cellule avec les détails de l'événement
                            setText("Titre event: " + event.getTitre_event() + "\n"
                                    + "Description event: " + event.getDescription_event() + "\n"
                                    + "State: " + event.getEtat_event() + "\n"
                                    + "Location: " + event.getLieu_event() + "\n"
                                    + "Event Price: " + event.getPrix_event() + "\n"
                                    + "Number of attendees: " + event.getNbr_personnes() + "\n"
                                    + "Date: " + event.getDate_event() + "\n"
                                    + "Category: " + event.getCategorie_event_id());
                        }
                    }
                };
            }
        });
    }
    public void refresh() {
        data.clear();
        data = FXCollections.observableArrayList(sp.getAll());
        listevents.setItems(data);
    }
    @FXML
    void AddReservationPage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Lina/AddReservation.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    void ProfilePage(ActionEvent event) {
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
    void ShopPage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Asma/Shop.fxml"));
            Parent root = loader.load();
            Shop P = loader.getController();
            P.set_info_connected();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void adminPage(ActionEvent event) {
        if (U.buildRoleStringFromRoles(SessionManager.getRoles()).equals("Admin")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserTable.fxml"));
                Parent root = loader.load();
                Scene currentScene = ((Node) event.getSource()).getScene();

                currentScene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("You Must Be An Admin");
        }

    }

    @FXML
    private void allerVersBtnMessagerie() {
        changeScene2("/listThreads.fxml");
    }

    private void changeScene2(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            forum.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void homePage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/connected.fxml"));
            Parent root = loader.load();
            Connected P = loader.getController();
            P.set_info_connected();

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
    public void set_info_connected(){
        connectedTxt.setText(SessionManager.getUsername());

    }
    @FXML
    void CompPage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Nour/EquipeGUI.fxml"));
            Parent root = loader.load();
            EquipeController E = loader.getController();
            E.set_info_connected();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
