package controllers;

import models.Thread;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import services.ThreadService;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.function.Predicate;

public class listThreads {
    @FXML
    private TableView<Thread> table;
    @FXML
    private TextField rechercheField;
    @FXML
    private TableColumn<Thread, String> titreColumn;
    @FXML
    private TableColumn<Thread, String> createurColumn;
    @FXML
    private TableColumn<Thread, Timestamp> dateCreationColumn;

    private ThreadService threadService = new ThreadService();

    public void initialize() {
        ThreadService threadService = new ThreadService();
        ObservableList<Thread> discussions = null;
        try {
            discussions = FXCollections.observableList(threadService.getAll());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titleThread"));
        createurColumn.setCellValueFactory(cellData -> {
            String creatorName = cellData.getValue().getCreatorThread().getUsername();
            return new SimpleStringProperty(creatorName);
        });

        rechercheField.textProperty().addListener((observable, oldValue, newValue) -> {
            rechercherParTitre();
        });
        dateCreationColumn.setCellValueFactory(new PropertyValueFactory<>("TimeStampCreation"));
        table.setItems(discussions);
        onClick();
    }

    /*
    private void initTableColumns() {
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titleThread"));
        createurColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCreatorThread().getNom()));
        dateCreationColumn.setCellValueFactory(new PropertyValueFactory<>("TimeStampCreation"));
    }

    private void loadThreads() throws SQLException {
        ObservableList<Thread> discussions = FXCollections.observableList(threadService.getAll());
        table.setItems(discussions);
    }
*/
    @FXML
    public void onClick() {
        table.setRowFactory(tv -> {
            TableRow<Thread> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()== MouseButton.PRIMARY
                        && event.getClickCount() == 2) {

                    Thread clickedRow = row.getItem();
                    // Retrieve the ID of the clicked thread
                    int threadId = clickedRow.getIdThread();
                    PostController.thrdId = threadId;

                    changeScene("/Post.fxml");

                }
            });
            return row ;
        });
    }

    @FXML
    public void redirectToAjout() {
        changeScene("/addThread.fxml");
    }

    @FXML
    public void retournerVersAcceuil() {
        //i gotta change thaat to the actual previous page ...
        changeScene("/connected.fxml");
    }

    @FXML
    public void rechercherParTitre() {
        String nomRecherche = rechercheField.getText().trim().toLowerCase();

        if (!nomRecherche.isEmpty()) {
            // Créer un prédicat pour filtrer les espaces dont le nom contient la chaîne de recherche
            Predicate<Thread> nomPredicate = thread -> thread.getTitleThread().toLowerCase().contains(nomRecherche);

            // Créer un FilteredList avec le prédicat
            FilteredList<Thread> filteredList = new FilteredList<>(table.getItems(), nomPredicate);

            // Mettre à jour la liste affichée dans la ListView avec le FilteredList filtré
            table.setItems(filteredList);
        } else {
            // Si le champ de recherche est vide, afficher tous les espaces
            changeScene("/listThreads.fxml");
        }
    }

    private void changeScene(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            table.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
