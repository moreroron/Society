package com.example.society.models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PostDao {
    @Query("select * from Post")
    LiveData<List<Post>> getAll();

    @Query("SELECT * FROM Post WHERE userId=:userId ")
    LiveData<List<Post>> getPostsByUserId(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... posts);

    @Delete
    void delete(Post post);
}
