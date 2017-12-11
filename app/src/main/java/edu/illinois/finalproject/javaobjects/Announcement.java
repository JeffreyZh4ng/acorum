package edu.illinois.finalproject.javaobjects;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jeffreyzhang on 12/9/17.
 */

public class Announcement {

    private String title;
    private String datePosted;
    private String timePosted;
    private String message;

    public Announcement() {
    }

    public Announcement(String title, String datePosted, String timePosted, String message) {
        this.title = title;
        this.datePosted = datePosted;
        this.timePosted = timePosted;
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

    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
