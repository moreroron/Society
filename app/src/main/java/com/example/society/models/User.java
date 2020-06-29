package com.example.society.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    public interface Listener<T> {
        void onComplete(T data);
    }

    @PrimaryKey
    @NonNull
    private String userId;
    private String username;
    private String mail;

    public User(String userId, String username, String mail) {
        this.userId = userId;
        this.username = username;
        this.mail = mail;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
