package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.util.Callback;
import models.Category;
import services.CategoryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import models.Category;
import services.CategoryService;
import javafx.scene.image.ImageView;

import java.io.IOException;


public class listCategory {
    @FXML
    private TextField tftheme;

    @FXML
    private TextField tfgenre;
    @FXML
    private ImageView logoIMG;
    @FXML
    private TextField tfSearchCategory;

    @FXML
    private ListView<Category> listCategory;
    ObservableList<Category> data= FXCollections.observableArrayList();
    CategoryService sp=new CategoryService();
    @FXML
    public void initialize(){

        refresh();
        listCategory.setCellFactory(new Callback<ListView<Category>, ListCell<Category>>() {
            @Override
            public ListCell<Category> call(ListView<Category> listView) {
                return new ListCell<Category>() {
                    @Override
                    public void updateItem(Category category, boolean empty) {
                        super.updateItem(category, empty);
                        if (empty || category == null) {
                            setText(null);
                        } else {
                            setText(String.format("Genre : %s%nTheme :    %s",
                                    category.getGenre(),
                                    category.getTheme()

                            ));


                            // Appliquer un style rouge uniquement si le texte est "Event Title"
                            if ("Category Title".equals(getText())) {
                                setStyle("-fx-text-fill: red;");
                            } else {
                                setStyle(null); // Retour au style par défaut
                            }

                            // Autres styles CSS que vous souhaitez appliquer
                            // setStyle("-fx-background-color: ...; -fx-font-weight: ...;");
                        }
                    }
                };
            }
        });

    }


    public void refresh(){
        data.clear();
        data=FXCollections.observableArrayList(sp.getAll());
        listCategory.setItems(data);
    }
    public String controleDeSaisire(){
        String erreur="";
        if(tftheme.getText().isEmpty()){
            erreur+="-Theme is required\n";
        }

        if(tfgenre.getText().isEmpty()){
            erreur+="-Genre is required\n";
        }


        return erreur;
    }
    @FXML
    void ajouterCategory(ActionEvent event) {
        //ps.add(new Personne(0,nomtf.getText(),prenomTF.getText(),Integer.parseInt(ageTF.getText())));
        String erreur=controleDeSaisire();
        if(erreur.length()>0){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ouups Invalide Form!");
            alert.setContentText(erreur);
            alert.showAndWait();
        }
        else {
            Category p=new Category();
            p.setTheme(tftheme.getText());
            p.setGenre(tfgenre.getText());
            sp.add(p);
            refresh();
        }

    }
    @FXML
    void fillforum(MouseEvent event) {
        Category p=listCategory.getSelectionModel().getSelectedItem();
        if(p!=null){
            tftheme.setText(p.getTheme());
            tfgenre.setText(p.getGenre());
        }
    }
    @FXML
    void modifierCategory(ActionEvent event) {
        String erreur=controleDeSaisire();
        if(erreur.length()>0){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ouups Invalide Form!");
            alert.setContentText(erreur);
            alert.showAndWait();
        }
        else {
            Category p=listCategory.getSelectionModel().getSelectedItem();
            p.setTheme(tftheme.getText());
            p.setGenre(tfgenre.getText());
            sp.update(p);
            refresh();
        }

    }
    @FXML
    void supprimerCategory(ActionEvent event) {
        Category p=listCategory.getSelectionModel().getSelectedItem();
        if(p!=null){
            sp.delete(p.getId_category());
            refresh();
        }
    }
    @FXML
    void GoBack(MouseEvent event) {
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
    void searchCategory(ActionEvent event) {
        String searchQuery = tfSearchCategory.getText().toLowerCase(); // Récupérer la valeur du champ de recherche

        // Filtrer les données en fonction de la recherche
        ObservableList<Category> filteredData = FXCollections.observableArrayList();
        for (Category category : data) {
            // Vérifier si le thème ou le genre contient la requête de recherche (en minuscules)
            if (category.getTheme().toLowerCase().contains(searchQuery) || category.getGenre().toLowerCase().contains(searchQuery)) {
                filteredData.add(category);
            }
        }

        // Mettre à jour la liste affichée avec les données filtrées
        listCategory.setItems(filteredData);
    }
}