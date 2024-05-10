package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Equipe {

    private int id;
    private String nom_e,logo_e;

    @Override
    public String toString() {
        return "Equipe{" +
                "id=" + id +
                ", nom_e='" + nom_e + '\'' +
                ", logo_e='" + logo_e + '\'' +
                ", competition=" + competition +
                ", nb_p=" + nb_p +
                ", user_id=" + user_id +
                '}';
    }

    public Equipe(int id, String nom_e, String logo_e, Competition competition, int nb_p, int user_id) {
        this.id = id;
        this.nom_e = nom_e;
        this.logo_e = logo_e;
        this.competition = competition;
        this.nb_p = nb_p;
        this.user_id = user_id;
    }

    private Competition competition;
    private int nb_p;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    private int user_id;

    // Autres propriétés et méthodes de la classe



    public Equipe() {
    }



    public int getId() {
        return id;
    }

    public int getNb_p() {
        return nb_p;
    }

    public void setNb_p(int nb_p) {
        this.nb_p = nb_p;
    }

    public String getNom_e() {
        return nom_e;
    }

    public String getLogo_e() {
        return logo_e;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom_e(String nom_e) {
        this.nom_e = nom_e;
    }

    public void setLogo_e(String logo_e) {
        this.logo_e = logo_e;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

}