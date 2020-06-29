package com.example.society.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.society.MainActivity;
import com.example.society.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

public class UserFirebase {

    final static String USER_COLLECTION = "users";
    public static final String TAG = MainActivity.class.getName();

    public static void getAllUsers(final User.Listener<List<User>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USER_COLLECTION).get().addOnCompleteListener((new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<User> usersData = new LinkedList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        usersData.add(user);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    listener.onComplete(usersData);
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        }));
    }

    public static void addUser(User user, final User.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USER_COLLECTION).document(user.getUserId()).set(user).addOnCompleteListener((new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener != null) {
                    listener.onComplete(task.isSuccessful());
                }
            }
        }));
    }

}
