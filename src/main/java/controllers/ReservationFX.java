package controllers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import models.Category;
import models.Event;
import models.Reservation;
import services.EventService;
import services.ReservationpService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.animation.KeyFrame;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReservationFX {


    @FXML
    private TextField tfmode_paiement;
    @FXML
    private TextField tfdate_reservation;
    @FXML
    private TextField tdIdUser;

    @FXML
    private ComboBox<String> cbevent_id; // Update the variable name to match the ID in the FXML
    @FXML
    public ImageView qrCodeImageView;
    @FXML
    private ImageView logoIMG;

    @FXML
    private ListView<Reservation> listreservations;

    ObservableList<Reservation> data= FXCollections.observableArrayList();
    ReservationpService sm=new ReservationpService();
    EventService sp=new EventService();



    @FXML
    public void initialize(){
        cbevent_id.getItems().setAll(sp.getNomEvent());
        refresh();
        listreservations.setCellFactory(new Callback<ListView<Reservation>, ListCell<Reservation>>() {
            @Override
            public ListCell<Reservation> call(ListView<Reservation> listView) {
                return new ListCell<Reservation>() {
                    @Override
                    public void updateItem(Reservation reservation, boolean empty) {
                        super.updateItem(reservation, empty);
                        if (empty || reservation == null) {
                            setText(null);
                        } else {
                            setText(String.format("Payment Mode  : %s%nDate : %s%nEvent :   %s",
                                    reservation.getMode_paiement(),
                                    reservation.getDate_reservation(),
                                    reservation.getEvent_id()

                            ));


                            // Appliquer un style rouge uniquement si le texte est "Event Title"
                            if ("Payment Mode".equals(getText())) {
                                setStyle("-fx-text-fill: red;");
                            } else {
                                setStyle(null); // Retour au style par défaut
                            }

                            // Autres styles CSS que vous souhaitez appliquer
                            // setStyle("-fx-background-color: ...; -fx-font-weight: ...;");
                        }
                    }
                };
            }
        });

    }


    public void refresh(){
        data.clear();
        data=FXCollections.observableArrayList(sm.getAll());
        listreservations.setItems(data);
    }

    public String controleDeSaisire() {
        StringBuilder erreur = new StringBuilder();
        if (tfmode_paiement.getText().isEmpty()) {
            erreur.append("- There is no payment mode.\n");
        }

        if (tfdate_reservation.getText().isEmpty()) {
            erreur.append("- Date field is empty.\n");
        } else {
            try {
                Date.valueOf(tfdate_reservation.getText());
            } catch (IllegalArgumentException e) {
                erreur.append("- Date format is incorrect. Please use YYYY-MM-DD format.\n");
            }
        }

        if (cbevent_id.getValue() == null) {
            erreur.append("- Choose an Event.\n");
        }

        if (tdIdUser.getText().isEmpty()) {
            erreur.append("- User ID field is empty.\n");
        }

        return erreur.toString();
    }

    @FXML
    void fillforum(MouseEvent event) {
        Reservation p = listreservations.getSelectionModel().getSelectedItem();
        if (p != null) {
            tfmode_paiement.setText(p.getMode_paiement());
            tfdate_reservation.setText(String.valueOf(p.getDate_reservation()));
            String nom = sp.getById(p.getEvent_id()).getTitre_event();
            cbevent_id.setValue(nom);
            tdIdUser.setText(String.valueOf(p.getUser_id()));
            // Category category = st.getById(p.getId_event());
            //  cbcategory.setValue(category);

        }
    }
    @FXML
    void modifierReservation(ActionEvent event) {
        String erreur = controleDeSaisire();
        if (!erreur.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Form!");
            alert.setContentText(erreur);
            alert.showAndWait();
        } else {
            Reservation p = listreservations.getSelectionModel().getSelectedItem();
            if (p != null) {
                p.setMode_paiement(tfmode_paiement.getText());
                try {
                    p.setDate_reservation(Date.valueOf(tfdate_reservation.getText()));
                } catch (IllegalArgumentException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Date");
                    alert.setContentText("Please enter a valid date in the format YYYY-MM-DD.");
                    alert.showAndWait();
                    return;
                }
                p.setEvent_id(sp.getIdByName(cbevent_id.getValue()));
                p.setUser_id(Integer.parseInt(tdIdUser.getText()));
                sm.update(p);
                refresh();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Reservation Selected");
                alert.setContentText("Please select a reservation to modify.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void supprimerReservation(ActionEvent event) {
        Reservation p=listreservations.getSelectionModel().getSelectedItem();
        if(p!=null){
            sm.delete(p.getId_reservation());
            refresh();
        }
    }



    @FXML
    void generatePDF(ActionEvent event) {
        Set<Reservation> reservationsSet = sm.getAll(); // Récupérer l'ensemble de réservations
        List<Reservation> reservationsList = new ArrayList<>(reservationsSet); // Convertir l'ensemble en liste
        String filePath = "reservations.pdf"; // Assurez-vous que filePath est une chaîne de caractères
        PDFGeneratorReservation pdfGenerator = new PDFGeneratorReservation(); // Créer une instance de PDFGenerator
        pdfGenerator.generatePDF(filePath, reservationsList); // Appel à la méthode generatePDF sur l'instance de PDFGenerator
    }
    @FXML
    void GoBack(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Lina/afficherEvent.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}