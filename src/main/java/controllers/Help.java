package controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import java.io.IOException;
public class Help {
    @FXML
    private ListView<String> faqList;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField userInputField;
    @FXML
    private ImageView logoIMG;



    @FXML
    private void initialize() {

        faqList.getItems().addAll(
                "I got Banned , How to send unban Request?",
                "How to reset my password? ",
                "How to update my personal information?",
                "Where are you located?"
        );

        faqList.setOnMouseClicked(event -> {
            String selectedQuestion = faqList.getSelectionModel().getSelectedItem();
            if (selectedQuestion != null) {
                userInputField.setText(selectedQuestion);
            }
        });
    }

    @FXML
    private void sendMessage() {
        String userInput = userInputField.getText().trim();
        if (!userInput.isEmpty()) {
            appendMessage("You : " + userInput, "#ff0000");
            String botResponse = getChatbotResponse(userInput);
            appendMessage("AL3AB BOT : " + botResponse, "#232b80");
            userInputField.clear();

        }
    }

    private String getChatbotResponse(String userInput) {
        userInput = userInput.toLowerCase();
        if (userInput.contains("i got banned , how to send unban request?")) {
            return "Sorry to hear That :(\n Once you get the Banned Page , Your See 'Send Unban Request' Button , Click on it and it will send an email automatically to the Admin ";
        } else if (userInput.contains("how to reset my password?")) {
            return "In the Login Page , there is a button named 'Forgot Password' type your Phone Number And You will receive SMS to Reset Your Password  ";
        } else if (userInput.contains("how to update my personal information?")) {
            return "In the Profile After Your Connected, There is Button to Update your Personnel Informations";
        } else if (userInput.contains("where are you located?")) {
            return "We are located in Ariana, El Ghazela Esprit";
        } else {
            return "Sorry , I can not understand your question";
        }
    }

    private void appendMessage(String message, String color) {
        chatArea.appendText(message + "\n");
        chatArea.setStyle(chatArea.getStyle() + "-fx-text-fill: " + color + ";");
    }
    @FXML
    void homePage(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();


        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}