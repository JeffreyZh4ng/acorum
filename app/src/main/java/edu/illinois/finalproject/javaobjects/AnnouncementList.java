package edu.illinois.finalproject.javaobjects;

import java.util.ArrayList;

/**
 * Created by jeffreyzhang on 12/9/17.
 */

public class AnnouncementList {

    private ArrayList<CourseAnnouncement> courseAnnouncements;

    public AnnouncementList() {
    }

    public ArrayList<CourseAnnouncement> getCourseAnnouncements() {
        return courseAnnouncements;
    }

    public void addAnnouncement(CourseAnnouncement courseAnnouncement) {
        courseAnnouncements.add(courseAnnouncement);
    }

    public void removeAnnouncement(CourseAnnouncement courseAnnouncement) {
        courseAnnouncements.remove(courseAnnouncement);
    }
}
