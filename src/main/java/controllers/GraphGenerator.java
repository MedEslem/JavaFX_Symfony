package controllers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Competition;
import models.Equipe;
import services.CompetitionService;
import services.EquipeService;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GraphGenerator {

    @FXML
    private AnchorPane home_form;

    @FXML
    private BarChart<String, Number> teamStatsChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private javafx.scene.control.Button returnbutton;


    private final EquipeService equipeService = new EquipeService();
    private final CompetitionService competitionService = new CompetitionService();

    @FXML
    public void initialize() {
        // Appeler la méthode pour afficher les statistiques des équipes lors de l'initialisation du contrôleur
        teamStatsChartData();

        // Définir les données de l'axe X (categories)
        xAxis.setLabel("Nom de l'équipe");

        // Définir les données de l'axe Y (valeurs numériques)
        yAxis.setLabel("Nombre de participants");
    }



    public void teamStatsChartData() {
        if (teamStatsChart == null) {
            System.out.println("Le BarChart est null. Impossible d'afficher les statistiques.");
            return;
        }

        Map<String, Integer> participantCounts = new HashMap<>();

        // Récupérer les équipes actuelles depuis votre TableView ou un autre composant
        ObservableList<Equipe> equipeList = equipeService.getAllEquipesObservableFromDB();

        // Calculer le nombre total de participants par équipe
        for (Equipe equipe : equipeList) {
            String nomEquipe = equipe.getNom_e();
            int nbParticipants = equipe.getNb_p();
            participantCounts.put(nomEquipe, nbParticipants);
        }

        // Configurer les données du graphique à barres
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : participantCounts.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        if (teamStatsChart.getData() != null) {
            teamStatsChart.getData().clear();
        }

        teamStatsChart.getData().add(series);
    }



    @FXML
    void returnbuttonOnClick(ActionEvent event) {
        try {
            // Charger la vue de l'équipe depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Nour/CompetitionGUI.fxml"));
            Parent root = loader.load();

            // Obtenez une référence au contrôleur de l'équipe pour toute interaction ultérieure si nécessaire
            CompetitionController competitionController = loader.getController();

            // Afficher la scène de l'équipe
            Stage stage = (Stage) returnbutton.getScene().getWindow(); // Obtenez la fenêtre actuelle
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Gérer l'exception de chargement de la vue
        }

    }
}