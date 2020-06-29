package com.example.society.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.society.MainActivity;
import com.example.society.models.Post;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

public class PostFirebase {

    final static String POST_COLLECTION = "posts";
    public static final String TAG = MainActivity.class.getName();

    public static void getAllPosts(final Post.Listener<List<Post>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(POST_COLLECTION).get().addOnCompleteListener((new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Post> postsData = new LinkedList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Post post = document.toObject(Post.class);
                        postsData.add(post);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    listener.onComplete(postsData);
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        }));
    }

    public static void addPost(Post post, final Post.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(POST_COLLECTION).document(post.getPostId()).set(post).addOnCompleteListener((new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener != null) {
                    listener.onComplete(task.isSuccessful());
                }
            }
        }));
    }
}























