package models;

import java.sql.Timestamp;

public class Thread {
    private int idThread;
    private String titleThread;
    private String descriptionThread;
    private String colorThread;

    private Timestamp TimeStampCreation;
    private User creatorThread;


    public String getColorThread() {
        return colorThread;
    }

    public void setColorThread(String colorThread) {
        this.colorThread = colorThread;
    }

    public String getDescriptionThread() {
        return descriptionThread;
    }

    public void setDescriptionThread(String descriptionThread) {
        this.descriptionThread = descriptionThread;
    }




    public Thread(){}
    public Thread(int idThread, String titleThread, Timestamp TimeStampCreation, User creatorThread) {
        this.idThread = idThread;
        this.titleThread = titleThread;
        this.TimeStampCreation = TimeStampCreation;
        this.creatorThread = creatorThread;
    }
    public Thread(int idThread, String titleThread, Timestamp TimeStampCreation, User creatorThread,String descriptionThread) {
        this.idThread = idThread;
        this.titleThread = titleThread;
        this.TimeStampCreation = TimeStampCreation;
        this.creatorThread = creatorThread;
        this.descriptionThread = descriptionThread;
    }
    public Thread(int idThread, String titleThread,String descriptionThread,String colorThread) {
        this.idThread = idThread;
        this.titleThread = titleThread;
        this.descriptionThread = descriptionThread;
        this.colorThread = colorThread;
    }
    public Thread(String titleThread, Timestamp TimeStampCreation, User creatorThread,String descriptionThread,String colorThread) {
        this.titleThread = titleThread;
        this.TimeStampCreation = TimeStampCreation;
        this.creatorThread = creatorThread;
        this.descriptionThread = descriptionThread;
        this.colorThread=colorThread;
    }
    public Thread(String titleThread, Timestamp TimeStampCreation, User creatorThread,String descriptionThread) {
        this.titleThread = titleThread;
        this.TimeStampCreation = TimeStampCreation;
        this.creatorThread = creatorThread;
        this.descriptionThread = descriptionThread;

    }

    public Thread(int idThread, String titleThread, Timestamp TimeStampCreation, User creatorThread, String descriptionThread, String colorThread) {
        this.idThread = idThread;
        this.titleThread = titleThread;
        this.TimeStampCreation = TimeStampCreation;
        this.creatorThread = creatorThread;
        this.descriptionThread = descriptionThread;
        this.colorThread = colorThread;

    }

    public Thread(int idThread,String titleThread, Timestamp TimeStampCreation) {
        this.idThread = idThread;
        this.titleThread = titleThread;
        this.TimeStampCreation = TimeStampCreation;

    }
    public Thread(int idThread,String titleThread,String descriptionThread) {
        this.idThread = idThread;
        this.titleThread = titleThread;
        this.descriptionThread = descriptionThread;

    }
    public Thread(String titleThread, Timestamp TimeStampCreation,String descriptionThread) {
        this.titleThread = titleThread;
        this.TimeStampCreation = TimeStampCreation;
        this.descriptionThread = descriptionThread;

    }


    public Thread(int idThread,String titleThread) {
        this.idThread = idThread;
        this.titleThread = titleThread;

    }

    public int getIdThread() {
        return idThread;
    }

    public void setIdThread(int idThread) {
        this.idThread = idThread;
    }

    public String getTitleThread() {
        return titleThread;
    }

    public void setTitleThread(String titleThread) {
        this.titleThread = titleThread;
    }

    public Timestamp getTimeStampCreation() {
        return TimeStampCreation;
    }

    public void setTimeStampCreation(Timestamp TimeStampCreation) {
        this.TimeStampCreation = TimeStampCreation;
    }

    public User getCreatorThread() {
        return creatorThread;
    }

    public void setCreatorThread(User creatorThread) {
        this.creatorThread = creatorThread;
    }

    @Override
    public String toString() {
        return "Thread{" +

                ", title='" + titleThread + '\'' +
                ", TimeStampCreation=" + TimeStampCreation +
                ", creator=" + creatorThread +
                '}';
    }
}
