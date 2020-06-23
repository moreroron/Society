package com.example.society.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private String mUserId;
    private String mUsername;
    private String mMail;

    public User(String userId, String email, String username) {
        mUserId = userId;
        mUsername = username;
        mMail = email;
    }

//    public static final User instance = new User();

    public String getUserId() {
        return mUserId;
    }

    public interface Listener<T> {
        void onComplete(T data);
    }

    public void getAllUsers(final User.Listener<List<User>> listener) {
        UserFirebase.getAllUsers(listener);
    }

    public void addUser(User user, final User.Listener<Boolean> listener) {
        UserFirebase.addUser(user, listener);
    }


}
