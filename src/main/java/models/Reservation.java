package models;

import java.util.Objects;
import java.sql.Date;

public class Reservation {
    private int id_reservation ;
    private String mode_paiement;
    private Date date_reservation;

    private int event_id  ;
    private int user_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Reservation(int id_reservation , String mode_paiement, int event_id, Date date_reservation, int user_id  ) {
        this.id_reservation  = id_reservation ;
        this.mode_paiement = mode_paiement;
        this.date_reservation = Date.valueOf(String.valueOf(date_reservation));
        this.event_id = event_id;
        this.user_id=user_id;
    }
    @Override
    public String toString() {
        return "Reservation{" +
                "id_reservation=" + id_reservation +
                ", mode_paiement='" + mode_paiement + '\'' +
                ", date_reservation=" + date_reservation +
                ", event_id=" + event_id +
                ", user_id=" + user_id +
                '}';
    }

    public Reservation() {
    }

    public String getMode_paiement() {
        return mode_paiement;
    }

    public void setMode_paiement(String mode_paiement) {
        this.mode_paiement = mode_paiement;
    }

    public Date getDate_reservation() {
        return date_reservation;
    }

    public void setDate_reservation(Date date_reservation) {
        this.date_reservation = date_reservation;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public int getId_reservation () {
        return id_reservation ;
    }

    public void setId_reservation (int id_reservation ) {
        this.id_reservation  = id_reservation ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id_reservation  == that.id_reservation  && Objects.equals(mode_paiement, that.mode_paiement) && Objects.equals(date_reservation, that.date_reservation)  && Objects.equals(event_id, that.event_id) && Objects.equals(user_id, that.user_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_reservation , mode_paiement, date_reservation, event_id,user_id );
    }


}
