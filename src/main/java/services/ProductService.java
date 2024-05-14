package services;
import models.Product;
import utils.MyDataBase;

import javax.swing.text.Document;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.sql.*;
import java.util.stream.Collectors;

public class ProductService implements IService2<Product> {
    private Connection cnx;

    public ProductService() {
        cnx = MyDataBase.getInstance().getConnection();
    }
    @Override
    public void add(Product product){
        String req = "INSERT INTO produit (categorie_prod_id , tags_id , nom_produit,description_produit, image_produit, prix_produit) VALUES (?,?,?,?,?,?)";
        try  {
            PreparedStatement stm  = cnx.prepareStatement(req);
            stm.setInt(1,product.getIdCategorie());
            stm.setInt(2,product.getIdTag());
            stm.setString(3,product.getNomP());
            stm.setString(4,product.getDescriptionP());
            stm.setString(5,product.getImageP());
            stm.setFloat(6,product.getPrixP());
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("add Product errors: " + e.getMessage());
        }



    }
    @Override
    public void update(Product product) {
        String sql = "UPDATE produit SET categorie_prod_id=?, tags_id=?, nom_produit=?,description_produit=?,image_produit=?, prix_produit=? WHERE id_produit=?";
        try  {
            PreparedStatement stm = cnx.prepareStatement(sql);
            stm.setInt(1, product.getIdCategorie());
            stm.setInt(2, product.getIdTag());
            stm.setString(3, product.getNomP());
            stm.setString(4, product.getDescriptionP());
            stm.setString(5, product.getImageP());
            stm.setFloat(6, product.getPrixP());
            stm.setInt(7, product.getIdP());
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("updated Product errors: " + e.getMessage());
        }
    }
    @Override
    public void delete(int id)  {
        String sql = "DELETE FROM produit WHERE id_produit=?";
        try  {
            PreparedStatement preparedStatement = cnx.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("deleted product errors " + e.getMessage());
        }
    }
    @Override
    public Set<Product> getAll()  {
        Set<Product> products= new HashSet<>();
        String sql = "select * from produit";
        try {
            Statement statement = cnx.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Product u = new Product();
                u.setIdP(rs.getInt("id_produit"));
                u.setIdCategorie(rs.getInt("categorie_prod_id"));
                u.setIdTag(rs.getInt("tags_id"));
                u.setNomP(rs.getString("nom_produit"));
                u.setDescriptionP(rs.getString("description_produit"));
                u.setImageP(rs.getString("image_produit"));
                u.setPrixP(rs.getInt("prix_produit"));
                products.add(u);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des plans d'étude: " + e.getMessage());
        }
        return products;
    }
    @Override
    public Product getById(int id){

        return getAll().stream().filter(p->p.getIdP()==id).findAny().orElse(null);

    }
    public TreeSet<Product> sortByTitre(){
        return getAll().stream()
                .collect(Collectors.toCollection(()->new TreeSet<>((c1, c2)->c1.getNomP().compareTo(c2.getNomP()))));
    }
    public TreeSet<Product> sortByCritere(String critere){
        switch (critere){
            case "Name":
                return getAll().stream()
                        .collect(Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Product::getNomP))));
            case "Description":
                return getAll().stream()
                        .collect(Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Product::getDescriptionP))));
            case "price":
                return getAll().stream()
                        .collect(Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Product::getPrixP))));

        }
        return getAll().stream()
                .collect(Collectors.toCollection(()->new TreeSet<>((c1, c2)->c1.getNomP().compareTo(c2.getNomP()))));

    }
    public String total() {
        Set<Product> products = getAll();
        DoubleSummaryStatistics stats = products.stream()
                .mapToDouble(Product::getPrixP)
                .summaryStatistics();

        return String.format("total price of products:\n- Number of Products: %d\n- Average Price: %.2f\n- Minimum Price: %.2f\n- Maximum Price: %.2f",
                stats.getCount(),
                stats.getAverage(),
                stats.getMin(),
                stats.getMax());
    }
    public Map<String, Integer> getProductsStatistics() throws SQLException {
        String sql = "SELECT d.nom_produit AS ProductName, SUM(d.prix_produit) AS totalPrice FROM produit d " +
                "GROUP BY d.nom_produit";
        Statement statement = cnx.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        Map<String, Integer> statistics = new HashMap<>();

        while (rs.next()) {
            String associationName = rs.getString("ProductName");
            int totalQuantity = rs.getInt("totalPrice");
            statistics.put(associationName, totalQuantity);
        }

        return statistics;
    }





}