package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Competition;
import models.Equipe;
import models.Mailing;
import models.User;
import services.CompetitionService;
import services.EquipeService;



import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import utils.DButils;
import utils.SessionManager;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import java.awt.image.BufferedImage;
import java.io.*;


import java.io.ByteArrayInputStream;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Button;


public class EquipeController {
    @FXML
    private Button comp;
    @FXML
    private Button teamm;

    @FXML
    private ComboBox<String> GameTF;

    @FXML
    private TextField LogoTF;

    @FXML
    private TextField TeamTF;

    @FXML
    private ImageView qrCodeImageView;

    @FXML
    private TextField NbTF;
    @FXML
    private Label connectedTxt;
    @FXML
    private Button eventBTN;

    @FXML
    private Button forum;

    @FXML
    private Button logoutIN;

    @FXML
    private Button profileBTN;
    @FXML
    private Button shopBTN;
    private User U = new User();


    EquipeService equipeService = new EquipeService();
    CompetitionService competitionService = new CompetitionService();


    private ObservableList<Equipe> EquipeData = FXCollections.observableArrayList();

    @FXML
    void initialize() {


        LogoTF.setVisible(false);


        // Récupérer la liste des jeux
        ObservableList<String> jeux = competitionService.RecupCombo();

        // Initialiser la ComboBox avec la liste des jeux
        GameTF.setItems(jeux);

        // Initialiser la liste des équipes à partir de la base de données
        EquipeData.addAll(equipeService.getAllEquipesObservableFromDB());

        // Initialiser la map pour compter les sélections de jeux
        Map<String, Integer> countMap = new HashMap<>();

        // Compter le nombre de sélections pour chaque jeu
        for (String jeu : jeux) {
            countMap.put(jeu, 0);
        }

        // Parcourir les équipes existantes pour mettre à jour les compteurs
        for (Equipe equipe : EquipeData) {
            String jeuSelectionne = equipe.getCompetition().getJeu_c();
            if (countMap.containsKey(jeuSelectionne)) {
                countMap.put(jeuSelectionne, countMap.get(jeuSelectionne) + 1);
            }
        }

        // Trouver le jeu le plus choisi
        String jeuPlusChoisi = null;
        int maxSelections = 0;
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > maxSelections) {
                maxSelections = entry.getValue();
                jeuPlusChoisi = entry.getKey();
            }
        }

        // Ajouter 'most played' devant le nom du jeu le plus choisi
        if (jeuPlusChoisi != null) {
            String mostPlayedGame = "most played " + jeuPlusChoisi;
            // Ajouter le jeu le plus joué à la liste des jeux
            jeux.add(mostPlayedGame);
            // Mettre à jour la liste des jeux pour afficher le jeu le plus choisi
            GameTF.setItems(jeux);
        }
        logoutIN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DButils.changeScene(actionEvent,"login.fxml",null,"Log IN");
            }
        });


    }
    public void set_info_connected(){
        connectedTxt.setText(SessionManager.getUsername());

    }
    private List<String> retrieveBestGames() {
        // Implémentez la logique pour récupérer les meilleurs jeux
        // à partir de votre modèle de données ou de votre service
        List<String> bestGames = new ArrayList<>();
        // Ajoutez des jeux factices pour le moment

        return bestGames;
    }

    @FXML
    private void Ajoutefichier(ActionEvent event) {
        FileChooser f = new FileChooser();
        File S = f.showOpenDialog(null);
        if(S != null) {
            // Here we replace single backslashes with double backslashes
            String correctedPath = S.getAbsolutePath().replace("\\", "\\\\");
            LogoTF.setText(correctedPath);
        }
    }
    @FXML
    void addEquipebtn(ActionEvent event) throws MessagingException, GeneralSecurityException, IOException {
        Mailing M = new Mailing();
        // Vérifier si tous les champs sont remplis
        if (!isValidEquipeInput()) {
            showAlert("Missing Information", "Please fill in all fields.");
            return;
        }

        // Vérifier si le nombre de participants est valide
        if (!isValidNumberOfParticipants()) {
            showAlert("Invalid Number of Participants", "Please enter a valid number of participants (1 - 6).");
            return;
        }

        Equipe equipe = createEquipeFromInput();
        equipe.setUser_id(SessionManager.getId());


        // Ajoutez l'équipe
        try {
            equipeService.add(equipe);
            showAlert("Success", "Your team has been added successfully.");
            M.sendMail("Joined into Our Competition","Your team has been added successfully.You will get more about it in the future",SessionManager.getEmail());
            EquipeData.add(equipe);
            TeamTF.clear();
            NbTF.clear();
            LogoTF.clear();
            // Générer et afficher le code QR
            afficherQRCode(equipe);
        } catch (SQLException e) {
            showAlert("Error", "Failed to add your team: " + e.getMessage());
        }

        // Ajoutez l'équipe


    }

    private void refreshGamesList() {
        ObservableList<String> jeux = competitionService.RecupCombo();
        GameTF.setItems(jeux);

        // Recherche du jeu le plus joué
        // Mettez à jour la logique ici pour mettre à jour le jeu le plus joué si nécessaire
    }

    // Vérifier si tous les champs sont remplis
    private boolean isValidEquipeInput() {
        return !TeamTF.getText().isEmpty() &&
                !LogoTF.getText().isEmpty() &&
                !NbTF.getText().isEmpty() &&
                GameTF.getValue() != null;
    }

    public void afficherQRCode(Equipe equipe) {
        EquipeService equipeService = new EquipeService();

        try {
            // Générer l'image du code QR
            Image qrCodeImage = equipeService.generateQRCodeImage(equipe, 300, 300);

            // Afficher l'image dans l'ImageView
            qrCodeImageView.setImage(qrCodeImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour convertir BufferedImage en Image JavaFX
    private Image convertToJavaFXImage(BufferedImage bufferedImage) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", out);
            InputStream in = new ByteArrayInputStream(out.toByteArray());
            return new Image(in);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    // Créer un objet Equipe à partir des données saisies par l'utilisateur
    private Equipe createEquipeFromInput() {
        Equipe e = new Equipe();
        e.setNom_e(TeamTF.getText());
        e.setLogo_e(LogoTF.getText()); // Utilisez le chemin d'accès de l'image pour le logo
        e.setNb_p(Integer.parseInt(NbTF.getText())); // Assurez-vous de convertir le texte en entier
        String selectedCompetitionName = GameTF.getValue();
        if (selectedCompetitionName != null) {
            Competition competition = competitionService.SelectPartenairebyname(selectedCompetitionName);
            e.setCompetition(competition);
        }
        return e;
    }


    // Vérifier si le nombre de participants est valide
    private boolean isValidNumberOfParticipants() {
        String nbParticipants = NbTF.getText();
        try {
            int nb = Integer.parseInt(nbParticipants);
            return nb >= 1 && nb <= 6;
        } catch (NumberFormatException e) {
            return false; // La valeur saisie n'est pas un entier
        }
    }
    // Afficher une boîte de dialogue avec un message
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    void adminPage(ActionEvent event) {
        if (U.buildRoleStringFromRoles(SessionManager.getRoles()).equals("Admin")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserTable.fxml"));
                Parent root = loader.load();
                Scene currentScene = ((Node) event.getSource()).getScene();

                currentScene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Admin Space","You Must Be An Admin");
        }

    }

    @FXML
    private void allerVersBtnMessagerie() {
        changeScene2("/listThreads.fxml");
    }

    private void changeScene2(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            forum.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void homePage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/connected.fxml"));
            Parent root = loader.load();
            Connected P = loader.getController();
            P.set_info_connected();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void EventsPage(ActionEvent event) {
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

    @FXML
    void ProfilePage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Parent root = loader.load();
            Profile P = loader.getController();
            P.set_info_connected();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void ShopPage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Asma/Shop.fxml"));
            Parent root = loader.load();
            Shop P = loader.getController();
            P.set_info_connected();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}