package services;

import utils.MyDataBase;
import models.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User> {

    private Connection connection;

    public UserService() {
        connection =MyDataBase.getInstance().getConnection();
    }

    @Override
    public void add(User user) throws SQLException {

        // Prepare a secure statement
        String sql = "INSERT INTO User (email, image_path, roles, password, wallet, username, is_verified, access) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);

        // Set values using PreparedStatement methods
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getImage_path());

        // Handle roles for compatibility with both String and String[] types
        if (user.getRoles() != null && user.getRoles().length > 0) {
            // Use the helper method from User.java
            String commaSeparatedRoles = user.createRolesString(user.getRoles());
            statement.setString(3, commaSeparatedRoles);
        } else {
            // Set an empty string or default value
            statement.setString(3, ""); // Or set a default role
        }


        statement.setString(4, user.getPassword());
        statement.setDouble(5, user.getWallet());
        statement.setString(6, user.getUsername());
        statement.setInt(7, user.getIs_verified());
        statement.setInt(8, user.getAccess());

        statement.executeUpdate();
    }



    @Override
    public void update(User user) throws SQLException {
        String sql = "UPDATE User SET  email = ?, image_path = ?, roles = ?, password = ?, wallet = ?, username = ?, is_verified = ?, access = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // Set values using PreparedStatement methods
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setString(2, user.getImage_path());

        // Handle roles as a String array
        if (user.getRoles() != null && user.getRoles().length > 0) {
            // Use the helper method from User.java
            String commaSeparatedRoles = user.createRolesString(user.getRoles());
            preparedStatement.setString(3, commaSeparatedRoles);
        } else {
            // Set an empty string or default value
            preparedStatement.setString(3, ""); // Or set a default role
        }

        preparedStatement.setString(4, user.getPassword()); // Never store passwords in plain text, consider hashing
        preparedStatement.setDouble(5, user.getWallet());
        preparedStatement.setString(6, user.getUsername());
        preparedStatement.setInt(7, user.getIs_verified());
        preparedStatement.setInt(8, user.getAccess());
        preparedStatement.setInt(9, user.getId()); // ID is the last parameter for the WHERE clause

        preparedStatement.executeUpdate();
    }




    @Override
    public void delete(int id) throws SQLException {
        String sql = "delete from User where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    public List<User> getAll() throws SQLException {
        String sql = "SELECT * FROM User";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();

        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setImage_path(rs.getString("image_path"));

            // Handle roles as a String array
            String rolesString = rs.getString("roles");
            if (rolesString != null && !rolesString.isEmpty()) {
                try {
                    // Attempt conversion to long array
                    long[] rolesArray = user.parseRolesFromString(rolesString); // Call helper method
                    user.setRoles(rolesArray);
                } catch (NumberFormatException e) {
                    // Handle the exception (e.g., log the error, set an empty array)
                    System.err.println("Error parsing roles for user " + user.getId() + ": " + e.getMessage());
                    user.setRoles(new long[0]); // Set an empty array
                }
            } else {
                // Handle cases where roles are null or empty (e.g., set an empty array)
                user.setRoles(new long[0]); // Or set default roles if applicable
            }

            user.setPassword(rs.getString("password")); // Reminder: Hash passwords for security
            user.setWallet(rs.getDouble("wallet"));
            user.setUsername(rs.getString("username"));
            user.setIs_verified(rs.getInt("is_verified"));
            user.setAccess(rs.getInt("access"));

            users.add(user);
        }

        return users;
    }



    @Override
    public User getById(int idUser) throws SQLException {
        String sql = "SELECT * FROM User WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idUser);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            User user = new User();
            user.setId(idUser); // Set the ID explicitly
            user.setEmail(resultSet.getString("email"));
            user.setImage_path(resultSet.getString("image_path"));

            // Handle roles as a String array with error handling
            String rolesString = resultSet.getString("roles");
            if (rolesString != null && !rolesString.isEmpty()) {
                try {
                    // Attempt conversion to long array
                    long[] rolesArray = user.parseRolesFromString(rolesString); // Call helper method
                    user.setRoles(rolesArray);
                } catch (NumberFormatException e) {
                    // Handle the exception (e.g., log the error, set an empty array)
                    System.err.println("Error parsing roles for user " + user.getId() + ": " + e.getMessage());
                    user.setRoles(new long[0]); // Set an empty array
                }
            } else {
                // Handle cases where roles are null or empty (e.g., set an empty array)
                user.setRoles(new long[0]); // Or set default roles if applicable
            }

            user.setPassword(resultSet.getString("password")); // Reminder: Hash passwords for security
            user.setWallet(resultSet.getDouble("wallet"));
            user.setUsername(resultSet.getString("username"));
            user.setIs_verified(resultSet.getInt("is_verified"));
            user.setAccess(resultSet.getInt("access"));

            return user;
        } else {
            // No matching record found
            return null;
        }
    }

}