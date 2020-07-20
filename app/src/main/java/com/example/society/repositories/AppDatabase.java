package com.example.society.repositories;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.society.SocietyApplication;
import com.example.society.models.Post;
import com.example.society.models.PostDao;

@Database(entities = {Post.class}, version = 17)
abstract class AppDatabaseRepository extends RoomDatabase {
    public abstract PostDao postDao();
}

public class AppDatabase  {
    static public AppDatabaseRepository db = Room.databaseBuilder(SocietyApplication.context, AppDatabaseRepository.class,"dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}

