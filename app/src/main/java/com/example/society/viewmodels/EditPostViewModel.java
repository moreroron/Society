package com.example.society.viewmodels;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.society.models.Post;
import com.example.society.models.PostDao;
import com.example.society.repositories.PostRepository;

public class EditPostViewModel extends ViewModel {
    LiveData<Post> postLiveData;

    public LiveData<Post> getPost(String postId) {
        if (postLiveData == null) {
            postLiveData = PostRepository.getInstance().getPostByPostId(postId);
        }
        return postLiveData;
    }

    public void updatePost(Post post) {
        PostRepository.getInstance().updatePost(post);
    }

    public void deletePost(Post post) {
        PostRepository.getInstance().deletePost(post);
    }
}
