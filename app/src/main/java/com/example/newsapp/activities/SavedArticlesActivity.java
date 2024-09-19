package com.example.newsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.newsapp.R;
import com.example.newsapp.adapters.NewsRecyclerViewAdapter;
import com.example.newsapp.databases.DatabaseHelper;
import com.example.newsapp.databinding.ActivitySavedArticlesBinding;
import com.example.newsapp.entities.ArticleEntity;
import com.example.newsapp.utils.ArrayListConverter;

import java.util.ArrayList;

public class SavedArticlesActivity extends AppCompatActivity {
    ActivitySavedArticlesBinding binding;
    DatabaseHelper databaseHelper;
    NewsRecyclerViewAdapter newsRecyclerViewAdapter;
    ArrayList<ArticleEntity> articleEntityArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedArticlesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Initializing variables
        databaseHelper = DatabaseHelper.getDatabase(SavedArticlesActivity.this);
        newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(SavedArticlesActivity.this, new ArrayList<>());

        //Setting up the recyclerView for the saved articles
        binding.savedArticlesRecyclerView.setLayoutManager(new LinearLayoutManager(SavedArticlesActivity.this));
        binding.savedArticlesRecyclerView.setAdapter(newsRecyclerViewAdapter);

        //Calling important functions below
        fetchSavedArticles();

        binding.homeScreenIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SavedArticlesActivity.this, MainActivity.class));
            }
        });
    }

    //Method for fetching data from room database
    void fetchSavedArticles() {
        articleEntityArrayList = (ArrayList<ArticleEntity>) databaseHelper.articleDao().getArticles();
        newsRecyclerViewAdapter.setArticleEntityArrayList(articleEntityArrayList);
        newsRecyclerViewAdapter.notifyDataSetChanged();
    }
}