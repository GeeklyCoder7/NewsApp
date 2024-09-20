package com.example.newsapp.interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.newsapp.entities.ArticleEntity;
import com.example.newsapp.models.ArticleModel;

import java.util.List;

@Dao
public interface ArticleDao {
    @Query("SELECT * FROM articles_table")
    List<ArticleEntity> getArticles();

    @Insert
    void addArticle(ArticleEntity articleEntity);

    @Delete
    void deleteArticle(ArticleEntity entity);

    @Query("SELECT * FROM articles_table WHERE url = :url LIMIT 1")
    ArticleEntity searchForExistence(String url);
}