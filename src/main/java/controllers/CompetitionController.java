package controllers;
import java.awt.*;
import java.sql.*;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import models.Competition;
import services.CompetitionService;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;

import javafx.scene.control.TextField;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import utils.DButils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.*;
import java.util.Comparator;

import java.util.Optional;


import java.io.InputStream;
import java.util.Base64;


public class CompetitionController {

    @FXML
    private DatePicker date;
    @FXML
    private Button UserBTN;

    @FXML
    private TextField descripTF;

    @FXML
    private TextField nqameTF;
    @FXML
    private Button ShopBTN;
    @FXML
    private Button memberBTN;
    @FXML
    private Button logoutIN;


    @FXML
    private ListView<Competition> outputbtn;



    @FXML
    private TextField timeTF;
    @FXML
    private TextField recherche;

    @FXML
    private ComboBox<String> type;




    @FXML
    private javafx.scene.control.Button generatePdfButton;


    ObservableList<Competition> ListCompetition;



    @FXML
    private javafx.scene.control.Button GoToTeam;

    @FXML
    private javafx.scene.control.Button home1;

    @FXML
    private javafx.scene.control.Button showGraphButton;
    @FXML
    private Button teamsBTN;



    CompetitionService competitionService = new CompetitionService();


    private ObservableList<Competition> competitionData = FXCollections.observableArrayList();

