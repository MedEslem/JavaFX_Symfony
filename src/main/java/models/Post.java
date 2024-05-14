package models;

import javafx.scene.image.Image;

import java.sql.Timestamp;
import java.util.Arrays;

public class Post {
    private int idPost ;
    private String contentPost;
    private Timestamp TimeStamp_envoi;
    private User sender ;
    private Thread thread;
    private Image image;
    public Post(){}

    public Post(int idPost, String contentPost, Timestamp TimeStamp_envoi, User sender, Thread thread, Image image) {
        this.idPost = idPost;
        this.contentPost = contentPost;
        this.TimeStamp_envoi = TimeStamp_envoi;
        this.sender = sender;
        this.thread = thread;
        this.image = image;
    }
    public Post(int idPost, String contentPost, Timestamp TimeStamp_envoi, User sender, Image image) {
        this.idPost = idPost;
        this.contentPost = contentPost;
        this.TimeStamp_envoi = TimeStamp_envoi;
        this.sender = sender;
        this.image = image;
    }
    public Post(String contentPost, Timestamp TimeStamp_envoi, User sender, Thread thread, Image image) {
        this.contentPost = contentPost;
        this.TimeStamp_envoi = TimeStamp_envoi;
        this.sender = sender;
        this.thread = thread;
        this.image = image;
    }
    public Post(String contentPost, Timestamp TimeStamp_envoi, User sender, Thread thread) {
        this.contentPost = contentPost;
        this.TimeStamp_envoi = TimeStamp_envoi;
        this.sender = sender;
        this.thread = thread;
    }
    public Post(String contentPost, Timestamp TimeStamp_envoi, User sender) {
        this.contentPost = contentPost;
        this.TimeStamp_envoi = TimeStamp_envoi;
        this.sender = sender;
    }
    public Post(int idPost, String contentPost, Timestamp TimeStamp_envoi, User sender) {
        this.idPost = idPost;
        this.contentPost = contentPost;
        this.TimeStamp_envoi = TimeStamp_envoi;
        this.sender = sender;
    }
    public Post(int idPost, String contentPost) {
        this.idPost = idPost;
        this.contentPost = contentPost;

    }

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }

    public String getContentPost() {
        return contentPost;
    }

    public void setContentPost(String contentPost) {
        this.contentPost = contentPost;
    }

    public Timestamp getTimeStamp_envoi() {
        return TimeStamp_envoi;
    }

    public void setTimeStamp_envoi(Timestamp TimeStamp_envoi) {
        this.TimeStamp_envoi = TimeStamp_envoi;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Post{" +

                ", content='" + contentPost + '\'' +
                ", TimeStamp_envoi=" + TimeStamp_envoi +
                ", sender=" + sender +
                ", thread=" + thread +

                '}';
    }
}
