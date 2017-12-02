package edu.illinois.finalproject.javaobjects;

import java.util.HashMap;

/**
 * Java object that contains the variables that each user contains.
 */
public class UserInformation {

    private String firstName;
    private String lastName;
    private String email;
    private HashMap<String, Boolean> enrolledCourses = new HashMap<>();

    public UserInformation() {
    }

    public UserInformation(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        //enrolledCourses.put("Placeholder Class", false);
    }

    public void addCourse(String course) {
        enrolledCourses.put(course, true);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<String, Boolean> getEnrolledCourses() {
        return enrolledCourses;
    }
}
