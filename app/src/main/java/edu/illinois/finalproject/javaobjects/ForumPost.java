package edu.illinois.finalproject.javaobjects;

/**
 * Java object that contains data for each forum post made
 */
public class ForumPost {

    private String postTitle;
    private String datePosted;
    private String postMessage;

    public ForumPost() {
    }

    public ForumPost(String postTitle, String datePosted, String postMessage) {
        this.postTitle = postTitle;
        this.datePosted = datePosted;
        this.postMessage = postMessage;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getPostMessage() {
        return postMessage;
    }

    public void setPostMessage(String postMessage) {
        this.postMessage = postMessage;
    }
}
