package com.example.society.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Post {

    public interface Listener<T> {
        void onComplete(T data);
    }

    @PrimaryKey
    @NonNull
     private String postId;
     private String userId;
     private String author;
     private String title;
     private String subtitle;
     private String date;
     private String cover;
     private boolean deleted;
     private long lastUpdated;

    public Post(@NonNull String postId, String userId, String author, String title, String subtitle, String date, String cover, boolean deleted) {
        this.postId = postId;
        this.userId = userId;
        this.author = author;
        this.title = title;
        this.subtitle = subtitle;
        this.date = date;
        this.cover = cover;
        this.deleted = deleted;
    }

    public Post() {}

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) { this.cover = cover; }

    public boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public long getLastUpdated() { return lastUpdated; }

    public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }

}


