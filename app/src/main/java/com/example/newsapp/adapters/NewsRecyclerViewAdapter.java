package com.example.newsapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsapp.R;
import com.example.newsapp.activities.DetailNewsActivity;
import com.example.newsapp.databinding.NewsSampleCardBinding;
import com.example.newsapp.models.ArticleModel;
import com.example.newsapp.models.NewsModel;

import java.util.ArrayList;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsRecyclerViewAdapterViewHolder> {

    Context context;
    ArrayList<ArticleModel> articleModelArrayList;

    //A key for accessing the ArticleModel object in the DetailNewsActivity
    public static final String ARTICLE_KEY = "my_article_key";

    public NewsRecyclerViewAdapter(Context context, ArrayList<ArticleModel> articleModelArrayList) {
        this.context = context;
        this.articleModelArrayList = articleModelArrayList;
    }

    @NonNull
    @Override
    public NewsRecyclerViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsRecyclerViewAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.news_sample_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsRecyclerViewAdapterViewHolder holder, int position) {
        ArticleModel articleModel = articleModelArrayList.get(position);
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
    }

    @Override
    public int getItemCount() {
        return articleModelArrayList.size();
    }

    public static class NewsRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {
        NewsSampleCardBinding binding;
        public NewsRecyclerViewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = NewsSampleCardBinding.bind(itemView);
        }
    }
}
