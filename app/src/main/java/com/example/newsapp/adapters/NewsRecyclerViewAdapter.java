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
import com.example.newsapp.activities.SavedArticlesActivity;
import com.example.newsapp.databases.DatabaseHelper;
import com.example.newsapp.databinding.NewsSampleCardBinding;
import com.example.newsapp.entities.ArticleEntity;
import com.example.newsapp.models.ArticleModel;
import com.example.newsapp.utils.ArrayListConverter;

import java.util.ArrayList;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsRecyclerViewAdapterViewHolder> {

    Context context;
    ArrayList<ArticleModel> articleModelArrayList;


    //A key for accessing the ArticleModel object in the DetailNewsActivity
    public static final String ARTICLE_KEY = "my_article_key";

    //This constructor will be used to set news data fetched from API call
    public NewsRecyclerViewAdapter(Context context, ArrayList<ArticleModel> articleModelArrayList) {
        this.context = context;
        this.articleModelArrayList = articleModelArrayList;
    }

    //Method for handling the articleEntityArraylist and converting it into articleModelArraylist
    public void setArticleEntityArrayList (ArrayList<ArticleEntity> articleEntityArrayList) {
        for (ArticleEntity articleEntity : articleEntityArrayList) {
            articleModelArrayList.add(ArrayListConverter.convertEntityToModel(articleEntity));
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsRecyclerViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsRecyclerViewAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.news_sample_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsRecyclerViewAdapterViewHolder holder, int position) {
        ArticleModel articleModel = articleModelArrayList.get(position);

        //Checking if the article already exists
        DatabaseHelper databaseHelper = DatabaseHelper.getDatabase(context);
        ArticleEntity existingArticle = databaseHelper.articleDao().getArticleByUrl(articleModel.getUrl());

        if (existingArticle != null) {
            changeDesign(holder.binding.saveNewsButton);
        }

        holder.binding.newsHeadlineTextView.setText(articleModel.getTitle());
        if (articleModel.getUrlToImage() == null) {
            holder.binding.newsImageView.setImageResource(R.drawable.news_image_not_available_png);
        } else {
            Glide.with(context).load(articleModel.getUrlToImage()).into(holder.binding.newsImageView);
        }

        holder.binding.newsDescriptionTextView.setText(articleModel.getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailNewsActivity.class);
                intent.putExtra(ARTICLE_KEY, articleModel);
                context.startActivity(intent);
            }
        });

        //Performing save article functionality on button click
        holder.binding.saveNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper = DatabaseHelper.getDatabase(context);

                //If the article already exists, storing it in the object to perform further checks
                ArticleEntity existingArticle = databaseHelper.articleDao().getArticleByUrl(articleModel.getUrl());

                if (existingArticle != null) { //If existingArticle is null it means it already exists.
                    Toast.makeText(context, "Article already saved offline", Toast.LENGTH_SHORT).show();
                } else { //If not null then saved the current article.
                    databaseHelper.articleDao().addArticle(new ArticleEntity(articleModel.getAuthor(),
                            articleModel.getTitle(),
                            articleModel.getDescription(),
                            articleModel.getUrlToImage(),
                            articleModel.getPublishedAt(),
                            articleModel.getContent(),
                            articleModel.getUrl()));
                    changeDesign(holder.binding.saveNewsButton);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleModelArrayList.size();
    }

    //Method for changing the saveButton design
    private void changeDesign(Button saveButton) {
        saveButton.setText("Saved");
        saveButton.setBackgroundColor(ContextCompat.getColor(context, R.color.already_saved_button_bg_color));
    }

    public static class NewsRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {
        NewsSampleCardBinding binding;
        public NewsRecyclerViewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = NewsSampleCardBinding.bind(itemView);
        }
    }
}
