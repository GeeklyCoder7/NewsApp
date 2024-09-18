package com.example.newsapp.activities;

import static com.example.newsapp.adapters.NewsRecyclerViewAdapter.ARTICLE_KEY;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.newsapp.R;
import com.example.newsapp.databinding.ActivityDetailNewsBinding;
import com.example.newsapp.models.ArticleModel;

public class DetailNewsActivity extends AppCompatActivity {
    ActivityDetailNewsBinding binding;
    ArticleModel receivedArticleModelObject;

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
        receivedArticleModelObject = getIntent().getParcelableExtra(ARTICLE_KEY);

        //Initializing and setting values in the respective variables.
        if (receivedArticleModelObject != null) {
            author = receivedArticleModelObject.getAuthor();
            title = receivedArticleModelObject.getTitle();
            description = receivedArticleModelObject.getDescription();
            urlToImage = receivedArticleModelObject.getUrlToImage();
            publishedAt = receivedArticleModelObject.getPublishedAt().substring(0, 10);
            content = receivedArticleModelObject.getContent();
            url = receivedArticleModelObject.getUrl();
        }

        setDetails(); //Calling this method to populate the views
    }

    //Method for setting up all the views
    private void setDetails() {
        Glide.with(DetailNewsActivity.this).load(urlToImage).into(binding.newsDetailImageView);
        binding.newsDetailHeadlineTextView.setText(title);
        binding.newsDetailAuthorTextView.setText("Author - " + author);
        binding.newsDetailPublishedAtTextView.setText("Published on - " + publishedAt);
        binding.newsDetailContentTextView.setText(content);
    }
}