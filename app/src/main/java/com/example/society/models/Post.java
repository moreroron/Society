package com.example.society.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Post {

    public interface Listener<T> {
        void onComplete(T data);
    }

    @PrimaryKey
    @NonNull
     private String postId;
     private String author;
     private String title;
     private String subtitle;
     private int likes;
     private String date;
     private String imageUrl;

    public Post(String postId, String author, String title, String subtitle, int likes, String date, String imageUrl) {
        this.postId = postId;
        this.author = author;
        this.title = title;
        this.subtitle = subtitle;
        this.likes = likes;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    public Post() {}

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}


