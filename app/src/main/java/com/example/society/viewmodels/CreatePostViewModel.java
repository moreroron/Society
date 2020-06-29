package com.example.society.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.society.models.Post;
import com.example.society.repositories.PostRepository;

public class CreatePostViewModel extends ViewModel {

    public void addPost(Post post) {
        PostRepository.getInstance().addPost(post);
    }
}
