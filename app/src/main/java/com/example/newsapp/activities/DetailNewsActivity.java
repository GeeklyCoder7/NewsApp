package com.example.newsapp.activities;

import static com.example.newsapp.adapters.NewsRecyclerViewAdapter.ARTICLE_MODEL_KEY;
import static com.example.newsapp.adapters.SavedArticlesRecyclerViewAdapter.ARTICLE_ENTITY_KEY;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.newsapp.R;
import com.example.newsapp.databinding.ActivityDetailNewsBinding;
import com.example.newsapp.entities.ArticleEntity;
import com.example.newsapp.models.ArticleModel;

public class DetailNewsActivity extends AppCompatActivity {
    ActivityDetailNewsBinding binding;
    ArticleModel receivedArticleModelObject;
    ArticleEntity receivedArticleEntityObject;

    //Defining the variables for storing the object data receive from the parcelable object.
    String author;
    String title;
    String description;
    String urlToImage;
    String publishedAt;
    String content;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Getting the article model that we received as parcelable object from NewsRecyclerViewAdapter class.
        if (getIntent().hasExtra(ARTICLE_MODEL_KEY)) {
            receivedArticleModelObject = getIntent().getParcelableExtra(ARTICLE_MODEL_KEY);
            if (receivedArticleModelObject != null) {
                populateUsingArticleModel(receivedArticleModelObject);
            }
        } else {
            receivedArticleEntityObject = getIntent().getParcelableExtra(ARTICLE_ENTITY_KEY);
            if (receivedArticleEntityObject != null) {
                populateUsingArticleEntity(receivedArticleEntityObject);
            }
        }

        setDetails(); //Calling this method to populate the views
    }

    //Method for setting up all the views
    @SuppressLint("SetTextI18n")
    private void setDetails() {
        if (urlToImage != null) {
            Glide.with(DetailNewsActivity.this).load(urlToImage).into(binding.newsDetailImageView);
        } else {
            binding.newsDetailImageView.setImageResource(R.drawable.placeholder);
        }
        binding.newsDetailHeadlineTextView.setText(title);
        binding.newsDetailAuthorTextView.setText("Author - " + author);
        binding.newsDetailPublishedAtTextView.setText("Published on - " + publishedAt);
        binding.newsDetailContentTextView.setText(content);
    }

    //Method to populate the variables using ArticleModelObject
    private void populateUsingArticleModel(ArticleModel articleModel) {
        author = articleModel.getAuthor();
        title = articleModel.getTitle();
        description = articleModel.getDescription();
        urlToImage = articleModel.getUrlToImage();
        publishedAt = articleModel.getPublishedAt().substring(0, 10);
        content = articleModel.getContent();
        url = articleModel.getUrl();
    }

    //Method to populate the variables using ArticleEntityObject
    private void populateUsingArticleEntity(ArticleEntity articleEntity) {
        author = articleEntity.getAuthor();
        title = articleEntity.getTitle();
        description = articleEntity.getDescription();
        urlToImage = articleEntity.getUrlToImage();
        publishedAt = articleEntity.getPublishedAt().substring(0, 10);
        content = articleEntity.getContent();
        url = articleEntity.getUrl();
    }
}