package controllers;


import models.Thread;
import models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import services.ThreadService;
import utils.DButils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;



public class addThread {

    @FXML
    private TextField titre ;
    @FXML
    private ColorPicker color;

    @FXML
    private TextField description;
    @FXML
    private Label errorMessage;

    @FXML
    private Label errorMessage2;


    User user1 = DButils.connectedUser;


    boolean isTitleValid = true;
    boolean isDescriptionValid = true;



    @FXML
    void addEvent() throws SQLException {
        String title = titre.getText();
        String desc = description.getText();
        Timestamp currentTimestamp = new Timestamp( System.currentTimeMillis());
        Color c = color.getValue();
        String colorAsRgb = String.format("#%02X%02X%02X",
                (int)(c.getRed() * 255),
                (int)(c.getGreen() * 255),
                (int)(c.getBlue() * 255));
        Thread thread = new Thread(title,currentTimestamp,user1,desc,colorAsRgb);
        if (title.isEmpty()) {
            errorMessage.setText("Title field is empty !");
            isTitleValid = false;
        } else if (title.length() > 10) {
            errorMessage.setText("Maximum length of a Title shouldn't be greater than 10 !");
            isTitleValid = false;
        }else{
            errorMessage.setText("");
            isTitleValid = true;
        }
        if (desc.isEmpty()) {
            errorMessage2.setText("Description field is empty !");
            isDescriptionValid = false;
        }else{
            errorMessage2.setText("");
            isDescriptionValid = true;
        }
        if(titreExist(title)){
            errorMessage.setText("The title exists !");
        }
        if(!titreValide(desc)){
            errorMessage2.setText("The description contain inapropriate words !");
        }
        if(!titreValide(title)){
            errorMessage.setText("The title contain inapropriate words !");

        }


        if(titreValide(title) && !titreExist(title) && isTitleValid && isDescriptionValid){
            try {

                ThreadService ts = new ThreadService();
                ts.add(thread);
                sendNotificationEmail(thread);  // Send notification email
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Thread added successfully!");

                alert.showAndWait();
                changeScene();


            }catch(Exception e){
                System.out.println(e.getMessage());
            }

        }
    }

    public void changeScene() {
        try {
            // Chargez le fichier FXML pour la nouvelle scène
            Parent root = FXMLLoader.load(getClass().getResource("/listThreads.fxml"));
            titre.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean titreExist(String titre) throws SQLException {
        ThreadService ts = new ThreadService();
        List<Thread> threads = ts.getAll();
        for(Thread thread:threads){
            if(thread.getTitleThread().equalsIgnoreCase(titre))

                return true ;
        }
        return false;
    }


    public static boolean titreValide(String titre) {
        List<String> motsInterdits = null;
        try {
            motsInterdits = Files.readAllLines(Paths.get("src/main/java/utils/motsinap.txt"));
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier de mots inappropriés");
            System.out.println(e.getMessage());
        }
        if (motsInterdits == null) {
            return true;
        }

        // Convertir le titre en minuscules pour une comparaison insensible à la casse
        String titreMinuscules = titre.toLowerCase();

        // Vérifier si le titre contient un mot interdit
        for (String mot : motsInterdits) {
            if (titreMinuscules.contains(mot)) {
                return false;
            }
        }

        return true;
    }

    private void sendNotificationEmail(Thread thread) {
        try {
            // SMTP server properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "25");

            // Sender's email credentials
            String username = "zoghlami.dhirar.10@gmail.com";
            String password = "badt mwvs cgpd bueg";  // Add your password/ key  here

            javax.mail.Session session = javax.mail.Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // Receiver's email
            String receiverEmail = "zoghlami.dhirar.10@gmail.com";

            if (receiverEmail != null) {
                sendEmail(session, username, receiverEmail, "New thread added: " + thread.getTitleThread());
                System.out.println("Notification email sent successfully.");
            } else {
                System.out.println("Receiver email not found.");
            }

        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    private void sendEmail(javax.mail.Session session, String from, String to, String content) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject("New Thread Notification");
        message.setText(content);
        Transport.send(message);
    }




}
