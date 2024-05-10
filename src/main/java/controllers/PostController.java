package controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import models.Thread;
import models.Post;
import models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.pdfbox.io.IOUtils;
import services.ExcelExporter;
import services.ThreadService;
import services.PostService;
import utils.DButils;
import utils.PostCell;


import java.awt.*;
import java.io.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;


public class PostController {
    @FXML
    private Button attachButton;

    @FXML
    private TextField PostField;

    @FXML
    private ListView<Post> PostList;

    @FXML
    private Button sendButton;
    @FXML
    private Button emojiButton;
    @FXML
    private Label error;
    @FXML
    private ImageView imageView;
    @FXML
    private VBox vb;
    @FXML
    private Button delete;
    @FXML
    private Button update;

    @FXML
    private Button pdfButton;

    public void setPostField(TextField PostField) {
        this.PostField = PostField;
    }

    public static int thrdId  ;
    public static String emojis = "";

    private Post addedPost;


    User user1 = new User(2,"dhirar");
    User user2 = new User(1,"dhirar");
    User userConnected = DButils.connectedUser;
    PostService ms = new PostService();
    ThreadService ds = new ThreadService();


    public void initialize() throws SQLException {
        ObservableList<Post> Posts = FXCollections.observableList(ms.afficherByThreadId(thrdId));
        Thread d1 = ds.getById(thrdId);
        if(!userConnected.equals(d1.getCreatorThread())){
            delete.setVisible(false);
            update.setVisible(false);
        }
        vb.setStyle("-fx-background-color: " + d1.getColorThread() + ";");

        PostList.setItems(Posts);
        PostList.setCellFactory(param -> new PostCell());

        System.out.println(Posts);

        // Add event handler for the emoji button
        emojiButton.setOnAction(e -> {
            try {
                emojiPopup(); // Call the emojiPopup method
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        pdfButton.setOnAction(event -> {
            genererPDF();
            /*
            ObservableList<Post> posts = PostList.getItems(); // Fetch the posts from the ListView
            exportToExcel(posts, "posts.xlsx"); // Export to Excel
             */
        });

        sendButton.setOnAction(e -> {
            try {
                sendPost(); // Call the sendPost method to send the post
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        attachButton.setOnAction(e->{
            PostService.file = fileChooser.showOpenDialog(stage);
            if(PostService.file!=null){

                System.out.println(PostService.file);

                Image image = new Image(PostService.file.toURI().toString(),100,150,true,true);
                System.out.println(image);
                imageView.setImage(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);

            }

        });
        imageView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                ContextMenu contextMenu = new ContextMenu();

                // Create menu items
                MenuItem deleteMenuItem = new MenuItem("Delete");
                deleteMenuItem.setOnAction(e->{
                    imageView.setImage(null);
                    imageView.setFitWidth(0);
                    imageView.setFitHeight(0);
                    PostService.file=null ;
                });
                contextMenu.getItems().addAll( deleteMenuItem);

                // Show context menu at the mouse location
                contextMenu.show(PostList, event.getScreenX(), event.getScreenY());

            }
        });
        attachContextMenuToListView(PostList);


    }




    public void deleteThread()  {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation ");
            alert.setHeaderText("Look, a Confirmation Dialog");
            alert.setContentText("Are you ok with deleting the Thread?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                ds.delete(thrdId);
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("success ");
                alert1.setContentText("Thread deleted successfully");
                alert1.showAndWait();
                changeScene("/ListThreads.fxml");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void retour() {
        changeScene("/ListThreads.fxml");
    }
    public void changeScene(String s) {
        try {
            // Chargez le fichier FXML pour la nouvelle scène
            Parent root = FXMLLoader.load(getClass().getResource(s));
            PostList.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendPost() throws SQLException {
        String mess = PostField.getText();
        if(mess.isEmpty() && PostService.file == null){
            error.setText("The Post is empty !");

        }else{
            // Get the text from the PostField
            error.setText("");

            String text = PostField.getText();
            Timestamp currentTimestamp = new Timestamp( System.currentTimeMillis());

            // Create a new Post object
            Post post = new Post(text,currentTimestamp,userConnected);
            // add Post to the database
            ms.add(post);
            imageView.setImage(null);
            imageView.setFitWidth(0);
            imageView.setFitHeight(0);
            PostService.file=null;
            // Add the Post to the PostList
            PostList.getItems().add(post);
            initialize();

            // Clear the PostField
            PostField.clear();
        }

    }
    public void updateTitle(){
        changeScene("/modifyTitle.fxml");
    }


        Stage newStage = new Stage();

        public void emojiPopup() throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("/emojis.fxml"));
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();

            newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    String currentText = PostField.getText();
                    PostField.setText(currentText +" "+ emojis);
                    emojis = "";
                }
            });
        }

    private void attachContextMenuToListView(ListView<Post> listView) {
        listView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) { // Right-click detected
                Post selectedItem = listView.getSelectionModel().getSelectedItem();
                addedPost = selectedItem;
                System.out.println("Selected Post: " + addedPost);

                if (selectedItem != null) {
                    int PostId = selectedItem.getIdPost();

                    // Create the context menu
                    ContextMenu contextMenu = new ContextMenu();

                    // Create menu items
                    MenuItem updateMenuItem = new MenuItem("Update");
                    MenuItem deleteMenuItem = new MenuItem("Delete");
                    System.out.println(userConnected.equals(selectedItem.getSender()));
                    System.out.println(userConnected);
                    System.out.println(selectedItem);

                    if(userConnected.equals(selectedItem.getSender())) {
                        updateMenuItem.setOnAction(updateEvent -> {
                            int idPost = selectedItem.getIdPost();
                            String contentpost = selectedItem.getContentPost();
                            PostField.setText(contentpost);
                            sendButton.setOnAction(actionEvent -> {
                                if (PostField.getText().isEmpty()) {
                                    error.setText("The Post is empty !");
                                } else {
                                    Post post = new Post(idPost,PostField.getText());
                                    try {
                                        ms.update(post);
                                        PostField.clear();
                                        initialize();
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                            System.out.println("Update Post with ID: " + idPost);
                        });

                        // Handle delete menu item
                        deleteMenuItem.setOnAction(deleteEvent -> {
                            try {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation");
                                alert.setHeaderText("Look, a Confirmation Dialog");
                                alert.setContentText("Are you ok with deleting the Post?");
                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.get() == ButtonType.OK) {
                                    ms.delete(PostId);
                                    listView.getItems().remove(selectedItem);
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println("Delete Post with ID: " + PostId);
                        });
                    }
                    // Add menu items to context menu
                    contextMenu.getItems().addAll(updateMenuItem, deleteMenuItem);

                    // Show context menu at the mouse location
                    contextMenu.show(listView, event.getScreenX(), event.getScreenY());
                }
            }
        });

    }

    @FXML
    private void genererPDF() {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            PDType0Font font = PDType0Font.load(document, getClass().getResourceAsStream("/fonts/CairoPlay-VariableFont_slnt,wght.ttf"));

            float margin = 0;

            InputStream borderStream = getClass().getResourceAsStream("/image/BORDD.png");
            if (borderStream == null) {
                System.out.println("Image file not found!");
                return;
            }
            PDImageXObject borderImage = PDImageXObject.createFromByteArray(document, IOUtils.toByteArray(borderStream), "BORDD.png");

            contentStream.drawImage(borderImage, margin, margin, page.getMediaBox().getWidth() - 2 * margin, page.getMediaBox().getHeight() - 2 * margin);

            PDImageXObject logoImage = PDImageXObject.createFromFile("src/main/resources/image/logo_noir.png", document);
            float logoWidth = 125;
            float logoHeight = logoWidth * logoImage.getHeight() / logoImage.getWidth();

            contentStream.drawImage(logoImage, page.getMediaBox().getWidth() - margin - logoWidth - 15, page.getMediaBox().getHeight() - margin - logoHeight - 15, logoWidth, logoHeight);

            // Retrieve list of posts
            PostService postService = new PostService();
            List<Post> posts;
            try {
                posts = postService.getAll();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Start writing posts content
            float yPosition = page.getMediaBox().getHeight() - 2 * margin - logoHeight - 15 - 100;
            for (Post post : posts) {
                contentStream.beginText();

                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Post content: " + post.getContentPost());
                contentStream.newLine();
//                contentStream.newLineAtOffset(150, yPosition);
//                contentStream.showText("Timestamp: " + post.getTimeStamp_envoi());

                contentStream.endText();
                yPosition -= 50;
            }

            contentStream.close();

            File file = new File("ListOfPosts" + ".pdf");
            document.save(file);
            document.close();

            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void writeText(PDPageContentStream contentStream, String text, float x, float y, PDType0Font font) throws IOException {
        String[] lines = text.split("\n");
        float fontSize = 14; // Adjust the font size as needed
        float leading = 1.5f * fontSize; // Adjust the line spacing as needed

        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(x, y);

        for (String line : lines) {
            contentStream.showText(line);
            contentStream.newLineAtOffset(0, -leading);
        }

        contentStream.endText();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /*
    @FXML
    private void exportToExcel(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("posts.xlsx");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            String filePath = file.getAbsolutePath();
            try {
                ExcelExporter.exportToExcel(PostList.getItems(), filePath); // Pass 'PostList.getItems()' instead of 'posts'
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
*/


}
