package controllers;
import models.CategorieProd;
import models.Tags;
import services.CategoriepService;
import ui.MainFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import javafx.geometry.Insets;

import java.io.IOException;
import java.util.Set;

public class Categorie{

    @FXML
    private TextField tfnomcategorie;

    @FXML
    private TextField tfimageC;


    @FXML
    private ListView<CategorieProd> listcategorie;

    ObservableList<CategorieProd> data= FXCollections.observableArrayList();
    CategoriepService sp=new CategoriepService();
    int idModifier;
    @FXML
    public void initialize(){
        tfimageC.setDisable(true);
        refresh();
    }
    public void refresh(){
        data.clear();
        data=FXCollections.observableArrayList(sp.getAll());
        listcategorie.setItems(data);



    }
    public String controleDeSaisire(){
        String erreur="";
        if(tfnomcategorie.getText().isEmpty()){
            erreur+="-there is no name\n";
        }

        if(tfimageC.getText().isEmpty()){
            erreur+="-there is no image\n";
        }


        return erreur;
    }
    @FXML
    void ajouterCategorie(ActionEvent event) {
        //ps.add(new Personne(0,nomtf.getText(),prenomTF.getText(),Integer.parseInt(ageTF.getText())));
        String erreur=controleDeSaisire();
        if(erreur.length()>0){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ouuups Invalide Form !");
            alert.setContentText(erreur);
            alert.showAndWait();
        }
        else {
            CategorieProd p=new CategorieProd();
            p.setNomC(tfnomcategorie.getText());
            p.setImageC(tfimageC.getText());
            sp.add(p);
            refresh();
            Notifications.create().title("Add category").text("category added successfully").hideAfter(Duration.seconds(5)).showInformation();
        }

    }
    @FXML
    void fillforum(MouseEvent event) {
        CategorieProd p=listcategorie.getSelectionModel().getSelectedItem();
        if(p!=null){
            tfnomcategorie.setText(p.getNomC());
            tfimageC.setText(p.getImageC());
        }
    }
    @FXML
    void modifierCategorie(ActionEvent event) {
        String erreur=controleDeSaisire();
        if(erreur.length()>0){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ouuups Invalide Form!");
            alert.setContentText(erreur);
            alert.showAndWait();
        }
        else {
            CategorieProd p=listcategorie.getSelectionModel().getSelectedItem();
            p.setNomC(tfnomcategorie.getText());
            p.setImageC(tfimageC.getText());
            sp.update(p);
            refresh();
        }

    }

    @FXML
    void supprimerCategorie(ActionEvent event) {
        CategorieProd p=listcategorie.getSelectionModel().getSelectedItem();
        if(p!=null){
            sp.delete(p.getIdC());
            refresh();
        }
    }
    @FXML
    void uploadimage(ActionEvent event) {
        FileChooser fileChooser=new FileChooser();
        File file=fileChooser.showOpenDialog(tfimageC.getScene().getWindow());
        if(file!=null){
            String filename= file.getName();
            tfimageC.setText(filename);
        }
    }
    @FXML
    void adminDashBoard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Asma/afficherProduit_client.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}