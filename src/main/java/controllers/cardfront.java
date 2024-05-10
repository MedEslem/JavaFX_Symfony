package controllers;

import models.Product;
import services.CategoriepService;
import services.OnChangeListener;
import services.ProductService;
import services.TagsService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class cardfront {
    @FXML
    private ImageView img;

    @FXML
    private Label lname;

    @FXML
    private Label ldescription;

    @FXML
    private Label lprice;

    @FXML
    private Label lcategorie;

    @FXML
    private Label ltag;
    ProductService sp=new ProductService();
    CategoriepService sc=new CategoriepService();
    TagsService st=new TagsService();
    private OnChangeListener onChangeListener;

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }
    Product product;

    public void remplireData(Product c) {
        product = c; //nstha9ha bch n3abi l produit fl refresh nst7a9ha fl modif w supp
        lname.setText(c.getNomP());
        ldescription.setText(c.getDescriptionP());
        lprice.setText(String.valueOf(c.getPrixP()));
        lcategorie.setText(sc.getById(c.getIdCategorie()).getNomC());
        ltag.setText(st.getById(c.getIdTag()).getGenre());
        File file = new File("C:/Users/USER/IdeaProjects/Al3abGames-Pi-3A-JavaFX--b97d291ed9629ef41b918d1097845ca0b517f19a/src/main/resources/ImageProduct/" + c.getImageP());
        Image image = new Image(file.toURI().toString());
        img.setImage(image);
    }

    @FXML
    public void initialize() {

    }
}