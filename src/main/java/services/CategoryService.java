package services;

import models.Category;
import utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.sql.*;
import java.util.Set;
import java.util.Optional;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CategoryService implements IService2<Category> {
    private Connection cnx;

    public CategoryService() {
        cnx = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void add(Category category) {
        String req = "INSERT INTO category ( theme , genre) VALUES (?,?)";
        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, category.getTheme());
            stm.setString(2, category.getGenre());
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("add category errors: " + e.getMessage());
        }
    }

    @Override
    public void update(Category category) {
        String sql = "UPDATE category SET theme=?, genre=? WHERE id_category=?";
        try {
            PreparedStatement stm = cnx.prepareStatement(sql);
            stm.setString(1, category.getTheme());
            stm.setString(2, category.getGenre());
            stm.setInt(3, category.getId_category());
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("update category errors: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM category WHERE id_category=?";
        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("delete category errors: " + e.getMessage());
        }
    }

    @Override
    public Set<Category> getAll() {
        String sql = "SELECT * FROM category";
        Set<Category> categorySet = new HashSet<>();
        try {
            Statement statement = cnx.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Category category = new Category();
                category.setId_category(rs.getInt("id_category"));
                category.setTheme(rs.getString("theme"));
                category.setGenre(rs.getString("genre"));
                categorySet.add(category);
            }
        } catch (SQLException e) {
            System.out.println("display category errors: " + e.getMessage());
        }
        return categorySet;
    }

    @Override
    public Category getById(int id) {
        return getAll().stream().filter(p -> p.getId_category() == id).findAny().orElse(null);
    }

    public List<String> getNomCategory() {
        return getAll().stream().map(m -> m.getGenre()).collect(Collectors.toList());
    }

    // Modifier la méthode getIdByName() dans la classe CategoryService
    public int getIdByName(String name) {
        Optional<Category> optionalCategory = getAll().stream().filter(m -> m.getGenre().equals(name)).findAny();
        if (optionalCategory.isPresent()) {
            return optionalCategory.get().getId_category();
        } else {
            // Gérer le cas où la catégorie n'est pas trouvée
            throw new IllegalArgumentException("La catégorie avec le nom '" + name + "' n'a pas été trouvée.");
        }
    }
}