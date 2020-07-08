package com.example.society.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.society.models.Post;
import com.example.society.repositories.PostRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ProfileViewModel extends ViewModel {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private LiveData<List<Post>> posts;

    public LiveData<List<Post>> getPosts() {
        if (posts == null) {
            posts = PostRepository.getInstance().getPostsById(user.getUid());
        }
        return posts;
    }

    public void deletePost(Post post) {
        PostRepository.getInstance().deletePost(post);
    }
}
