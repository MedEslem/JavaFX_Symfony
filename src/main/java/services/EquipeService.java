package services;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Competition;
import models.Equipe;
import utils.MyDataBase;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class EquipeService implements IService<Equipe> {

    Connection connection;
    private CompetitionService competitionService;


    public EquipeService() {
        connection = MyDataBase.getInstance().getConnection();
        competitionService = new CompetitionService();
    }

    public Image generateQRCodeImage(Equipe equipe, int width, int height) throws WriterException, IOException {
        // Construire le contenu du code QR à partir des informations de l'équipe
        String qrCodeText = "Name of team : " + equipe.getNom_e() + "\n" +
                "Competition : " + equipe.getCompetition().getJeu_c() + "\n" +
                "Number of participants : " + equipe.getNb_p() +
                "Logo of team : " + equipe.getLogo_e();


        // Générer la matrice de bits pour le code QR
        BitMatrix bitMatrix = generateBitMatrix(qrCodeText, width, height);

        // Convertir la matrice de bits en une image BufferedImage
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // Convertir BufferedImage en Image JavaFX
        return convertToJavaFXImage(bufferedImage);
    }

    // Méthode pour générer la matrice de bits pour le code QR
    private BitMatrix generateBitMatrix(String qrCodeText, int width, int height) throws WriterException {
        // Créer un objet QRCodeWriter de ZXing
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        // Définir les options pour la génération du code QR
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        // Générer la matrice de bits pour le code QR
        return qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, width, height, hints);
    }

    // Méthode pour convertir BufferedImage en Image JavaFX
    private Image convertToJavaFXImage(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", outputStream);
        return new Image(new ByteArrayInputStream(outputStream.toByteArray()));
    }
    // Méthode pour récupérer une liste observable des équipes à partir de la base de données
    public ObservableList<Equipe> getAllEquipesObservableFromDB() {
        ObservableList<Equipe> observableList = FXCollections.observableArrayList();
        try {
            observableList.addAll(fetchEquipesFromDatabase());
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des équipes de la base de données : " + e.getMessage());
        }
        return observableList;
    }
    public ObservableList<Equipe> getAllEquipesByCompetition(Competition competition) {
        ObservableList<Equipe> equipesByCompetition = FXCollections.observableArrayList();
        try {
            // Récupérer toutes les équipes de la base de données
            List<Equipe> allEquipes = fetchEquipesFromDatabase();
            // Filtrer les équipes par compétition
            for (Equipe equipe : allEquipes) {
                if (equipe.getCompetition().equals(competition)) {
                    equipesByCompetition.add(equipe);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Gérer l'exception si nécessaire
        }
        return equipesByCompetition;
    }

    private List<Equipe> fetchEquipesFromDatabase() throws SQLException {
        List<Equipe> equipeList = new ArrayList<>();
        String sql = "SELECT * FROM equipe";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Equipe equipe = new Equipe();
                equipe.setId(rs.getInt("id"));
                equipe.setNom_e(rs.getString("nom_e"));
                equipe.setLogo_e(rs.getString("logo_e"));
                equipe.setNb_p(rs.getInt("nb_p"));
                int competitionId = rs.getInt("comp_id");
                Competition competition = competitionService.getById(competitionId);
                equipe.setCompetition(competition);
                equipeList.add(equipe);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des équipes de la base de données : " + e.getMessage());
            throw e;
        }
        return equipeList;
    }


    public void add(Equipe equipe) throws SQLException {
        // Vérifier si l'équipe est associée à une compétition
        // Vérifier si l'équipe est null
        if (equipe == null) {
            throw new IllegalArgumentException("Equipe object is null.");
        }

        // Vérifier si l'équipe est associée à une compétition
        if (equipe.getCompetition() == null) {
            throw new IllegalArgumentException("L'équipe doit être associée à une compétition.");
        }
        String req = "INSERT INTO equipe (nom_e, logo_e, nb_p,comp_id,user_id) VALUES (?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setString(1, equipe.getNom_e());
            preparedStatement.setString(2, equipe.getLogo_e());
            preparedStatement.setInt(3, equipe.getNb_p());
            // Récupérer l'ID de la compétition à partir de l'objet Equipe
            int compId = Integer.parseInt(String.valueOf(equipe.getCompetition().getId()));
            preparedStatement.setInt(4, compId);
            preparedStatement.setInt(5, equipe.getUser_id());

            // Exécution de la requête
            preparedStatement.executeUpdate();

            System.out.println("Équipe ajoutée.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'équipe : " + e.getMessage());
            throw e;
        }
    }


    public ObservableList<Equipe> RecupBase(){

        ObservableList<Equipe> list = FXCollections.observableArrayList();

        java.sql.Connection cnx;
        cnx = MyDataBase.getInstance().getConnection();
        String sql = "select *from equipe";
        try {

            PreparedStatement st = (PreparedStatement) cnx.prepareStatement(sql);

            ResultSet rs = st.executeQuery();
            while (rs.next()){
                Equipe equipe = new Equipe();
                int id = rs.getInt("id");
                String nom_e = rs.getString("nom_e");
                String logo_e = rs.getString("logo_e");
                int nb_p= rs.getInt("nb_p");
                int idUser= rs.getInt("user_id");

                // Use existing competitionService member variable
                Competition competition = competitionService.Selectcompetition(rs.getInt("comp_id"));
                equipe.setId(id);
                equipe.setLogo_e(logo_e);
                equipe.setNom_e(nom_e);
                equipe.setNb_p(nb_p);
                equipe.setCompetition(competition);
                equipe.setUser_id(idUser);

                list.add(equipe);
            }
        }catch (SQLException ex){
            ex.getMessage();
        }
        return list;
    }



    public void update(Equipe equipe) throws SQLException {
        String req = "UPDATE equipe SET nom_e = ?, logo_e = ?,nb_p= ?, comp_id = ?,user_id=? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setString(1, equipe.getNom_e());
            preparedStatement.setString(2, equipe.getLogo_e());
            preparedStatement.setInt(3, equipe.getNb_p());

            // Récupérer l'ID de la compétition à partir de l'objet Equipe
            //   int compId = Integer.parseInt(String.valueOf(equipe.getCompetition().getId()));
            preparedStatement.setInt(4, equipe.getCompetition().getId());
            preparedStatement.setInt(5, equipe.getUser_id());

            preparedStatement.setInt(6, equipe.getId());

            // Exécution de la requête
            preparedStatement.executeUpdate();

            System.out.println("Équipe mise à jour.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'équipe : " + e.getMessage());
            throw e;
        }
    }








    @Override
    public void delete(int id) throws SQLException {
        String sql = "delete from equipe where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();

    }


    public List<Equipe> getAll() throws SQLException {
        List<Equipe> equipeList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = MyDataBase.getInstance().getConnection();
            String query = "SELECT e.id, e.nom_e, e.logo_e,e.nb_p, e.comp_id,e.user_id, c.jeu_c FROM equipe e JOIN competition c ON e.comp_id = c.id";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom_e = resultSet.getString("nom_e");
                String logo_e = resultSet.getString("logo_e");
                int nb_p = resultSet.getInt("nb_p");

                int competitionId = resultSet.getInt("comp_id");
                int UserId = resultSet.getInt("user_id");
                String jeu_c = resultSet.getString("jeu_c");
                Competition competition = new Competition(competitionId, resultSet.getString("description_c"), resultSet.getString("type_c"), resultSet.getString("jeu_c"), resultSet.getDate("date_c"), resultSet.getTime("heure_c"));

                Equipe equipe = new Equipe(id,nom_e,logo_e,competition,nb_p, UserId);

                equipeList.add(equipe);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return equipeList;
    }





    public Equipe getById(int id) throws SQLException {
        String req = "SELECT e.*, c.jeu_c FROM equipe e JOIN competition c ON e.comp_id = c.id WHERE e.id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int equipeId = resultSet.getInt("id");
                    String nom_e = resultSet.getString("nom_e");
                    String logo_e = resultSet.getString("logo_e");
                    int nb_p = resultSet.getInt("nb_p");
                    int competitionId = resultSet.getInt("comp_id");
                    int UserId = resultSet.getInt("user_id");
                    String jeu_c = resultSet.getString("jeu_c");
                    Competition competition = new Competition(competitionId, resultSet.getString("description_c"), resultSet.getString("type_c"), resultSet.getString("jeu_c"), resultSet.getDate("date_c"), resultSet.getTime("heure_c"));

                    Equipe equipe = new Equipe(id,nom_e,logo_e,competition,nb_p, UserId);
                    return equipe;

                } else {
                    // Si aucun enregistrement n'est trouvé pour l'ID donné, retourner null
                    return null;
                }
            }
        }
    }


    public Equipe getByName(String name) throws SQLException {
        return null;
    }



}