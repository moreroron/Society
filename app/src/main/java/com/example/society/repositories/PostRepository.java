package com.example.society.repositories;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.society.api.PostFirebase;
import com.example.society.models.Post;

import java.util.List;

public class PostRepository {
    private static PostRepository instance;

    public static PostRepository getInstance() {
        if (instance == null) {
            instance = new PostRepository();
        }
        return instance;
    }

    public void refreshPosts() {
        PostFirebase.getAllPosts(new Post.Listener<List<Post>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<Post> data) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        for(Post post : data) {
                            AppDatabase.db.postDao().insertAll(post);
                        }
                        return "";
                    }
                }.execute("");
            }
        });
    }

    public LiveData<List<Post>> getPosts() {
        LiveData<List<Post>> postsLiveData = AppDatabase.db.postDao().getAll(); // get posts from localDb
        refreshPosts(); // get posts from api asynchronously
        return postsLiveData;
    }

    public void addPost(Post post) {
        PostFirebase.addPost(post, new Post.Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {

            }
        });
    }

}
