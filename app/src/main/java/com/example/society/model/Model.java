package com.example.society.model;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;
import java.util.List;

public class Model {

    public static Model instance = new Model();
    private List<User> users = new LinkedList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Model() {}



}
