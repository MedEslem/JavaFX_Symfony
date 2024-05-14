
package controllers;

import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import models.Product;
import services.OnChangeListener;
import services.ProductService;
import javafx.scene.control.Button;
import ui.MainFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import utils.DButils;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.scene.control.*;

public class AfficherProduit_client implements Initializable {
    @FXML
    private GridPane grid;
    @FXML
    private ComboBox<String> cbcategorie;

    @FXML
    private ComboBox<String> cbtag;
    @FXML
    private Button UserBTN;


    @FXML
    private TextField tfnameP;

    @FXML
    private TextField tfdescP;

    @FXML
    private TextField tfimgP;

    @FXML
    private TextField tfprixP;
    @FXML
    private Button eventsBTN;
    @FXML
    private Button CompBTN;
    @FXML
    private AnchorPane anchorpane;
    @FXML
    private ComboBox<String> cbtri;
    @FXML
    private TextField tfrecherche;
    @FXML
    private PieChart associationDonationChart;
    @FXML
    private Button logoutIN;

    @FXML
    private Button memberBTN;
    @FXML
    private Button CategoryBTN;

    @FXML
    private Button TagsBTN;
    ObservableList<Product> data= FXCollections.observableArrayList();
    ProductService sp=new ProductService();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        refresh(sp.getAll());
        cbtri.getItems().setAll("Name","Description","Price");
        recherche_avance();
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
                FXMLLoader card = new FXMLLoader(MainFX.class.getResource("/Asma/Productcardclient.fxml"));
                AnchorPane anchorPane = card.load();//7atyna l card f interface vu que l grid fiha des interfaces
                Productcardclient item = card.getController();
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

    @FXML
    public void tri(javafx.event.ActionEvent actionEvent) {
        refresh(sp.sortByCritere(cbtri.getValue()));

    }
    public void recherche_avance(){
        refresh(sp.getAll());
        ObservableList<Product> data= FXCollections.observableArrayList(sp.getAll());
        FilteredList<Product> filteredList=new FilteredList<>(data, c->true);
        tfrecherche.textProperty().addListener((observable,oldValue,newValue)->{
            filteredList.setPredicate(c->{
                if(newValue.isEmpty()|| newValue==null){
                    return true;
                }
                if(c.getNomP().contains(newValue)){
                    return true;
                }
                else if(c.getDescriptionP().contains(newValue)){
                    return true;
                }
                else if(String.valueOf(c.getPrixP()).contains(newValue)){
                    return true;
                }
                else{
                    return false;
                }
            });

            refresh(new HashSet<>(filteredList));
        });
    }

    @FXML
    void add(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Asma/addProduct.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void afficherStatistiques(ActionEvent event) {
        String stats = sp.total();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Total Prices");
        alert.setHeaderText("Price summary of all products");
        alert.setContentText(stats);
        alert.showAndWait();
    }
    @FXML
    void genererPDF(ActionEvent event) {
        Set<Product> products = sp.getAll();
        String filePath = "products.pdf"; // Chemin du fichier PDF à créer
        PDFGenerator.generatePDF(products, filePath);
    }
    @FXML
    void stat(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Asma/stat.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void update(ActionEvent mouseEvent) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Asma/afficherProduit.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) mouseEvent.getSource()).getScene();

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
    @FXML
    void UserPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserTable.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void Category(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Asma/categorie.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void TagsPage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Asma/tags.fxml"));
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
