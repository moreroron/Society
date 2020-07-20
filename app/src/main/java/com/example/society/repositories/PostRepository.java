package com.example.society.repositories;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.example.society.MainActivity;
import com.example.society.SocietyApplication;
import com.example.society.adapters.PostAdapter;
import com.example.society.api.PostFirebase;
import com.example.society.models.Post;
import com.example.society.models.PostDao;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class PostRepository {

    public interface Listener {
        void onComplete();
    }

    private static PostRepository instance;

    public static PostRepository getInstance() {
        if (instance == null) {
            instance = new PostRepository();
        }
        return instance;
    }

    public LiveData<List<Post>> getAllPosts() {
        LiveData<List<Post>> postsLiveData = AppDatabase.db.postDao().getAll();
        refreshPostList(null);
        return postsLiveData;
    }

    public void refreshPostList(final Listener listener) {
        final SharedPreferences postTimestamp = SocietyApplication.context.getSharedPreferences("lastUpdated", MODE_PRIVATE);
        long lastUpdated = postTimestamp.getLong("PostsLastUpdateDate", 0);

        PostFirebase.getAllPostsSince(lastUpdated, new Post.Listener<List<Post>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<Post> data) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        long lastUpdated = 0;
                        for(Post post : data) {
                            if (post.getDeleted()) {
                                AppDatabase.db.postDao().delete(post);
                            } else {
                                AppDatabase.db.postDao().insertAll(post);
                            }
                            if (post.getLastUpdated() > lastUpdated) {
                                lastUpdated = post.getLastUpdated();
                            }
                        }

                        SharedPreferences.Editor edit = postTimestamp.edit();
                        edit.putLong("PostsLastUpdateDate", lastUpdated);
                        edit.apply();
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (listener != null) listener.onComplete();
                    }
                }.execute("");
            }
        });
    }

    public LiveData<Post> getPostByPostId(String postId) {
        return AppDatabase.db.postDao().getPostByPostId(postId);
    }

    public LiveData<List<Post>> getPostsById(String userId) {
        return AppDatabase.db.postDao().getPostsByUserId(userId);
    }

    public void addPost(Post post) {
        new InsertAsyncTask(AppDatabase.db.postDao()).execute(post);
        PostFirebase.addPost(post, new Post.Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {}
        });
    }

    public void updatePost(Post post) {
        new UpdateAsyncTask(AppDatabase.db.postDao()).execute(post);
        PostFirebase.updatePost(post, new PostAdapter.Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {}
        });
    }

    public void deletePost(Post post) {
        new DeleteAsyncTask(AppDatabase.db.postDao()).execute(post);
        PostFirebase.deletePost(post);
    }

    // async tasks

    private static class InsertAsyncTask extends android.os.AsyncTask<Post, Void, Void> {
        PostDao postDao;
        InsertAsyncTask(PostDao postDao) {
            this.postDao = postDao;
        }
        @Override
        protected Void doInBackground(Post... posts) {
            postDao.insert(posts[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Post, Void, Void> {
        PostDao postDao;
        UpdateAsyncTask(PostDao postDao) {
            this.postDao = postDao;
        }
        @Override
        protected Void doInBackground(Post... posts) {
            postDao.update(posts[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Post, Void, Void> {
        PostDao postDao;
        DeleteAsyncTask(PostDao postDao) {
            this.postDao = postDao;
        }
        @Override
        protected Void doInBackground(Post... posts) {
            postDao.delete(posts[0]);
            return null;
        }
    }

}
