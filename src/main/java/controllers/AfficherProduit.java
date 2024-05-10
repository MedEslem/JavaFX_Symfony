package controllers;
import models.Product;
import services.CategoriepService;
import services.ProductService;
import services.TagsService;
import services.OnChangeListener;
import ui.MainFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
import java.util.Set;


public class AfficherProduit implements OnChangeListener {
    @FXML
    private ComboBox<String> cbcategorie;

    @FXML
    private ComboBox<String> cbtag;

    @FXML
    private TextField tfnameP;

    @FXML
    private TextField tfdescP;

    @FXML
    private TextField tfimgP;

    @FXML
    private TextField tfprixP;
    @FXML
    private AnchorPane anchorpane;



    @FXML
    private GridPane grid;
    @FXML
    private ComboBox<String> cbtri;
    @FXML
    private TextField tfrecherche;

    int idModifier;
    ObservableList<Product> data= FXCollections.observableArrayList();
    ProductService sp=new ProductService();
    CategoriepService sm=new CategoriepService();
    TagsService st=new TagsService();
    @FXML
    public void initialize(){

        cbcategorie.getItems().setAll(sm.getNomCategorie());
        cbtag.getItems().setAll(st.getNomTag());
        tfimgP.setDisable(true);

        refresh();
    }
    public void refresh(){
        grid.getChildren().clear();//bch nfasa5 ili f wost l matrice lkol
        Set<Product> products=sp.getAll();
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
                item.setOnChangeListener(this);

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
    public String controleDeSaisire(){
        String erreur="";
        if(tfnameP.getText().isEmpty()){
            erreur+="-Nom vide\n";
        }

        if(tfdescP.getText().isEmpty()){
            erreur+="-image vide\n";
        }
        if(cbcategorie.getValue()==null){
            erreur+="-categorie vide\n";
        }
        if(cbtag.getValue()==null){
            erreur+="-tags vide\n";
        }


        return erreur;
    }
    @FXML
    void ajouterProduit(ActionEvent event) {
        //ps.add(new Personne(0,nomtf.getText(),prenomTF.getText(),Integer.parseInt(ageTF.getText())));
        String erreur=controleDeSaisire();
        if(erreur.length()>0){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ouups Invalide Form!");
            alert.setContentText(erreur);
            alert.showAndWait();
        }
        else {
            Product p=new Product();
            p.setNomP(tfnameP.getText());
            p.setDescriptionP(tfdescP.getText());
            p.setImageP(tfimgP.getText());
            p.setPrixP(  Float.parseFloat(tfprixP.getText()));;
            int idCategorie= sm.getIdByName(String.valueOf(cbcategorie.getValue()));
            p.setIdCategorie(idCategorie);
            int idTag= st.getIdByName(String.valueOf(cbtag.getValue()));
            p.setIdTag(idTag);


            sp.add(p);
            refresh();
            Notifications.create().title("add product").text("product added successfully").hideAfter(Duration.seconds(5)).showInformation();
        }

    }

    @FXML
    void modifierProduit(ActionEvent event) {

        String erreur=controleDeSaisire();
        if(erreur.length()>0){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ouups Invalide Form!");
            alert.setContentText(erreur);
            alert.showAndWait();
        }
        else {
            Product p=new Product();
            p.setIdP(idModifier);
            p.setNomP(tfnameP.getText());
            p.setDescriptionP(tfdescP.getText());
            p.setImageP(tfimgP.getText());
            p.setPrixP(  Float.parseFloat(tfprixP.getText()));
            int idCategorie= sm.getIdByName(String.valueOf(cbcategorie.getValue()));
            p.setIdCategorie(idCategorie);
            int idTag= st.getIdByName(String.valueOf(cbtag.getValue()));
            p.setIdTag(idTag);


            sp.update(p);
            refresh();

        }


    }

    @FXML
    void uploadimage(ActionEvent event) {
        FileChooser fileChooser=new FileChooser();
        File file=fileChooser.showOpenDialog(tfimgP.getScene().getWindow());
        if(file!=null){
            String filename= file.getName();
            tfimgP.setText(filename);
        }
    }

    @FXML
    void afficherProduits(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Asma/afficherProduit_client.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onSupprimerClicked() {
        refresh();
    }
    @Override
    public void onModdifierClicked(Product c) {
        idModifier=c.getIdP();
        tfnameP.setText(c.getNomP());
        tfdescP.setText(c.getDescriptionP());
        tfprixP.setText(String.valueOf(c.getPrixP()));
        tfimgP.setText(c.getImageP());
        cbcategorie.setValue(sm.getById(c.getIdP()).getNomC());
        cbtag.setValue(st.getById(c.getIdP()).getTheme());


    }


    public void back(MouseEvent mouseEvent) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Asma/afficherProduit_client.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) mouseEvent.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}