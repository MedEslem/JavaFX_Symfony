package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Competition;
import models.Equipe;
import services.CompetitionService;
import services.EquipeService;
import javafx.scene.control.ListView;

import javafx.scene.image.Image;

import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class GetEquipe {

    @FXML
    private ListView<Equipe> listView;

    @FXML
    private ComboBox<String> GameTF;

    @FXML
    private TextField idx;

    @FXML
    private TextField LogoTF;

    @FXML
    private TextField TeamTF;

    @FXML
    private TextField NbTF;

    @FXML
    private TextField recherche;

    @FXML
    private Button battle;

    @FXML
    private ImageView logoIMG;
    ObservableList<Equipe> ListEquipe;

    EquipeService equipeService = new EquipeService();
    CompetitionService competitionService = new CompetitionService();

    public void initialize() {
        // Récupérer les données des équipes et les stocker dans ListEquipe
        ListEquipe = equipeService.RecupBase();

        // Définir la cellule personnalisée pour la ListView
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Equipe item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Créer un HBox pour disposer l'image et le texte horizontalement
                    HBox hbox = new HBox(10);

                    // Créer un ImageView pour afficher l'image du logo
                    ImageView imageView = new ImageView();
                    imageView.setFitHeight(50); // Définir la hauteur de l'image
                    imageView.setFitWidth(50); // Définir la largeur de l'image
                    try {
                        // Charger l'image du fichier
                        File file = new File(item.getLogo_e());
                        Image image = new Image(file.toURI().toString());
                        imageView.setImage(image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Créer un TextFlow pour afficher les attributs de l'équipe
                    TextFlow textFlow = new TextFlow();

                    // Créer des Text pour chaque attribut de l'équipe avec leur style de texte personnalisé
                    Text idText = new Text("ID: ");
                    idText.setStyle("-fx-fill: #ff3131; -fx-font-weight: bold;");
                    Text idValue = new Text(String.valueOf(item.getId()));

                    Text nomText = new Text("\nNom: ");
                    nomText.setStyle("-fx-fill: #ff3131; -fx-font-weight: bold;");
                    Text nomValue = new Text(item.getNom_e());

                    Text jeuText = new Text("\nJeu: ");
                    jeuText.setStyle("-fx-fill: #ff3131; -fx-font-weight: bold;");
                    Text jeuValue = new Text(item.getCompetition().getJeu_c());

                    Text nbPlayersText = new Text("\nNombre de joueurs: ");
                    nbPlayersText.setStyle("-fx-fill: #ff3131; -fx-font-weight: bold;");
                    Text nbPlayersValue = new Text(String.valueOf(item.getNb_p()));

                    // Ajouter les Text au TextFlow
                    textFlow.getChildren().addAll(idText, idValue, nomText, nomValue, jeuText, jeuValue, nbPlayersText, nbPlayersValue);

                    // Ajouter l'image et le TextFlow à HBox
                    hbox.getChildren().addAll(imageView, textFlow);

                    // Définir le HBox comme contenu de la cellule
                    setGraphic(hbox);
                }
            }
        });

        // Charger les données dans votre ListView
        listView.setItems(ListEquipe);

        // Appeler la méthode ChercheFichier() pour initialiser la recherche de fichiers
        ChercheFichier();

        // Ajouter un écouteur pour gérer les clics sur les éléments de la ListView
        listView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 0) {
                onEdit();
            }
        });
    }


    @FXML
    private void Ajoutefichier(ActionEvent event) {
        FileChooser f = new FileChooser();
        File S = f.showOpenDialog(null);
        if (S != null) {
            // Here we replace single backslashes with double backslashes
            String correctedPath = S.getAbsolutePath().replace("\\", "\\\\");
            LogoTF.setText(correctedPath);
        }
    }

    public void onEdit() {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            Equipe f = listView.getSelectionModel().getSelectedItem();
            System.out.println(f);
            String id = String.valueOf(f.getId());
            idx.setText(id);
            LogoTF.setText(f.getLogo_e());
            TeamTF.setText(f.getNom_e());
            GameTF.setValue(f.getCompetition().getJeu_c());
            NbTF.setText(String.valueOf(f.getNb_p()));
        }
    }

    @FXML
    void DeleteTeambtn(ActionEvent event) {
        Equipe equipe = listView.getSelectionModel().getSelectedItem();
        if (equipe == null) {
            showAlert("No Selection", "Please select a Team to delete.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Team");
        alert.setContentText("Are you sure you want to delete this Team?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                equipeService.delete(equipe.getId());
                showAlert("Success", "Team deleted successfully.");
                initialize(); // Refresh the list of teams after deletion
                listView.refresh(); // Refresh the table view
            } catch (SQLException e) {
                showAlert("Error", "Failed to delete Team " + e.getMessage());
            }
        }
    }
    public void ChercheFichier() {
        ListEquipe = equipeService.RecupBase();
        listView.setItems(equipeService.RecupBase());
        FilteredList<Equipe> filteredData;
        filteredData = new FilteredList<>(ListEquipe, b -> true);
        recherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((u -> {
                if ((newValue == null) || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return u.getNom_e().toLowerCase().contains(lowerCaseFilter) ||
                        u.getCompetition().getJeu_c().toLowerCase().contains(lowerCaseFilter);
            }));
        });

        SortedList<Equipe> sortedList = new SortedList<>(filteredData);
        sortedList.setComparator((o1, o2) -> {
            // Définir votre logique de comparaison personnalisée ici
            // Par exemple, comparer les noms des équipes
            return o1.getNom_e().compareTo(o2.getNom_e());
        });
        listView.setItems(sortedList);
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        try {
            int id = Integer.valueOf(idx.getText());
            String logo = LogoTF.getText();
            String name = TeamTF.getText();
            int nbPlayers = Integer.parseInt(NbTF.getText());
            Competition competition = competitionService.SelectPartenairebyname(GameTF.getValue());


            if (competition == null) {
                System.out.println("Aucun partenaire trouvé avec le nom: " + GameTF.getValue());
                return;
            }

            Equipe updatedEquipe = new Equipe(id, name, logo, competition, nbPlayers,1);

            equipeService.update(updatedEquipe);

            initialize();
            System.out.println("Equipe mise à jour avec succès");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    void CompPage(MouseEvent event) {
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