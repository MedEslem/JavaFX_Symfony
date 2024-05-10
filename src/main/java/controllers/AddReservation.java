package controllers;

import javafx.scene.image.Image;
import models.Event;
import models.Mailing;
import models.QRCodeGenerator;
import models.Reservation;
import services.EventService;
import services.ReservationpService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import utils.SessionManager;
import javafx.scene.image.ImageView;

import javax.mail.MessagingException;

public class AddReservation {




    @FXML
    private TextField tfmode_paiement;
    @FXML
    private TextField tfdate_reservation;

    @FXML
    private ImageView LogoImg;

    @FXML
    private ComboBox<String> cbevent_id;
    @FXML
    public ImageView qrCodeImageView;// Update the variable name to match the ID in the FXML

    @FXML
    private ListView<Reservation> listreservations;

    ObservableList<Reservation> data= FXCollections.observableArrayList();
    ReservationpService sm=new ReservationpService();
    EventService sp=new EventService();

    //maplouay

    //end map louay

    @FXML
    public void initialize(){
        cbevent_id.getItems().setAll(sp.getNomEvent());

        refresh();
    }
    public void refresh() {
        data.clear();
        data.addAll(sm.getAll());
    }

    public String controleDeSaisire(){
        String erreur="";
        if(tfmode_paiement.getText().isEmpty()){
            erreur+="-there is no payment mode\n";
        }

        if(cbevent_id.getValue()==null){
            erreur+="-Choose an Event \n";
        }


        return erreur;
    }
    @FXML
    void ajouterReservation(ActionEvent event) throws MessagingException, GeneralSecurityException, IOException {
        // Vérifier la saisie utilisateur
        String erreur = controleDeSaisire();
        Mailing M = new Mailing();
        if (erreur.length() > 0) {
            // Afficher une alerte en cas de saisie invalide
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ouuups Invalide Form !");
            alert.setContentText(erreur);
            alert.showAndWait();
        } else {
            // Créer une nouvelle réservation
            Reservation p = new Reservation();
            p.setMode_paiement(tfmode_paiement.getText());
            p.setDate_reservation(Date.valueOf(tfdate_reservation.getText()));
            int event_id = sp.getIdByName(String.valueOf(cbevent_id.getValue()));
            p.setEvent_id(event_id);
            p.setUser_id(SessionManager.getId());
            // Ajouter la réservation à la base de données
            sm.add(p);
            // Rafraîchir l'interface utilisateur
            refresh();
            // Envoyer un e-mail de confirmation
            M.sendMail("Confirmation of Your Event Reservation on AL3AB", "Dear Customer,\n" +
                    "\n" +
                    "We're thrilled to confirm your reservation for our upcoming event . \n" +
                    "\n" +
                    "Please make sure to arrive before the event starts to ensure a smooth check-in process. Remember to bring your your QR-Code, it will be scanned at the door.\n" +
                    "If you have any special requirements or requests, feel free to reach out to us.\n" +
                    "\n" +
                    "We look forward to seeing you at the event and hope you have a fantastic time.\n" +
                    "Best regards,\n" +
                    "\n" +
                    "AL3AB Team.", SessionManager.getEmail());



            // Définir les données sélectionnées pour la génération du code QR
            Object selected = p; // Supposons que vous utilisez p comme données de réservation

            // Générer le code QR à partir des données de la transaction
            try {
                byte[] qrCodeData = QRCodeGenerator.generateQRCode(selected.toString(), 200, 200);
                Image qrCodeImage = new Image(new ByteArrayInputStream(qrCodeData));

                // Afficher le code QR dans ImageView ou dans une autre partie de votre interface utilisateur
                qrCodeImageView.setImage(qrCodeImage);
            } catch (Exception e) {
                e.printStackTrace();
                // Gérer les exceptions liées à la génération du code QR
            }
        }




        // Afficher une notification de confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("You reservation has been treated successfully. You will shortly receive a confirmation Mail.");
        alert.show();

        // Fermer la notification automatiquement après 10 secondes
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(10), e -> alert.close())
        );
        timeline.play();
    }
    @FXML
    void ReturnBack(MouseEvent event) {
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




}