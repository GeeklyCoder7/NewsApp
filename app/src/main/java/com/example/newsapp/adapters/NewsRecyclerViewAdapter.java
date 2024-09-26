package com.example.newsapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsapp.R;
import com.example.newsapp.activities.DetailNewsActivity;
import com.example.newsapp.databases.DatabaseHelper;
import com.example.newsapp.databinding.NewsSampleCardBinding;
import com.example.newsapp.entities.ArticleEntity;
import com.example.newsapp.models.ArticleModel;
import com.example.newsapp.utils.UtilityMethods;

import java.util.ArrayList;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsRecyclerViewAdapterViewHolder> {

    Context context;
    ArrayList<ArticleModel> articleModelArrayList;
    ArticleEntity existingArticle;
    DatabaseHelper databaseHelper;


    //A key for accessing the ArticleModel object in the DetailNewsActivity
    public static final String ARTICLE_MODEL_KEY = "my_article_model_key";

    public NewsRecyclerViewAdapter(Context context, ArrayList<ArticleModel> articleModelArrayList) {
        this.context = context;
        this.articleModelArrayList = articleModelArrayList;
        databaseHelper = DatabaseHelper.getDatabase(context);
    }

    @NonNull
    @Override
    public NewsRecyclerViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsRecyclerViewAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.news_sample_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsRecyclerViewAdapterViewHolder holder, int position) {
        ArticleModel articleModel = articleModelArrayList.get(position);
        //Checking if the current article already exists (Checking for future processes)
        existingArticle = databaseHelper.articleDao().searchForExistence(articleModel.getUrl());
        if (existingArticle != null) {
            changeButtonDesign(holder.binding.saveNewsButton);
        }

        holder.binding.newsHeadlineTextView.setText(articleModel.getTitle());
        if (articleModel.getUrlToImage() == null || articleModel.getUrlToImage().isEmpty()) {
            holder.binding.newsImageView.setImageResource(R.drawable.news_image_not_available_png);
        } else {
            Glide.with(context).load(articleModel.getUrlToImage()).into(holder.binding.newsImageView);
        }

        holder.binding.newsDescriptionTextView.setText(articleModel.getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailNewsActivity.class);
                intent.putExtra(ARTICLE_MODEL_KEY, articleModel);
                context.startActivity(intent);
            }
        });

        //Performing save article functionality on button click
        holder.binding.saveNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                existingArticle = databaseHelper.articleDao().searchForExistence(articleModel.getUrl());
                if (existingArticle != null) {
                    Toast.makeText(context, "Article already saved.", Toast.LENGTH_SHORT).show();
                } else {
                    databaseHelper.articleDao().addArticle(new ArticleEntity(
                            articleModel.getAuthor(),
                            articleModel.getTitle(),
                            articleModel.getDescription(),
                            articleModel.getUrlToImage(),
                            articleModel.getPublishedAt(),
                            articleModel.getContent(),
                            articleModel.getUrl()
                    ));
                    //Changing the button state after saving to the database
                    changeButtonDesign(holder.binding.saveNewsButton);
                }
            }
        });

        holder.binding.shareNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UtilityMethods.isConnectedToInternet(context)) { //If connected to internet then we will allow to share the article URL
                    UtilityMethods.shareArticleLink(context, articleModel.getUrl());
                } else { //If not connected we wont share the article URL
                    Toast.makeText(context, "No internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleModelArrayList.size();
    }

    //Method for changing button design
    private void changeButtonDesign(Button button) {
        button.setText("Saved");
        button.setBackgroundColor(ContextCompat.getColor(context, R.color.already_saved_button_bg_color));
        button.setTextColor(ContextCompat.getColor(context, R.color.white));
    }

    public static class NewsRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {
        NewsSampleCardBinding binding;
        public NewsRecyclerViewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = NewsSampleCardBinding.bind(itemView);
        }
    }
}
