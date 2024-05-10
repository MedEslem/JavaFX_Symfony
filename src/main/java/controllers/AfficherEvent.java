package controllers;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.util.Callback;
import models.Reservation;
import models.Event;
import models.Category;
import services.ReservationpService;
import services.EventService;
import services.CategoryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import utils.DButils;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;


public class AfficherEvent {
    @FXML
    private ComboBox<String> cbcategory;

    @FXML
    private WebView webView;
    @FXML
    private Button CompBTN;
    @FXML
    private Button logoutIN;

    @FXML
    private Button memberBTN;
    @FXML
    private Label revenueLabel;


    @FXML
    private TextField tftitre_event;
    @FXML
    private Button CategoryBTN;

    @FXML
    private Button ReservationBTN;

    @FXML
    private TextField tfdescription_event;
    @FXML
    private TextField tfetat_event;
    @FXML
    private TextField tflieu_event;

    @FXML
    private TextField tfprix_event;
    @FXML
    private TextField tfnbr_personnes;
    @FXML
    private TextField tfdate_event;
    @FXML
    private Button shopBTN;

    @FXML
    private Button userBTN;




    @FXML
    private ListView<Event> listevents;
    ObservableList<Event> data = FXCollections.observableArrayList();
    EventService sp = new EventService();
    ReservationpService sm = new ReservationpService();
    CategoryService st = new CategoryService();

    //maplouay



//maplouay


    //*****************//


