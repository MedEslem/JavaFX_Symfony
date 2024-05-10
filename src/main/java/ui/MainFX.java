package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.SmsSender;

public class MainFX extends Application {



    @Override
    public void start(Stage primaryStage)  throws  Exception{
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/listThreads.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);

        primaryStage.setTitle("Al3ab Games");

        primaryStage.setScene(scene);
        primaryStage.show();
        System.out.println(SmsSender.getCode());

    }

    public static void main(String[] args) {
        launch(args);
    }
}