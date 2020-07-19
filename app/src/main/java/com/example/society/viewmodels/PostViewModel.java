package com.example.society.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.society.models.Post;
import com.example.society.repositories.PostRepository;

import java.util.List;

public class PostViewModel extends ViewModel {
    private LiveData<List<Post>> posts;

    public LiveData<List<Post>> getAllPosts() {
        if (posts == null) {
            posts = PostRepository.getInstance().getAllPosts();
        }
        return posts;
    }

    public void refresh(PostRepository.Listener listener) {
        PostRepository.getInstance().refreshPostList(listener);
    }
}