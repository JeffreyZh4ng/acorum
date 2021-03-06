package edu.illinois.finalproject.javaobjects;

import java.util.HashMap;

/**
 * Java object that contains the variables for each course.
 */
public class Course {

    private String courseName;
    private String university;
    private String term;
    private String year;
    private String section;
    private String headInstructor;
    private int enrollment = 1;
    private HashMap<String, String> instructors = new HashMap<>();

    public Course() {
    }

    public Course(String courseName, String university, String term, String section, String year, String userName, String userKey) {
        this.courseName = courseName;
        this.university = university;
        this.section = section;
        this.term = term;
        this.year = year;
        this.headInstructor = userName;
        instructors.put(userKey, userName);
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
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

    public String getHeadInstructor() {
        return headInstructor;
    }

    public void setHeadInstructor(String headInstructor) {
        this.headInstructor = headInstructor;
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
