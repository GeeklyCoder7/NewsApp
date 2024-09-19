package com.example.newsapp.utils;

import com.example.newsapp.entities.ArticleEntity;
import com.example.newsapp.models.ArticleModel;

public class ArrayListConverter {
    public static ArticleModel convertEntityToModel(ArticleEntity articleEntity) {
        return new ArticleModel(
                articleEntity.getAuthor(),
                articleEntity.getTitle(),
                articleEntity.getDescription(),
                articleEntity.getUrlToImage(),
                articleEntity.getPublishedAt(),
                articleEntity.getContent(),
                articleEntity.getUrl()
        );
    }
}
