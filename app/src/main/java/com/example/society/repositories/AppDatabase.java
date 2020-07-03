package com.example.society.repositories;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.society.SocietyApplication;
import com.example.society.models.Post;
import com.example.society.models.PostDao;
import com.example.society.models.User;
import com.example.society.models.UserDao;

@Database(entities = {User.class, Post.class}, version = 1)
abstract class AppDatabaseRepository extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract PostDao postDao();
}

public class AppDatabase  {
    static public AppDatabaseRepository db = Room.databaseBuilder(SocietyApplication.context, AppDatabaseRepository.class,"dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
