package edu.illinois.finalproject.javaobjects;

/**
 * Java object that contains data for each forum post made
 */
public class ForumResponsePost {

    private String datePosted;
    private String postMessage;
    private String posterKey;

    public ForumResponsePost() {
    }

    public ForumResponsePost(String datePosted, String postMessage, String posterKey) {
        this.datePosted = datePosted;
        this.postMessage = postMessage;
        this.posterKey = posterKey;
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

    public String getPosterKey() {
        return posterKey;
    }

    public void setPosterKey(String posterKey) {
        this.posterKey = posterKey;
    }
}