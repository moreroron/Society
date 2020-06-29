package com.example.society.repositories;

import com.example.society.models.User;

import java.util.ArrayList;

public class UserRepository {

    private static UserRepository instance;
    private ArrayList<User> dataSet = new ArrayList<>();

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }
}
