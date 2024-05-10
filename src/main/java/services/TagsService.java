package services;

import services.IService2;
import models.Tags;
import utils.MyDataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import java.util.Set;
import java.util.stream.Collectors;


public class TagsService implements IService2<Tags> {
    private Connection cnx;

    public TagsService() {
        cnx = MyDataBase.getInstance().getConnection();
    }
    @Override
    public void add(Tags tags) {
        String req = "INSERT INTO tags ( theme , genre) VALUES (?,?)";
        try {
            PreparedStatement stm  = cnx.prepareStatement(req);
            stm.setString(1,tags.getTheme());
            stm.setString(2,tags.getGenre());
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("add tag errors: " + e.getMessage());
        }
    }
    @Override
    public void update(Tags tags) {
        String sql = "UPDATE tags SET theme=?, genre=?WHERE id_tag=?";
        try {
            PreparedStatement stm = cnx.prepareStatement(sql);
            stm.setString(1,tags.getTheme());
            stm.setString(2,tags.getGenre());
            stm.setInt(3,tags.getIdT());
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("add tag errors: " + e.getMessage());
        }
    }
    @Override
    public void delete(int id)  {
        String sql = "DELETE FROM tags WHERE id_tag=?";
        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("add tag errors: " + e.getMessage());
        }
    }
    @Override
    public Set<Tags> getAll()  {
        String sql = "select * from tags";
        Set<Tags> tags= new HashSet<>();
        try {
            Statement statement = cnx.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Tags u = new Tags();
                u.setIdT(rs.getInt("id_tag"));
                u.setTheme(rs.getString("theme"));
                u.setGenre(rs.getString("genre"));
                tags.add(u);
            }
        } catch (SQLException e) {
            System.out.println("display category errors " + e.getMessage());
        }
        return tags;
    }
    @Override
    public Tags getById(int id)  {

        return getAll().stream().filter(p->p.getIdT()==id).findAny().orElse(null);

    }
    public List<String> getNomTag(){
        return getAll().stream().map(m->m.getGenre()).collect(Collectors.toList());
    }
    public int getIdByName(String nom){
        return getAll().stream().filter(m->m.getGenre().equals(nom)).findAny().orElse(null).getIdT();

    }
}