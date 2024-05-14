package services;
import models.Reservation;

import utils.MyDataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.sql.*;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
public class ReservationpService implements IService2<Reservation> {
    private Connection cnx;

    public ReservationpService() {
        cnx = MyDataBase.getInstance().getConnection();
    }
    @Override
    public void add(Reservation reservation)  {
        String req = "INSERT INTO reservation (mode_paiement, date_reservation, event_id,user_id) VALUES (?,?,?,?)";
        try  {
            PreparedStatement stm  = cnx.prepareStatement(req);
            stm.setString(1,reservation.getMode_paiement());
            stm.setDate(2, reservation.getDate_reservation());
            stm.setInt(3,reservation.getEvent_id());
            stm.setInt(4,reservation.getUser_id());

            //  System.out.println("reservation : "+"*"+ reservation.getMode_paiement()+"*"+ "added succesfuly");
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("add category errors: " + e.getMessage());
        }
    }
    @Override
    public void update(Reservation reservation) {
        String sql = "UPDATE reservation SET mode_paiement=?, date_reservation=?, event_id=?,user_id=? WHERE id_reservation=?";
        try {
            PreparedStatement stm = cnx.prepareStatement(sql);
            stm.setString(1,reservation.getMode_paiement());
            stm.setDate(2,reservation.getDate_reservation());
            stm.setInt(3,reservation.getEvent_id());
            stm.setInt(4,reservation.getId_reservation());
            stm.setInt(5,reservation.getUser_id());
            stm.executeUpdate();
            System.out.println("Updated Succesfully");
        } catch (SQLException e) {
            System.out.println("updated reservation errors: " + e.getMessage());
        }
    }
    @Override
    public void delete(int id)  {
        String sql = "DELETE FROM reservation WHERE id_reservation=?";
        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("deleted reservation errors" + e.getMessage());
        }
    }
    @Override
    public Set<Reservation> getAll()  {
        Set<Reservation> reservations = new HashSet<>();
        String sql = "select * from reservation";
        try {
            Statement statement = cnx.createStatement();
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                Reservation u = new Reservation();
                u.setId_reservation(rs.getInt("id_reservation"));
                u.setEvent_id(rs.getInt("event_id"));
                u.setMode_paiement(rs.getString("mode_paiement"));
                u.setDate_reservation(rs.getDate("date_reservation"));
                u.setUser_id(rs.getInt("user_id"));
                reservations.add(u);
            }
        } catch (SQLException e) {
            System.out.println("display reservation errors " + e.getMessage());
        }
        return reservations;
    }
    @Override
    public Reservation getById(int id)  {

        return getAll().stream().filter(p->p.getId_reservation()==id).findAny().orElse(null);

    }
    //public List<String> getNomReservation(){
    //   return getAll().stream().map(m->m.getMode_paiement()).collect(Collectors.toList());
    //}
    // public int getIdByName(String nom){
    //    return getAll().stream().filter(m->m.getMode_paiement().equals(nom)).findAny().orElse(null).getId_reservation();
    // }

}