package com.example.newsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.newsapp.R;
import com.example.newsapp.adapters.SavedArticlesRecyclerViewAdapter;
import com.example.newsapp.databases.DatabaseHelper;
import com.example.newsapp.databinding.ActivitySavedArticlesBinding;
import com.example.newsapp.entities.ArticleEntity;

import java.util.ArrayList;

public class SavedArticlesActivity extends AppCompatActivity {
    ActivitySavedArticlesBinding binding;
    ArrayList<ArticleEntity> articleEntityArrayList;
    SavedArticlesRecyclerViewAdapter savedArticlesRecyclerViewAdapter;
    DatabaseHelper databaseHelper;
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
        articleEntityArrayList = new ArrayList<>();
        databaseHelper = DatabaseHelper.getDatabase(SavedArticlesActivity.this);

        //Calling important methods below
        fetchSavedArticles();

        binding.goToHomeIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SavedArticlesActivity.this, MainActivity.class));
            }
        });
    }

    //Method for fetching the articles from room database
    private void fetchSavedArticles() {
        articleEntityArrayList = (ArrayList<ArticleEntity>) databaseHelper.articleDao().getArticles();
        setUpSavedArticlesRecyclerView();
    }

    //Method for setting up the saved articles recyclerview
    private void setUpSavedArticlesRecyclerView() {
        savedArticlesRecyclerViewAdapter = new SavedArticlesRecyclerViewAdapter(SavedArticlesActivity.this, articleEntityArrayList);
        binding.savedArticlesRecyclerView.setLayoutManager(new LinearLayoutManager(SavedArticlesActivity.this));
        binding.savedArticlesRecyclerView.setAdapter(savedArticlesRecyclerViewAdapter);
        savedArticlesRecyclerViewAdapter.notifyDataSetChanged();

    }
}