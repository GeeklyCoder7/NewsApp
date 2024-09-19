package com.example.newsapp.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.newsapp.entities.ArticleEntity;
import com.example.newsapp.interfaces.ArticleDao;

@Database(entities = ArticleEntity.class, exportSchema = false, version = 1)
public abstract class DatabaseHelper extends RoomDatabase {
    private static final String DATABASE_NAME = "articles_database";
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, DatabaseHelper.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    public abstract ArticleDao articleDao();
}
