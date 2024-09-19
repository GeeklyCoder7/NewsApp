package com.example.newsapp.interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.newsapp.entities.ArticleEntity;

import java.util.List;

@Dao
public interface ArticleDao {
    @Query("select * from articles_table")
    List<ArticleEntity> getArticles();

    @Insert
    void addArticle(ArticleEntity articleEntity);

    @Delete
    void deleteArticle(ArticleEntity entity);
}
