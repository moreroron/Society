package com.example.society.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.society.models.Post;
import com.example.society.repositories.PostRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UserDetailsViewModel extends ViewModel {
    private LiveData<List<Post>> posts;

    public LiveData<List<Post>> getPosts(String userId ) {
        if (posts == null) {
            posts = PostRepository.getInstance().getPostsById(userId);
        }
        return posts;
    }
}
