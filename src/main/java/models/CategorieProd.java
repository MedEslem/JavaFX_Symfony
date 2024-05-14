package models;

import java.util.Objects;

public class CategorieProd {
    private int idC;
    private String nomC;

    public CategorieProd(int idC, String nomC, String imageC) {
        this.idC = idC;
        this.nomC = nomC;
        this.imageC = imageC;
    }

    public CategorieProd(String nomC, String imageC) {
        this.nomC = nomC;
        this.imageC = imageC;
    }

    public CategorieProd() {
    }

    public String getNomC() {
        return nomC;
    }

    public void setNomC(String nomC) {
        this.nomC = nomC;
    }

    private String imageC;

    public String getImageC() {
        return imageC;
    }

    public void setImageC(String imageC) {
        this.imageC = imageC;
    }

    public int getIdC() {
        return idC;
    }

    public void setIdC(int idC) {
        this.idC = idC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategorieProd that = (CategorieProd) o;
        return idC == that.idC && Objects.equals(nomC, that.nomC) && Objects.equals(imageC, that.imageC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idC, nomC, imageC);
    }

    @Override
    public String toString() {
        return
                " category name='" + nomC + '\'' +
                        ", image='" + imageC + '\''
                ;
    }
}