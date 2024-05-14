package controllers;
import models.CategorieProd;
import models.Tags;
import services.TagsService;
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
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;

public class listTags {
    @FXML
    private TextField tftheme;

    @FXML
    private TextField tfgenre;

    @FXML
    private ListView<Tags> listtag;
    ObservableList<Tags> data= FXCollections.observableArrayList();
    TagsService sp=new TagsService();
    @FXML
    public void initialize(){

        refresh();
    }
    public void refresh(){
        data.clear();
        data=FXCollections.observableArrayList(sp.getAll());
        listtag.setItems(data);
    }
    public String controleDeSaisire(){
        String erreur="";
        if(tftheme.getText().isEmpty()){
            erreur+="-theme vide\n";
        }

        if(tfgenre.getText().isEmpty()){
            erreur+="-genre vide\n";
        }


        return erreur;
    }
    @FXML
    void ajouterTag(ActionEvent event) {
        //ps.add(new Personne(0,nomtf.getText(),prenomTF.getText(),Integer.parseInt(ageTF.getText())));
        String erreur=controleDeSaisire();
        if(erreur.length()>0){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ouups Invalide Form!");
            alert.setContentText(erreur);
            alert.showAndWait();
        }
        else {
            Tags p=new Tags();
            p.setTheme(tftheme.getText());
            p.setGenre(tfgenre.getText());
            sp.add(p);
            refresh();
            Notifications.create().title("Add tag").text("tag added successfully").hideAfter(Duration.seconds(5)).showInformation();
        }

    }
    @FXML
    void fillforum(MouseEvent event) {
        Tags p=listtag.getSelectionModel().getSelectedItem();
        if(p!=null){
            tftheme.setText(p.getTheme());
            tfgenre.setText(p.getGenre());
        }
    }
    @FXML
    void modifierTag(ActionEvent event) {
        String erreur=controleDeSaisire();
        if(erreur.length()>0){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ouups Invalide Form!");
            alert.setContentText(erreur);
            alert.showAndWait();
        }
        else {
            Tags p=listtag.getSelectionModel().getSelectedItem();
            p.setTheme(tftheme.getText());
            p.setGenre(tfgenre.getText());
            sp.update(p);
            refresh();
        }

    }
    @FXML
    void supprimerTag(ActionEvent event) {
        Tags p=listtag.getSelectionModel().getSelectedItem();
        if(p!=null){
            sp.delete(p.getIdT());
            refresh();
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