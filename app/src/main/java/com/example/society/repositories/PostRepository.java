package com.example.society.repositories;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.society.adapters.PostAdapter;
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


    public LiveData<List<Post>> getPosts() {
        // get posts from localDb
        LiveData<List<Post>> postsLiveData = AppDatabase.db.postDao().getAll();

        // get posts from api asynchronously
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

        return postsLiveData;
    }

    public LiveData<List<Post>> getPostsById(String userId) {
        // get posts from localDb
        LiveData<List<Post>> postsLiveData = AppDatabase.db.postDao().getPostsByUserId(userId);

        // TODO: do we need a check from api? the data already in localdb
        // get posts from api asynchronously
//        PostFirebase.getPostsByUserId(userId, new Post.Listener<List<Post>>() {
//            @SuppressLint("StaticFieldLeak")
//            @Override
//            public void onComplete(final List<Post> data) {
//                new AsyncTask<String, String, String>() {
//                    @Override
//                    protected String doInBackground(String... strings) {
//                        for(Post post : data) {
//                            AppDatabase.db.postDao().insertAll(post);
//                        }
//                        return "";
//                    }
//                }.execute("");
//            }
//        });
//
        return postsLiveData;
    }

    public void addPost(Post post) {
        PostFirebase.addPost(post, new Post.Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {

            }
        });
    }

    public LiveData<List<Post>> updatePost(Post post) {
        LiveData<List<Post>> postsLiveData = AppDatabase.db.postDao().getAll();
        PostFirebase.updatePost(post, new PostAdapter.Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {

            }
        });
        return postsLiveData;
    }

}
