package models;

import javafx.scene.image.Image;

import java.sql.Timestamp;
import java.util.Arrays;

public class Message {
    private int idMessage ;
    private String contentMessage;
    private Timestamp TimeStamp_envoi;
    private User sender ;
    private Thread thread;
    private Image image;
    public Message(){}

    public Message(int idMessage, String contentMessage, Timestamp TimeStamp_envoi, User sender, Thread thread, Image image) {
        this.idMessage = idMessage;
        this.contentMessage = contentMessage;
        this.TimeStamp_envoi = TimeStamp_envoi;
        this.sender = sender;
        this.thread = thread;
        this.image = image;
    }
    public Message(int idMessage, String contentMessage, Timestamp TimeStamp_envoi, User sender, Image image) {
        this.idMessage = idMessage;
        this.contentMessage = contentMessage;
        this.TimeStamp_envoi = TimeStamp_envoi;
        this.sender = sender;
        this.image = image;
    }
    public Message(String contentMessage, Timestamp TimeStamp_envoi, User sender, Thread thread, Image image) {
        this.contentMessage = contentMessage;
        this.TimeStamp_envoi = TimeStamp_envoi;
        this.sender = sender;
        this.thread = thread;
        this.image = image;
    }
    public Message(String contentMessage, Timestamp TimeStamp_envoi, User sender, Thread thread) {
        this.contentMessage = contentMessage;
        this.TimeStamp_envoi = TimeStamp_envoi;
        this.sender = sender;
        this.thread = thread;
    }
    public Message(String contentMessage, Timestamp TimeStamp_envoi, User sender) {
        this.contentMessage = contentMessage;
        this.TimeStamp_envoi = TimeStamp_envoi;
        this.sender = sender;
    }
    public Message(int idMessage, String contentMessage, Timestamp TimeStamp_envoi, User sender) {
        this.idMessage = idMessage;
        this.contentMessage = contentMessage;
        this.TimeStamp_envoi = TimeStamp_envoi;
        this.sender = sender;
    }
    public Message(int idMessage, String contentMessage) {
        this.idMessage = idMessage;
        this.contentMessage = contentMessage;

    }

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public void setContentMessage(String contentMessage) {
        this.contentMessage = contentMessage;
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
        return "Message{" +

                ", content='" + contentMessage + '\'' +
                ", TimeStamp_envoi=" + TimeStamp_envoi +
                ", sender=" + sender +
                ", thread=" + thread +

                '}';
    }
}
