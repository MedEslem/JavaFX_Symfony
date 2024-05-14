package controllers;

import models.Thread;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import services.ThreadService;

import java.io.IOException;
import java.sql.SQLException;

public class modifyTitle {
    @FXML
    private TextField titreMod;
    @FXML
    private TextField descriptionMod;
    @FXML
    private Label error;
    @FXML
    private ColorPicker color;
    @FXML
    private Label error2;

    private boolean isTitreValid = true;
    private boolean isDescriptionValid = true;

    private ThreadService threadService = new ThreadService();

    public void initialize() {
        try {
            Thread thread = threadService.getById(PostController.thrdId); //i did test it manually before ...
            titreMod.setText(thread.getTitleThread());
            descriptionMod.setText(thread.getDescriptionThread());
            color.setValue(Color.web(thread.getColorThread()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void modifierTitre(ActionEvent event) {
        String titre = titreMod.getText();
        String description = descriptionMod.getText();

        //int id = 3; //MessageController.discuId;
        int thrdId = PostController.thrdId;

        Color c = color.getValue();
        String colorAsRgb = String.format("#%02X%02X%02X",
                (int)(c.getRed() * 255),
                (int)(c.getGreen() * 255),
                (int)(c.getBlue() * 255));

        if (titre.isEmpty()) {
            error.setText("Title field is empty !");
            isTitreValid = false;
        } else if (titre.length() > 10) {
            error.setText("Maximum length of a Title shouldn't be greater than 10 !");
            isTitreValid = false;
        } else {
            error.setText("");
            isTitreValid = true;
        }

        if (description.isEmpty()) {
            error2.setText("Description field is empty !");
            isDescriptionValid = false;
        } else {
            error2.setText("");
            isDescriptionValid = true;
        }

        try {
            if(addThread.titreExist(titre)){
                error.setText("Title exists !");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(!addThread.titreValide(description)){
            error2.setText("The description contain inapropriate words !");
        }
        if(!addThread.titreValide(titre)){
            error.setText("The title contain inapropriate words !");

        }


        try {
            if (addThread.titreValide(titre) && !addThread.titreExist(titre) && isTitreValid && isDescriptionValid) {
                Thread thread = new Thread(thrdId, titre, description, colorAsRgb);
                try {
                    threadService.update(thread);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Thread has been successfully modified !");
                    changeScene("/listThreads.fxml");
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                    changeScene("/listThreads.fxml");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void changeScene(String s) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(s));
            titreMod.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void retour() {
        changeScene("/Post.fxml");
    }
}