    public void initialize() {
        // Récupérer les données de la compétition et les stocker dans competitionData

        competitionData = competitionService.RecupBase();

        outputbtn.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Competition item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    TextFlow textFlow = new TextFlow();

                    Text idText = new Text("ID: ");
                    idText.setStyle("-fx-fill: #ff3131; -fx-font-weight: bold;");
                    Text idValue = new Text(String.valueOf(item.getId()));

                    Text descriptionText = new Text("\nDescription: ");
                    descriptionText.setStyle("-fx-fill: #ff3131; -fx-font-weight: bold;");
                    Text descriptionValue = new Text(item.getDescription_c());

                    Text jeuText = new Text("\nJeu: ");
                    jeuText.setStyle("-fx-fill: #ff3131; -fx-font-weight: bold;");
                    Text jeuValue = new Text(item.getJeu_c());

                    Text typeText = new Text("\nType: ");
                    typeText.setStyle("-fx-fill: #ff3131; -fx-font-weight: bold;");
                    Text typeValue = new Text(item.getType_c());


                    Text dateText = new Text("\nDate: ");
                    dateText.setStyle("-fx-fill: #ff3131; -fx-font-weight: bold;");
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                    Text dateValue = new Text(dateFormatter.format(item.getDate_c()));

                    Text heureText = new Text("\nHeure: ");
                    heureText.setStyle("-fx-fill: #ff3131; -fx-font-weight: bold;");
                    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
                    Text heureValue = new Text(timeFormatter.format(item.getHeure_c()));

                    textFlow.getChildren().addAll(idText, idValue, descriptionText, descriptionValue, jeuText, jeuValue, typeText, typeValue, dateText, dateValue, heureText, heureValue);

                    setGraphic(textFlow);
                }
                logoutIN.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        DButils.changeScene(actionEvent,"login.fxml",null,"Log IN");
                    }
                });

            }
        });

        // Charger les données dans votre ListView
        outputbtn.setItems(competitionData);

        table();
        ChercheFichier();
        outputbtn.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 0) {
                onEdit();
            }
        });
    }


    private void table() {
        outputbtn.setItems(competitionService.RecupBase());
        Comparator<Competition> comparator = Comparator.comparing(Competition::getDescription_c);
        SortedList<Competition> sortedList = new SortedList<>(outputbtn.getItems(), comparator);
        outputbtn.setItems(sortedList);
    }

    public void ChercheFichier() {
        FilteredList<Competition> filtreddata = new FilteredList<>(competitionData, b -> true);
        recherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filtreddata.setPredicate((u -> {
                if ((newValue == null) || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return u.getDescription_c().toLowerCase().contains(lowerCaseFilter)
                        || u.getType_c().toLowerCase().contains(lowerCaseFilter)
                        || u.getJeu_c().toLowerCase().contains(lowerCaseFilter)
                        || u.getDate_c().toString().toLowerCase().contains(lowerCaseFilter) // Recherche par date
                        || u.getHeure_c().toString().toLowerCase().contains(lowerCaseFilter); // Recherche par heure
            }));
        });

        // Mettre à jour la ListView avec la liste filtrée
        Comparator<Competition> comparator = Comparator.comparing(Competition::getDescription_c);
        SortedList<Competition> srt = new SortedList<>(filtreddata, comparator);
        outputbtn.setItems(srt);
    }


    public void onEdit() {
        if (outputbtn.getSelectionModel().getSelectedItem() != null) {
            Competition selectedCompetition = outputbtn.getSelectionModel().getSelectedItem();
            descripTF.setText(selectedCompetition.getDescription_c());
            type.setValue(selectedCompetition.getType_c());
            nqameTF.setText(selectedCompetition.getJeu_c());

            // Vérification de la valeur de la date
            if (date.getValue() != null) {
                // Conversion de la date en LocalDate
                LocalDate localDate = date.getValue().atStartOfDay(ZoneId.systemDefault()).toLocalDate();
                date.setValue(localDate);
            }

            // Vérification de la valeur de l'heure
            if (selectedCompetition.getHeure_c() != null) {
                Time time = selectedCompetition.getHeure_c();
                LocalTime localTime = time.toLocalTime();
                timeTF.setText(localTime.toString());
            }
        }
    }

    @FXML
    void addcompetition(ActionEvent event) {
        if (!isValidCompetitionInput()) {
            showAlert("Missing Information", "Please fill in all fields.");
            return;
        }

        Competition competition = createCompetitionFromInput();
        try {
            competitionService.add(competition);
            showAlert("Success", "Competition added successfully.");
            table();
        } catch (SQLException e) {
            showAlert("Error", "Failed to add competition: " + e.getMessage());
        }
    }

    @FXML
    void deletecompetition(ActionEvent event) {
        Competition competition = outputbtn.getSelectionModel().getSelectedItem();
        if (competition == null) {
            showAlert("No Selection", "Please select a competition to delete.");
            return;
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Competition");
        alert.setContentText("Are you sure you want to delete this competition?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                competitionService.delete(competition.getId());
                showAlert("Success", "Competition deleted successfully.");
                table();
            } catch (SQLException e) {
                showAlert("Error", "Failed to delete competition: " + e.getMessage());
            }
        }
    }

    @FXML
    void updatecompetition(ActionEvent event) {
        Competition selectedCompetition = outputbtn.getSelectionModel().getSelectedItem();
        if (selectedCompetition == null) {
            showAlert("No Selection", "Please select a competition to update.");
            return;
        }
        if (!isValidCompetitionInput()) {
            showAlert("Missing Information", "Please fill in all fields.");
            return;
        }
        Competition updatedCompetition = createCompetitionFromInput();
        updatedCompetition.setId(selectedCompetition.getId());
        try {
            competitionService.update(updatedCompetition);
            showAlert("Success", "Competition updated successfully.");
            table();
        } catch (SQLException e) {
            showAlert("Error", "Failed to update competition: " + e.getMessage());
        }
    }






    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean isValidCompetitionInput() {
        return descripTF.getText() != null && !descripTF.getText().isEmpty() &&
                type.getValue() != null && !type.getValue().isEmpty() &&
                nqameTF.getText() != null && !nqameTF.getText().isEmpty() &&

                date.getValue() != null && timeTF.getText() != null && !timeTF.getText().isEmpty();
    }

    private Competition createCompetitionFromInput() {
        String description = descripTF.getText();
        String typeValue = type.getValue();
        String gameName = nqameTF.getText();
        LocalDate localDate = date.getValue();
        java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);

        // Vérifier si l'heure est dans le format "HH:mm:ss"
        String timeString = timeTF.getText();
        if (!isValidTimeFormat(timeString)) {
            showAlert("Invalid Time", "Time must be in the format HH:mm:ss");
            return null; // ou gérer ce cas comme vous le souhaitez
        }
        java.util.Date utilDate = new java.util.Date(sqlDate.getTime());
        // Convertir la chaîne de temps en objet Time
        Time time = Time.valueOf(timeString + ":00"); // Ajouter les secondes si elles ne sont pas incluses



        // Créer et retourner une nouvelle instance de Competition
        return new Competition(description, typeValue, gameName, utilDate, time);
    }


    private boolean isValidTimeFormat(String time) {
        return time.matches("^(?:[01]\\d|2[0-3]):[0-5]\\d(:[0-5]\\d)?$");
    }
    @FXML
    public void generatePDFReport(ActionEvent actionEvent) {
        CompetitionService competitionService1 = new CompetitionService();
        try {
            List<Competition> competition = competitionService1.getAll();
            String htmlContent = convertCompetitionListToHtml(competition);
            String apiEndpoint = "https://pdf-api.co/pdf";
            String apiKey = "007AAFF6403C0D554DE694FA59030F057C94";
            String requestBody = String.format("{\"apiKey\": \"%s\", \"html\": \"%s\"}", apiKey, htmlContent);
            HttpResponse<byte[]> response = Unirest.post(apiEndpoint)
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .asBytes();

            if (response.getStatus() == 200 && "application/pdf".equals(response.getHeaders().getFirst("Content-Type"))) {
                Path path = Paths.get("ProgramReport.pdf");
                try (OutputStream outputStream = Files.newOutputStream(path)) {
                    outputStream.write(response.getBody());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.err.println("Failed to write PDF file. Error: " + ex.getMessage());
                    return;
                }
                System.out.println("PDF Generated at: " + path.toAbsolutePath());

                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(path.toFile());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        System.err.println("Unable to open the PDF file. Error: " + ex.getMessage());
                    }
                } else {
                    System.err.println("Desktop operations not supported on the current platform. Cannot open the PDF file automatically.");
                }
            } else {
                System.err.println("Failed to generate PDF: " + response.getStatusText());
                System.err.println("Status Code: " + response.getStatus());
                System.err.println("Response Headers: " + response.getHeaders());
                String responseBody = new String(response.getBody(), StandardCharsets.UTF_8);
                System.err.println("Response Body: " + responseBody);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to retrieve competition data from the database. Error: " + e.getMessage());
        }
    }

    private String convertCompetitionListToHtml(List<Competition> competition) {
        StringBuilder htmlBuilder = new StringBuilder();

        // Begin HTML
        htmlBuilder.append("<html><body>");
        // Load image from resources and convert it to Base64
        String imagePath = "/Nour/pdf.jpg";
        String imageBase64 = "";
        try (InputStream inputStream = getClass().getResourceAsStream(imagePath)) {
            byte[] imageBytes = inputStream.readAllBytes();
            imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load image: " + e.getMessage());
        }

        // Add image to HTML
        htmlBuilder.append("<div style='position: relative; text-align: center;'>");
        htmlBuilder.append("<img src='data:image/png;base64,").append(imageBase64).append("' alt='PDF Image' style='max-width: 100%; height: auto;'>");
        htmlBuilder.append("<div style='position: absolute; top: 10%; left: 50%; transform: translateX(-50%); color: red; font-size: 27px;'>Competition Report</div>");

        // Add table
        htmlBuilder.append("<table style='position: absolute; top: 24%; left: 50%; transform: translate(-50%, -50%); border-collapse: collapse; width: 80%; background-color: rgba(255, 255, 255, 0.9);'>");
        htmlBuilder.append("<tr><th style='padding: 10px; background-color: #f2f2f2;'>Games</th><th style='padding: 10px; background-color: #f2f2f2;'>Type</th><th style='padding: 10px; background-color: #f2f2f2;'>Date</th><th style='padding: 10px; background-color: #f2f2f2;'>Heure</th></tr>");

        // Add rows for each Programme
        for (Competition com : competition) {
            htmlBuilder.append("<tr>");

            htmlBuilder.append("<td style='padding: 10px; border: 1px solid #ddd;'>").append(com.getJeu_c()).append("</td>");
            htmlBuilder.append("<td style='padding: 10px; border: 1px solid #ddd;'>").append(com.getType_c()).append("</td>");
            htmlBuilder.append("<td style='padding: 10px; border: 1px solid #ddd;'>").append(com.getDate_c()).append("</td>");
            htmlBuilder.append("<td style='padding: 10px; border: 1px solid #ddd;'>").append(com.getHeure_c()).append("</td>");
            htmlBuilder.append("</tr>");
        }

        // End HTML
        htmlBuilder.append("</table></div></body></html>");

        return htmlBuilder.toString();
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
    void ShopPage(ActionEvent event) {
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
    void eventsPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Lina/afficherEvent.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void showGameGraph(ActionEvent event) {
        try {
            // Charger la vue de l'équipe depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Nour/GraphGenerator.fxml"));
            Parent root = loader.load();

            // Obtenez une référence au contrôleur de l'équipe pour toute interaction ultérieure si nécessaire
            GraphGenerator graphGenerator = loader.getController();

            // Afficher la scène de l'équipe
            Stage stage = (Stage) showGraphButton.getScene().getWindow(); // Obtenez la fenêtre actuelle
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Gérer l'exception de chargement de la vue
        }

    }
    @FXML
    void teamsPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Nour/GetEquipe.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void UserPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserTable.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Le reste de votre code...
}