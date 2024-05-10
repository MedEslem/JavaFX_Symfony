package services;

import models.CategorieProd;
import utils.MyDataBase;
import services.IService2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
public class CategoriepService implements IService2<CategorieProd> {
    private Connection cnx;

    public CategoriepService() {
        cnx = MyDataBase.getInstance().getConnection();
    }
    @Override
    public void add(CategorieProd categorie)  {
        String req = "INSERT INTO categorie_prod (nom_categ_p, image_categ) VALUES (?,?)";
        try  {
            PreparedStatement stm  = cnx.prepareStatement(req);
            stm.setString(1,categorie.getNomC());
            stm.setString(2,categorie.getImageC());
            System.out.println("categorie : "+"*"+ categorie.getNomC()+"*"+ "added succesfuly");
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("add category errors: " + e.getMessage());
        }
    }
    @Override
    public void update(CategorieProd categorie) {
        String sql = "UPDATE categorie_prod SET nom_categ_p=?, image_categ=?WHERE id_categ_prod=?";
        try {
            PreparedStatement stm = cnx.prepareStatement(sql);
            stm.setString(1,categorie.getNomC());
            stm.setString(2,categorie.getImageC());
            stm.setInt(3,categorie.getIdC());
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("updated category errors: " + e.getMessage());
        }
    }
    @Override
    public void delete(int id)  {
        String sql = "DELETE FROM categorie_prod WHERE id_categ_prod=?";
        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("deleted category errors" + e.getMessage());
        }
    }
    @Override
    public Set<CategorieProd> getAll()  {
        Set<CategorieProd> categories= new HashSet<>();
        String sql = "select * from categorie_prod";
        try {
            Statement statement = cnx.createStatement();
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                CategorieProd u = new CategorieProd();
                u.setIdC(rs.getInt("id_categ_prod"));
                u.setNomC(rs.getString("nom_categ_p"));
                u.setImageC(rs.getString("image_categ"));
                categories.add(u);
            }
        } catch (SQLException e) {
            System.out.println("display category errors " + e.getMessage());
        }
        return categories;
    }
    @Override
    public CategorieProd getById(int id)  {

        return getAll().stream().filter(p->p.getIdC()==id).findAny().orElse(null);

    }
    public List<String> getNomCategorie(){
        return getAll().stream().map(m->m.getNomC()).collect(Collectors.toList());
    }
    public int getIdByName(String nom){
        return getAll().stream().filter(m->m.getNomC().equals(nom)).findAny().orElse(null).getIdC();
    }

}