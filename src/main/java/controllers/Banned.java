package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import models.Mailing;
import utils.DButils;
import utils.SessionManager;
import javafx.fxml.Initializable;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ResourceBundle;

public class Banned implements Initializable {

    @FXML
    private Text HelloTxt;

    @FXML
    private Button HomePageBTN;

    @FXML
    private Text UnbanTXT;

    @FXML
    void SendUban(MouseEvent event) throws MessagingException, GeneralSecurityException, IOException {
        Mailing Mail = new Mailing();
        Mail.sendMail("Unban Request","Hello My UserName is"+" " +SessionManager.getUsername()+"I understand that my account was banned ,and I apologize for any violation of your terms of service that may have led to this action.","mohamedeslem.somrani@gmail.com");
    }


    public void set_info_connected(){
        HelloTxt.setText("Hello"+" "+SessionManager.getUsername());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HomePageBTN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DButils.changeScene(actionEvent,"login.fxml",null,"Log IN");
            }
        });
    }
}