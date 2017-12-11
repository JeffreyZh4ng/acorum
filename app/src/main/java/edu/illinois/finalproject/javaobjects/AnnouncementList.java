package edu.illinois.finalproject.javaobjects;

import java.util.HashMap;

/**
 * Created by jeffreyzhang on 12/9/17.
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
    public void deleteAnnouncement (int index) {
        announcements.remove(index);
    }

}
