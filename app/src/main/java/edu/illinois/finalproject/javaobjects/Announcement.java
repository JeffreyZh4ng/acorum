package edu.illinois.finalproject.javaobjects;

/**
 * Java object that contains data for each announcement made
 */
public class Announcement {

    private String title;
    private String datePosted;
    private String message;

    public Announcement() {
    }

    public Announcement(String title, String datePosted, String message) {
        this.title = title;
        this.datePosted = datePosted;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
