package models;
import java.sql.Time;
import java.util.Date;

public class Competition {

    private int id;
    private String description_c, type_c, jeu_c;
    private Date date_c;
    private Time heure_c;
    private String nom_c;

    public Competition(String description_c, String type_c, String jeu_c,  Date date_c, Time heure_c) {
        this.description_c = description_c;
        this.type_c = type_c;
        this.jeu_c = jeu_c;

        this.date_c = date_c;
        this.heure_c = heure_c;
    }


    public Competition() {
    }

    public Competition(int id, String jeu_c) {
        this.id = id;
        this.jeu_c = jeu_c;
    }

    public Competition(int id, String description_c, String type_c, String jeu_c, Date date_c, Time heure_c) {
        this.id = id;
        this.description_c = description_c;
        this.type_c = type_c;
        this.jeu_c = jeu_c;
        this.date_c = date_c;
        this.heure_c = heure_c;

    }

    public Competition(String description_c, String type_c, String jeu_c, Date date_c, Time heure_c,String nom_c) {
        this.description_c = description_c;
        this.type_c = type_c;
        this.jeu_c = jeu_c;
        this.date_c = date_c;
        this.heure_c = heure_c;
        this.nom_c=nom_c;
    }



    public String getNom_c() {
        return nom_c;
    }

    public void setNom_c(String nom_c) {
        this.nom_c = nom_c;
    }
    public String getDescription_c() {
        return description_c;
    }

    public void setDescription_c(String description_c) {
        this.description_c = description_c;
    }

    public String getType_c() {
        return type_c;
    }

    public void setType_c(String type_c) {
        this.type_c = type_c;
    }

    public Time getHeure_c() {
        return heure_c;
    }

    public void setHeure_c(Time heure_c) {
        this.heure_c = heure_c;
    }

    public Date getDate_c() {
        return date_c;
    }

    public void setDate_c(Date date_c) {
        this.date_c = date_c;
    }

    public String getJeu_c() {
        return jeu_c;
    }

    public void setJeu_c(String jeu_c) {
        this.jeu_c = jeu_c;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Competition{" +
                "id=" + id +
                ", description_c='" + description_c + '\'' +
                ", type_c='" + type_c + '\'' +
                ", nom_c='" + nom_c + '\'' +
                ", jeu_c='" + jeu_c + '\'' +
                ", date_c=" + date_c +
                ", heure_c=" + heure_c +
                '}';
    }
}