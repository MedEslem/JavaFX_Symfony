package models;

import java.util.Objects;

public class Product {
    private int idP;
    private String nomP;
    private String descriptionP;
    private String imageP;
    private float prixP;
    private int idCategorie;
    private int idTag;


    public Product(int idP, String nomP, String descriptionP, String imageP, float prixP, int idCategorie, int idTag) {
        this.idP = idP;
        this.nomP = nomP;
        this.descriptionP = descriptionP;
        this.imageP = imageP;
        this.prixP  = prixP;
        this.idCategorie = idCategorie;
        this.idTag = idTag;
    }

    public Product(String nomP, String descriptionP, String imageP, float prixP, int idCategorie, int idTag) {
        this.nomP = nomP;
        this.descriptionP = descriptionP;
        this.imageP = imageP;
        this.prixP = prixP;
        this.idCategorie = idCategorie;
        this.idTag = idTag;
    }

    public Product() {
    }

    public int getIdP() {
        return idP;
    }

    public void setIdP(int idP) {
        this.idP = idP;
    }

    public String getNomP() {
        return nomP;
    }

    public void setNomP(String nomP) {
        this.nomP = nomP;
    }

    public String getDescriptionP() {
        return descriptionP;
    }

    public void setDescriptionP(String descriptionP) {
        this.descriptionP = descriptionP;
    }

    public String getImageP() {
        return imageP;
    }

    public void setImageP(String imageP) {
        this.imageP = imageP;
    }

    public float getPrixP() {
        return prixP;
    }

    public void setPrixP(float prixP) {
        this.prixP = prixP;
    }

    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    public int getIdTag() {
        return idTag;
    }

    public void setIdTag(int idTag) {
        this.idTag = idTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return idP == product.idP && Float.compare(prixP, product.prixP) == 0 && idCategorie == product.idCategorie && idTag == product.idTag && Objects.equals(nomP, product.nomP) && Objects.equals(descriptionP, product.descriptionP) && Objects.equals(imageP, product.imageP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idP, nomP, descriptionP, imageP, prixP, idCategorie, idTag);
    }

    @Override
    public String toString() {
        return
                "Product Name='" + nomP + '\'' +
                        ", Description='" + descriptionP + '\'' +
                        ", Image='" + imageP + '\'' +
                        ", Price=" + prixP +
                        ", Category=" + idCategorie +
                        ", Tag=" + idTag
                ;
    }
}