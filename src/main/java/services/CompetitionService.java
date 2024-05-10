package services;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Competition;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompetitionService implements IService<Competition> {

    private Connection connection;

    public CompetitionService() {
        connection = MyDataBase.getInstance().getConnection();
    }

    public void add(Competition competition) throws SQLException {
        String req = "INSERT INTO competition (description_c, type_c, jeu_c,  date_c, heure_c) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            // Définition des valeurs à l'aide des méthodes setter
            preparedStatement.setString(1, competition.getDescription_c());
            preparedStatement.setString(2, competition.getType_c());
            preparedStatement.setString(3, competition.getJeu_c());


            // Convertir la java.util.Date en java.sql.Date
            preparedStatement.setDate(4, new Date(competition.getDate_c().getTime()));
            preparedStatement.setTime(5, new Time(competition.getHeure_c().getTime()));


            // Exécution de la requête
            preparedStatement.executeUpdate();

            System.out.println("Compétition ajoutée.");
        } catch (SQLException e) {
            // Gestion de l'exception SQLException
            System.err.println("Erreur lors de l'ajout de la compétition : " + e.getMessage());
            // Vous pouvez également propager l'exception à la couche supérieure si nécessaire
            throw e;
        }
    }


    public ObservableList<Competition> RecupBase(){

        ObservableList<Competition> list = FXCollections.observableArrayList();
        CompetitionService competitionService = new CompetitionService();
        Competition r = null;

        java.sql.Connection cnx;
        cnx = MyDataBase.getInstance().getConnection();
        String sql = "select *from competition";
        try {

            PreparedStatement st = (PreparedStatement) cnx.prepareStatement(sql);

            ResultSet rs = st.executeQuery();
            while (rs.next()){
                r = new Competition(rs.getInt("id"), rs.getString("description_c"),rs.getString("type_c"),rs.getString("jeu_c"),rs.getDate("date_c"),rs.getTime("heure_c"));


                list.add(r);
            }
        }catch (SQLException ex){
            ex.getMessage();
        }
        return list;
    }

    @Override
    public void update(Competition competition) throws SQLException {
        String req = "UPDATE competition SET description_c=?, type_c=? , jeu_c=? ,date_c=? , heure_c=?  WHERE id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            // Définition des valeurs à l'aide des méthodes setter
            preparedStatement.setString(1, competition.getDescription_c());
            preparedStatement.setString(2, competition.getType_c());
            preparedStatement.setString(3, competition.getJeu_c());

            preparedStatement.setDate(4, new Date(competition.getDate_c().getTime()));
            preparedStatement.setTime(5, new Time(competition.getHeure_c().getTime()));
            preparedStatement.setInt(6, competition.getId());

            // Exécution de la requête
            preparedStatement.executeUpdate();

            System.out.println("Compétition mise à jour.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la compétition : " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM competition WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Suppression de la compétition échouée : " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Competition> getAll() throws SQLException {

        List<Competition> competitions = new ArrayList<>();
        String req = "SELECT * FROM competition";
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery(req);
        while (rs.next()) {
            Competition competition = new Competition();
            competition.setId(rs.getInt("id"));
            competition.setDescription_c(rs.getString("description_c"));
            competition.setType_c(rs.getString("type_c"));

            // Appel de la méthode getJeu_c() du contrôleur pour obtenir le nom du jeu

            competition.setJeu_c(rs.getString("jeu_c")); // Passer l'ID de la compétition
            competition.setDate_c(rs.getDate("date_c"));
            competition.setHeure_c(rs.getTime("heure_c"));

            competitions.add(competition);
        }
        return competitions;
    }




    // Méthode pour récupérer une compétition par son ID

    public Competition Selectcompetition(int id){
        Competition r = new Competition();
        String req = "SELECT * FROM competition where id ="+id+"";

        try {
            PreparedStatement ps = connection.prepareStatement(req);

            ResultSet rs = ps.executeQuery(req);

            while(rs.next()){

                r = new Competition(rs.getInt("id"), rs.getString("description_c"), rs.getString("type_c"), rs.getString("jeu_c"), rs.getDate("date_c"), rs.getTime("heure_c"));


            }


        } catch (SQLException ex) {
            Logger.getLogger(CompetitionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

    // Méthode pour récupérer le type de compétition à partir de l'ID de la compétition
    public String getCompetitionType(int competitionId) throws SQLException {
        String query = "SELECT jeu_c FROM competition WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, competitionId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("jeu_c");
                } else {
                    return null;
                }
            }
        }
    }
    public Competition SelectPartenairebyname(String nom){
        Competition r = null;
        String req = "SELECT * FROM competition WHERE jeu_c = ?";  // Use prepared statements with placeholders

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, nom);  // Set the name parameter in the query
            ResultSet rs = ps.executeQuery();

            if(rs.next()){  // Use if instead of while if you expect one or zero results
                r = new Competition(rs.getInt("id"), rs.getString("description_c"), rs.getString("type_c"), rs.getString("jeu_c"), rs.getDate("date_c"), rs.getTime("heure_c"));


            }
        } catch (SQLException ex) {
            Logger.getLogger(CompetitionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }
    public Map<String, Integer> getGamesSelectionCount() throws SQLException {
        Map<String, Integer> gamesSelectionCount = new HashMap<>();

        // Requête SQL pour compter le nombre de sélections pour chaque jeu
        String sqlQuery = "SELECT jeu_c, COUNT(*) AS selection_count FROM competition GROUP BY jeu_c";

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            // Parcourir les résultats de la requête
            while (resultSet.next()) {
                String game = resultSet.getString("jeu_c");
                int selectionCount = resultSet.getInt("selection_count");
                gamesSelectionCount.put(game, selectionCount);
            }
        }

        return gamesSelectionCount;
    }
    public String getMostPlayedGame() {
        try {
            // Récupérer le nombre de sélections pour chaque jeu
            Map<String, Integer> gamesSelectionCount = getGamesSelectionCount();

            // Variables pour stocker le jeu le plus joué et son nombre de sélections
            String mostPlayedGame = null;
            int maxSelections = 0;

            // Parcourir la map pour trouver le jeu le plus joué
            for (Map.Entry<String, Integer> entry : gamesSelectionCount.entrySet()) {
                String game = entry.getKey();
                int selections = entry.getValue();

                // Si le nombre de sélections est supérieur au maximum trouvé jusqu'à présent,
                // mettre à jour le jeu le plus joué et son nombre de sélections
                if (selections > maxSelections) {
                    mostPlayedGame = game;
                    maxSelections = selections;
                }
            }

            // Retourner le jeu le plus joué
            return mostPlayedGame;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Méthode pour récupérer une compétition par son ID
    public Competition getById(int id) throws SQLException {

        String req = "SELECT * FROM competition WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Competition competition = new Competition();
                    competition.setId(resultSet.getInt("id"));
                    competition.setDescription_c(resultSet.getString("description_c"));
                    competition.setType_c(resultSet.getString("type_c"));
                    competition.setJeu_c(resultSet.getString("jeu_c"));

                    competition.setDate_c(resultSet.getDate("date_c"));
                    competition.setHeure_c(resultSet.getTime("heure_c"));


                    return competition;
                } else {
                    // Si aucun enregistrement n'est trouvé pour l'ID donné, retourner null
                    return null;
                }
            }
        }
    }


    public Competition getByName(String name) throws SQLException {
        String req = "SELECT * FROM competition WHERE jeu_c=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Competition competition = new Competition();
                    competition.setId(resultSet.getInt("id"));
                    competition.setDescription_c(resultSet.getString("description_c"));
                    competition.setType_c(resultSet.getString("type_c"));
                    competition.setJeu_c(resultSet.getString("jeu_c"));

                    competition.setDate_c(resultSet.getDate("date_c"));
                    competition.setHeure_c(resultSet.getTime("heure_c"));
                    return competition;
                } else {
                    // Si aucune compétition n'est trouvée pour le nom donné, retourner null
                    return null;
                }
            }
        }
    }

    public ObservableList<String> RecupCombo() {
        ObservableList<String> list = FXCollections.observableArrayList();

        try {
            // Établir la connexion à la base de données
            Connection cnx = MyDataBase.getInstance().getConnection();

            // Préparer la requête SQL pour récupérer les données
            String sql = "SELECT jeu_c FROM competition";

            // Exécuter la requête SQL
            try (PreparedStatement st = cnx.prepareStatement(sql);
                 ResultSet rs = st.executeQuery()) {

                // Parcourir les résultats de la requête
                while (rs.next()) {
                    // Récupérer la valeur de jeu_c pour chaque compétition
                    String jeu_c = rs.getString("jeu_c");

                    // Ajouter la valeur à la liste observable
                    list.add(jeu_c);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;
    }





}