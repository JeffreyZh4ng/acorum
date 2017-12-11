package edu.illinois.finalproject.javaobjects;

import java.util.HashMap;

/**
 * Java object that contains a list of announcements
 */
public class AnnouncementList {

    private int announcementCount = 0;
    private HashMap<String, Announcement> announcements = new HashMap<>();

    public AnnouncementList() {
    }

    public int getAnnouncementCount() {
        return announcementCount;
    }

    public void setAnnouncementCount(int announcementCount) {
        this.announcementCount = announcementCount;
    }

    public HashMap<String, Announcement> getAnnouncements() {
        return announcements;
    }

    public void addAnnouncement (Announcement announcement) {
        announcements.put((Integer.toString(announcementCount) + "_key"), announcement);
        announcementCount++;
    }

    //Needs work
    public void deleteAnnouncement (String key) {
        announcements.remove(key);
    }

}
