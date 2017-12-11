package edu.illinois.finalproject.javaobjects;

import java.util.HashMap;

/**
 * Java object that contains a list of announcements
 */
public class AnnouncementList {

    private HashMap<String, Announcement> announcements = new HashMap<>();

    public AnnouncementList() {
    }

    public HashMap<String, Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(HashMap<String, Announcement> announcements) {
        this.announcements = announcements;
    }
}