    @FXML
    public void initialize() {
        cbcategory.getItems().setAll(st.getNomCategory());
        refresh();
        listevents.setCellFactory(new Callback<ListView<Event>, ListCell<Event>>() {
            @Override
            public ListCell<Event> call(ListView<Event> listView) {
                return new ListCell<Event>() {
                    @Override
                    public void updateItem(Event event, boolean empty) {
                        super.updateItem(event, empty);
                        if (empty || event == null) {
                            setText(null);
                        } else {
                            setText(String.format("Titre event : %s%nDescription event : %s%nEtat event : %s%nLieu event : %s%nPrix event : %.2f%nNombre personnes : %.2f%nDate event : %s%nCategory : %s",
                                    event.getTitre_event(),
                                    event.getDescription_event(),
                                    event.getEtat_event(),
                                    event.getLieu_event(),
                                    event.getPrix_event(),
                                    event.getNbr_personnes(),
                                    event.getDate_event(),
                                    st.getById(event.getCategorie_event_id()).getGenre()));


                            // Appliquer un style rouge uniquement si le texte est "Event Title"
                            if ("Event Title".equals(getText())) {
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
        logoutIN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DButils.changeScene(actionEvent,"login.fxml",null,"Log IN");
            }


        });
    }

    public void refresh() {
        data.clear();
        data = FXCollections.observableArrayList(sp.getAll());
        listevents.setItems(data);
    }

    public String controleDeSaisire() {
        String erreur = "";
        if (tftitre_event.getText().isEmpty()) {
            erreur += "-Add Title\n";
        }

        if (tfdescription_event.getText().isEmpty()) {
            erreur += "-Add Description \n";
        }
        // if(cbcategory.getValue()==null){
        //     erreur+="-Niveau vide\n";
        // }
        // if(cbcategorie.getValue()==null){
        //  erreur+="-Niveau vide\n";
        // }


        return erreur;
    }


    //Ajoutevent//
    @FXML
    void ajouterEvent(ActionEvent event) {
        String erreur = controleDeSaisire();
        if (!erreur.isEmpty()) {
            afficherErreur(erreur);
        } else {
            try {
                Event p = new Event();
                p.setTitre_event(tftitre_event.getText());
                p.setDescription_event(tfdescription_event.getText());
                p.setEtat_event(tfetat_event.getText());
                p.setLieu_event(tflieu_event.getText());
                p.setNbr_personnes(Float.parseFloat(tfnbr_personnes.getText()));
                p.setPrix_event(Float.parseFloat(tfprix_event.getText()));
                p.setDate_event(Date.valueOf(tfdate_event.getText()));
                int categorie_event_id = st.getIdByName(cbcategory.getValue());
                p.setCategorie_event_id(categorie_event_id);
                sp.add(p);
                refresh();
            } catch (NumberFormatException e) {
                afficherErreur("Le prix ou le nombre de personnes doit être un nombre valide.");
            }
        }
    }
    // Méthode pour afficher une boîte de dialogue d'erreur
    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Oups ! Forme invalide");
        alert.setContentText(message);
        alert.showAndWait();
    }
    // endajoutevent////
    @FXML
    void fillforum(MouseEvent event) {
        Event p = listevents.getSelectionModel().getSelectedItem();
        if (p != null) {
            tftitre_event.setText(p.getTitre_event());
            tfdescription_event.setText(p.getDescription_event());
            tfetat_event.setText(p.getEtat_event());
            tflieu_event.setText(p.getLieu_event());
            tfprix_event.setText(String.valueOf(p.getPrix_event()));
            tfnbr_personnes.setText(String.valueOf(p.getNbr_personnes()));
            tfdate_event.setText(String.valueOf(p.getDate_event()));
            String nom = st.getById(p.getCategorie_event_id()).getGenre();
            cbcategory.setValue(nom);
            // Category category = st.getById(p.getId_event());
            //  cbcategory.setValue(category);

        }
    }




    //modification//
    @FXML
    void modifierEvent(ActionEvent event) {
        String erreur=controleDeSaisire();
        if(erreur.length()>0){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ouups Invalide Form!");
            alert.setContentText(erreur);
            alert.showAndWait();
        }
        else {
            Event p=listevents.getSelectionModel().getSelectedItem();
            p.setTitre_event(tftitre_event.getText());
            p.setDescription_event(tfdescription_event.getText());
            p.setEtat_event(tfetat_event.getText());
            p.setLieu_event(tflieu_event.getText());
            p.setNbr_personnes(Float.parseFloat(tfnbr_personnes.getText()));
            p.setPrix_event(Float.parseFloat(tfprix_event.getText()));
            p.setDate_event(Date.valueOf(tfdate_event.getText()));
            int categorie_event_id = st.getIdByName(String.valueOf(cbcategory.getValue()));
            p.setCategorie_event_id(categorie_event_id);





            sp.update(p);
            refresh();
        }

    }
    // end modification//
    @FXML
    void supprimerEvent(ActionEvent event) {
        Event p = listevents.getSelectionModel().getSelectedItem();
        if (p != null) {
            sp.delete(p.getId_event());
            refresh();
        }
    }
    @FXML
    void CategoryPage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Lina/category.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void ReservPage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Lina/reservation.fxml"));
            Parent root = loader.load();
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
    @FXML
    void userPage(ActionEvent event) {
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
    void shopPage(ActionEvent event) {
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
    @FXML
    private void calculerRevenuTotal() {
        List<Event> events = data.stream().collect(Collectors.toList());
        float totalRevenue = 0;
        for (Event event : events) {
            totalRevenue += event.getPrix_event() * event.getNbr_personnes();
        }
        revenueLabel.setText("Events Revenue: " + totalRevenue + " DT");
    }

    @FXML
    private void afficherStatistiquesPrix() {
        List<Event> events = data.stream().collect(Collectors.toList());

        if (events.isEmpty()) {
            afficherAvertissement("Aucun événement trouvé pour afficher les statistiques de prix.");
            return;
        }

        float totalPrix = 0;
        float prixMinimum = Float.MAX_VALUE;
        float prixMaximum = Float.MIN_VALUE;

        for (Event event : events) {
            float prix = event.getPrix_event();
            totalPrix += prix;
            if (prix < prixMinimum) {
                prixMinimum = prix;
            }
            if (prix > prixMaximum) {
                prixMaximum = prix;
            }
        }

        float prixMoyen = totalPrix / events.size();

        String statistiques = String.format("-Total Events Prices: %.2f DT\n", totalPrix);
        statistiques += String.format("                   **********          \n-Average Events price: %.2f DT\n",     prixMoyen);
        statistiques += String.format("                   **********          \n-Minimum Events price: %.2f DT\n",     prixMinimum);
        statistiques += String.format("                   **********          \n-Maximum Events price: %.2f DT\n",     prixMaximum);

        afficherMessage(statistiques);
    }

    private void afficherAvertissement(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Avertissement");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Events' Prices");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}