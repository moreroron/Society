package com.example.society.models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PostDao {
    @Query("select * from Post")
    LiveData<List<Post>> getAll();

//    @Query("select * from Post WHERE deleted = 1")
//    LiveData<List<Post>> getAllUndeletedPosts();

    @Query("SELECT * FROM Post WHERE userId=:userId ")
    LiveData<List<Post>> getPostsByUserId(String userId);

    @Query("SELECT * FROM Post WHERE postId=:postId ")
    LiveData<Post> getPostByPostId(String postId);

    @Insert
    void insert(Post post);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... posts);

    @Update
    void update(Post post);

    @Delete
    void delete(Post post);
}
