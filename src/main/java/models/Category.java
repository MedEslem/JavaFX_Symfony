package models;

import java.util.Objects;

public class Category {
    private int id_category;
    private String theme;
    private String genre;

    public int getId_category() {
        return id_category;
    }

    public String getTheme() {
        return theme;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id_category == category.id_category && Objects.equals(theme, category.theme) && Objects.equals(genre, category.genre);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id_category, theme, genre);
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    public Category() {
    }

    public Category(int id_category, String theme, String genre) {
        this.id_category = id_category;
        this.theme = theme;
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id_category=" + id_category +
                ", theme='" + theme + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}