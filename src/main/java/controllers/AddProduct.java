package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import models.Mailing;
import models.Product;
import services.CategoriepService;
import services.OnChangeListener;
import services.ProductService;
import services.TagsService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import javafx.scene.control.Button;
import utils.SessionManager;

import javax.mail.MessagingException;

public class AddProduct  {
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
    private ImageView logoIMG;
    @FXML
    private AnchorPane anchorpane;

    ProductService sp=new ProductService();
    CategoriepService sm=new CategoriepService();
    TagsService st=new TagsService();
    @FXML
    void ajouterProduit(ActionEvent event) throws IOException, MessagingException, GeneralSecurityException {
        //ps.add(new Personne(0,nomtf.getText(),prenomTF.getText(),Integer.parseInt(ageTF.getText())));
        String erreur=controleDeSaisire();
        Mailing M = new Mailing();
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

            M.sendMail("Al3ab Gmes","Dear Client,\n" +
                    "\n" +
                    "We are pleased to inform you that your product has been successfully added to our platform, Al3ab Games. We are excited to have your offerings in our catalog, and we believe that they will be a great fit for our customers. You can now view your product listing at any time by visiting your dashboard.\n" +
                    "\n" +
                    "Thank you for choosing to partner with us. We look forward to contributing to your business success and are here to support you every step of the way. Should you have any questions or require further assistance, please do not hesitate to reach out.\n" +
                    "\n" +
                    "Warm regards,", SessionManager.getEmail());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Product has been addes successfully. Your client will shortly receive a confirmation Mail.");

            alert.show();

            // Fermer la notification automatiquement après 3 secondes
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(10), e -> alert.close())
            );
            timeline.play();
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
    void uploadimage(ActionEvent event) {
        FileChooser fileChooser=new FileChooser();
        File file=fileChooser.showOpenDialog(tfimgP.getScene().getWindow());
        if(file!=null){
            String filename= file.getName();
            tfimgP.setText(filename);
        }
    }
    @FXML
    void backProducts(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherProduit_client.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void initialize(){

        cbcategorie.getItems().setAll(sm.getNomCategorie());
        cbtag.getItems().setAll(st.getNomTag());
        tfimgP.setDisable(true);


    }
    @FXML
    void ProductPage(MouseEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Asma/afficherProduit_client.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}