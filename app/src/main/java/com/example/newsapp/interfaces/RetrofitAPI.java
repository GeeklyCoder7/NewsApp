package com.example.newsapp.interfaces;

import com.example.newsapp.models.NewsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RetrofitAPI {

    @GET("/v2/everything")
    Call<NewsModel> getNewsByCategory(
            @Query("q") String q,
            @Query("page") String page,
            @Query("apikey") String apikey
    );
}