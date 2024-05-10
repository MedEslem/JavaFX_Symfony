package services;

import models.Thread;
import models.User;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThreadService implements IService<Thread> {

    private Connection connection;

    public ThreadService() {
        connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void add(Thread thread) throws SQLException {
        String query = "INSERT INTO `thread` (`titleThread`, `descriptionThread`, `colorThread`, `TimeStampCreation`, `creatorId`)" +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, thread.getTitleThread());
            preparedStatement.setString(2, thread.getDescriptionThread());
            preparedStatement.setString(3, thread.getColorThread());
            preparedStatement.setTimestamp(4, thread.getTimeStampCreation());
            preparedStatement.setInt(5, thread.getCreatorThread().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error while adding thread: " + e.getMessage());
        }
    }

    @Override
    public void update(Thread thread) throws SQLException {
        String query = "UPDATE `thread` SET `titleThread` = ?, `descriptionThread` = ?, `colorThread` = ? WHERE `idThread` = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, thread.getTitleThread());
            preparedStatement.setString(2, thread.getDescriptionThread());
            preparedStatement.setString(3, thread.getColorThread());
            preparedStatement.setInt(4, thread.getIdThread());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Error while updating thread: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM `thread` WHERE `idThread` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error while deleting thread: " + e.getMessage());
        }
    }

    @Override
    public Thread getById(int id) throws SQLException {
        String query = "SELECT * FROM `thread` WHERE `idThread` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    String title = resultSet.getString("titleThread");
                    String description = resultSet.getString("descriptionThread");
                    String color = resultSet.getString("colorThread");
                    Timestamp timeStampCreation = resultSet.getTimestamp("TimeStampCreation");
                    int creatorId = resultSet.getInt("creatorId");

                    // Fetch creator information using creatorId
                    String creatorQuery = "SELECT id, username FROM user WHERE id = ?";
                    try (PreparedStatement creatorStatement = connection.prepareStatement(creatorQuery)) {
                        creatorStatement.setInt(1, creatorId);
                        try (ResultSet creatorResultSet = creatorStatement.executeQuery()) {
                            if (creatorResultSet.next()) {
                                int userId = creatorResultSet.getInt("id");
                                String userName = creatorResultSet.getString("username");
                                User user = new User(userId, userName);
                                return new Thread(id, title,timeStampCreation, user, description, color);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error while fetching thread: " + e.getMessage());
        }
        return null;
    }


    @Override
    public List<Thread> getAll() throws SQLException {
        List<Thread> threads = new ArrayList<>();
        String query = "SELECT t.idThread, t.titleThread, t.descriptionThread, t.colorThread, t.TimeStampCreation, u.id, u.username " +
                "FROM thread t " +
                "JOIN user u ON t.creatorId = u.id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("idThread");
                    String title = resultSet.getString("titleThread");
                    String description = resultSet.getString("descriptionThread");
                    String color = resultSet.getString("colorThread");
                    Timestamp timeStampCreation = resultSet.getTimestamp("TimeStampCreation");
                    int userId = resultSet.getInt("id");
                    String userName = resultSet.getString("username");

                    // Create User object
                    User user = new User(userId, userName);

                    // Create Thread object and add it to the list
                    threads.add(new Thread(id, title,timeStampCreation, user, description, color));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error while fetching threads: " + e.getMessage());
        }
        return threads;
    }
}
