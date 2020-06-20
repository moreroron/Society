package com.example.society.model;

import androidx.lifecycle.ViewModel;

import java.util.LinkedList;
import java.util.List;

public class UserListViewModel extends ViewModel {

    List<User> users = new LinkedList<>();

    public List<User> getUsers() {
        return users;
    }
}
