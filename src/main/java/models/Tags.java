package models;

import java.util.Objects;

public class Tags {
    private int idT;
    private String theme;
    private String genre;

    public int getIdT() {
        return idT;
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
        Tags tags = (Tags) o;
        return idT == tags.idT && Objects.equals(theme, tags.theme) && Objects.equals(genre, tags.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idT, theme, genre);
    }

    public void setIdT(int idT) {
        this.idT = idT;
    }

    public Tags() {
    }

    public Tags(int idT, String theme, String genre) {
        this.idT = idT;
        this.theme = theme;
        this.genre = genre;
    }

    @Override
    public String toString() {
        return
                "Theme='" + theme + '\'' +
                        ", Genre='" + genre + '\''
                ;
    }
}