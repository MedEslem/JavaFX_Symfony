package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import models.Product;
import models.User;
import services.ProductService;
import ui.MainFX;
import utils.DButils;
import utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class Shop implements Initializable {

    @FXML
    private Button HomeBTN;
    @FXML
    private GridPane grid;


    @FXML
    private Button adminBTN;

    @FXML
    private Button compBTN;

    @FXML
    private Label connectedTxt;

    @FXML
    private Button eventBTN;

    @FXML
    private Button forum;


    @FXML
    private Button logoutIN;

    @FXML
    private Button profileBTN;

    @FXML
    private Button shopBTN;
    private User U = new User();
    ObservableList<Product> data= FXCollections.observableArrayList();
    ProductService sp=new ProductService();

    @FXML
    void HomePage(ActionEvent event) {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh(sp.getAll());
        logoutIN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DButils.changeScene(actionEvent,"login.fxml",null,"Log IN");
            }
        });

    }
    public void refresh(Set<Product> products){
        grid.getChildren().clear();//bch nfasa5 ili f wost l matrice lkol

        int column = 0;
        int row = 1;
        for (Product c : products) {
            try {
                //  bch n3ayt ll carte w n3abi les information
                //or l carte mawjoud f interface anchorpane
                FXMLLoader card = new FXMLLoader(MainFX.class.getResource("/Asma/CardShop.fxml"));
                AnchorPane anchorPane = card.load();//7atyna l card f interface vu que l grid fiha des interfaces
                CardShop item = card.getController();
                item.remplireData(c);

                if (column == 1) {
                    column = 0;
                    row++;
                }
                grid.add(anchorPane, column++, row);
                GridPane.setMargin(anchorPane, new Insets(10));
            } //psk l fichier ynajm ykoun mouch mwjoud maynajmch ylowdi interface mt3 fich mch mawjoud
            catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    public void set_info_connected(){
        connectedTxt.setText(SessionManager.getUsername());

    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Or use appropriate Alert type (e.g., ERROR)
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void EventsPage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Lina/FrontEvent.fxml"));
            Parent root = loader.load();
            FrontEvent Fe = loader.getController();
            Fe.set_info_connected();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
