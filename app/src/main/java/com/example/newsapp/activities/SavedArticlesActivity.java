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
import com.example.newsapp.interfaces.OnArticleRemovedListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SavedArticlesActivity extends AppCompatActivity implements OnArticleRemovedListener {
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
                finish();
            }
        });

        binding.deleteAllArticlesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calling deleteAllArticles() method inside ExecutorThread so that UI thread doesn't get affected
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        databaseHelper.articleDao().deleteAllArticles();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onArticleRemoved();
                            }
                        });
                    }
                });
            }
        });
    }

    //Overriding onBackPressed() method to send user back to MainActivity


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SavedArticlesActivity.this, MainActivity.class));
        finish();
    }

    //Method for fetching the articles from room database
    private void fetchSavedArticles() {
        articleEntityArrayList = (ArrayList<ArticleEntity>) databaseHelper.articleDao().getArticles();
        setUpSavedArticlesRecyclerView();
    }

    //Method for setting up the saved articles recyclerview
    private void setUpSavedArticlesRecyclerView() {
        if (articleEntityArrayList.isEmpty()) {
            binding.nothingToShowLinearLayout.setVisibility(View.VISIBLE);
            binding.nestedScrollView.setVisibility(View.GONE);
        } else {
            binding.nestedScrollView.setVisibility(View.VISIBLE);
            binding.nothingToShowLinearLayout.setVisibility(View.GONE);
            savedArticlesRecyclerViewAdapter = new SavedArticlesRecyclerViewAdapter(SavedArticlesActivity.this, articleEntityArrayList, this::onArticleRemoved);
            binding.savedArticlesRecyclerView.setLayoutManager(new LinearLayoutManager(SavedArticlesActivity.this));
            binding.savedArticlesRecyclerView.setAdapter(savedArticlesRecyclerViewAdapter);
        }
    }

    @Override
    public void onArticleRemoved() {
        binding.nothingToShowLinearLayout.setVisibility(View.VISIBLE);
        binding.nestedScrollView.setVisibility(View.GONE);
    }
}