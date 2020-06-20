package com.example.society.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    public static final User instance = new User();

    private String mUserId;
    private String mUsername;

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
