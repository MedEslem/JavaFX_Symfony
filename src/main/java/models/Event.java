package models;

import java.sql.Date;
import java.util.Objects;

public class Event {
    private int id_event;
    private String titre_event;
    private String description_event;
    private String etat_event;
    private String lieu_event;
    private float prix_event;
    private float nbr_personnes;
    private Date date_event;
    private int categorie_event_id;

    public Event(int id_event, String titre_event, String description_event, String etat_event, String lieu_event, float prix_event, float nbr_personnes, Date date_event, int categorie_event_id) {
        this.id_event = id_event;
        this.titre_event = titre_event;
        this.description_event = description_event;
        this.etat_event = etat_event;
        this.lieu_event = lieu_event;
        this.prix_event = prix_event;
        this.nbr_personnes = nbr_personnes;
        this.date_event = date_event;
        this.categorie_event_id = categorie_event_id;
    }

    public Event(String titre_event, String description_event, String etat_event, String lieu_event, float prix_event, float nbr_personnes, Date date_event, int categorie_event_id) {
        this.titre_event = titre_event;
        this.description_event = description_event;
        this.etat_event = etat_event;
        this.lieu_event = lieu_event;
        this.prix_event = prix_event;
        this.nbr_personnes = nbr_personnes;
        this.date_event = date_event;
        this.categorie_event_id = categorie_event_id;
    }

    public Event() {
    }

    public int getId_event() {
        return id_event;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public String getTitre_event() {
        return titre_event;
    }

    public void setTitre_event(String titre_event) {
        this.titre_event = titre_event;
    }

    public String getDescription_event() {
        return description_event;
    }

    public void setDescription_event(String description_event) {
        this.description_event = description_event;
    }

    public String getEtat_event() {
        return etat_event;
    }

    public void setEtat_event(String etat_event) {
        this.etat_event = etat_event;
    }

    public String getLieu_event() {
        return lieu_event;
    }

    public void setLieu_event(String lieu_event) {
        this.lieu_event = lieu_event;
    }

    public float getPrix_event() {
        return prix_event;
    }

    public void setPrix_event(float prix_event) {
        this.prix_event = prix_event;
    }

    public float getNbr_personnes() {
        return nbr_personnes;
    }

    public void setNbr_personnes(float nbr_personnes) {
        this.nbr_personnes = nbr_personnes;
    }

    public Date getDate_event() {
        return date_event;
    }

    public void setDate_event(Date date_event) {
        this.date_event = date_event;
    }

    public int getCategorie_event_id() {
        return categorie_event_id;
    }

    public void setCategorie_event_id(int categorie_event_id) {
        this.categorie_event_id = categorie_event_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id_event == event.id_event &&
                Float.compare(prix_event, event.prix_event) == 0 &&
                Float.compare(nbr_personnes, event.nbr_personnes) == 0 &&
                date_event.compareTo(event.date_event) == 0 &&
                categorie_event_id == event.categorie_event_id &&
                Objects.equals(titre_event, event.titre_event) &&
                Objects.equals(description_event, event.description_event) &&
                Objects.equals(etat_event, event.etat_event) &&
                Objects.equals(lieu_event, event.lieu_event);
    }



    @Override
    public int hashCode() {
        return Objects.hash(id_event, titre_event, description_event, etat_event, lieu_event, prix_event, nbr_personnes, date_event, categorie_event_id);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id_event=" + id_event +
                ", titre_event='" + titre_event + '\'' +
                ", description_event='" + description_event + '\'' +
                ", etat_event='" + etat_event + '\'' +
                ", lieu_event='" + lieu_event + '\'' +
                ", prix_event=" + prix_event +
                ", nbr_personnes=" + nbr_personnes +
                ", date_event=" + date_event +
                ", categorie_event_id=" + categorie_event_id +
                '}';
    }
}
