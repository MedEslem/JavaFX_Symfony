package services;
import models.Event;
import utils.MyDataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.sql.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class EventService implements IService2<Event> {
    private Connection cnx;
    // private PreparedStatement stm;
    //  private Statement st;


    public EventService() {
        cnx = MyDataBase.getInstance().getConnection();
    }
    @Override
    public void add(Event event){
        String req = "INSERT INTO event (categorie_event_id , titre_event, description_event, etat_event, lieu_event, prix_event, nbr_personnes, date_event) VALUES (?,?,?,?,?,?,?,?)";
        try  {
            PreparedStatement stm  = cnx.prepareStatement(req);
            stm.setInt(1, event.getCategorie_event_id());
            stm.setString(2, event.getTitre_event());
            stm.setString(3, event.getDescription_event());
            stm.setString(4, event.getEtat_event());
            stm.setString(5, event.getLieu_event());
            stm.setFloat(6, event.getPrix_event());
            stm.setFloat(7, event.getNbr_personnes());
            stm.setDate(8, event.getDate_event());
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("add Event errors: " + e.getMessage());
        }

        //stm.setInt(2, event.getCategorie_event_id());


    }
    @Override
    public void update(Event event) {
        String sql = "UPDATE event SET categorie_event_id=?, titre_event=?, description_event=?, etat_event=?, lieu_event=?, prix_event=?, nbr_personnes=?, date_event=? WHERE id_event=?";
        try  {
            PreparedStatement stm = cnx.prepareStatement(sql);
            stm.setInt(1, event.getCategorie_event_id());
            stm.setString(2, event.getTitre_event());
            stm.setString(3, event.getDescription_event());
            stm.setString(4, event.getEtat_event());
            stm.setString(5, event.getLieu_event());
            stm.setFloat(6, event.getPrix_event());
            stm.setFloat(7, event.getNbr_personnes());
            stm.setDate(8, event.getDate_event());
            stm.setInt(9, event.getId_event());
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("updated Event errors: " + e.getMessage());
        }
    }
    @Override
    public void delete(int id)  {
        String sql = "DELETE FROM event WHERE id_event=?";
        try  {
            PreparedStatement preparedStatement = cnx.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("deleted event errors " + e.getMessage());
        }
    }
    @Override
    public Set<Event> getAll()  {
        Set<Event> events = new HashSet<>();
        String sql = "select * from event";
        try {
            Statement statement = cnx.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Event u = new Event();
                u.setId_event(rs.getInt("id_event"));
                u.setCategorie_event_id(rs.getInt("categorie_event_id"));
                u.setTitre_event(rs.getString("titre_event"));
                u.setDescription_event(rs.getString("description_event"));
                u.setEtat_event(rs.getString("etat_event"));
                u.setLieu_event(rs.getString("lieu_event"));
                u.setPrix_event(rs.getFloat("prix_event"));
                u.setNbr_personnes(rs.getFloat("nbr_personnes"));
                u.setDate_event(rs.getDate("date_event"));

                events.add(u);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des plans d'étude: " + e.getMessage());
        }
        return events;
    }
    @Override
    public Event getById(int id){

        return getAll().stream().filter(p->p.getId_event()==id).findAny().orElse(null);

    }
    public List<String> getNomEvent() {
        return getAll().stream().map(m -> m.getTitre_event()).collect(Collectors.toList());
    }


    public int getIdByName(String nom){
        return getAll().stream().filter(m->m.getTitre_event().equals(nom)).findAny().orElse(null).getId_event();
    }







}