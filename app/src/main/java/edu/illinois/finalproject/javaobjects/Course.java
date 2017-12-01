package edu.illinois.finalproject.javaobjects;

import java.util.HashMap;

/**
 * Java object that contains the variables for each course.
 */
public class Course {

    private String university;
    private String key;
    private String term;
    private String year;
    private String section;
    private int enrollment = 1;
    private HashMap<String, String> instructors = new HashMap<>();

    public Course() {
    }

    public Course(String university, String key, String term, String section, String year, String userName, String userKey) {
        this.university = university;
        this.key = key;
        this.section = section;
        this.term = term;
        this.year = year;
        instructors.put(userKey, userName);
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(int enrollment) {
        this.enrollment = enrollment;
    }

    public HashMap<String, String> getInstructors() {
        return instructors;
    }
}
