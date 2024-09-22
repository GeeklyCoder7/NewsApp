package com.example.newsapp.adapters;

import android.annotation.SuppressLint;
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
import com.example.newsapp.activities.SavedArticlesActivity;
import com.example.newsapp.databases.DatabaseHelper;
import com.example.newsapp.databinding.ActivitySavedArticlesBinding;
import com.example.newsapp.databinding.SavedArticlesSampleBinding;
import com.example.newsapp.entities.ArticleEntity;
import com.example.newsapp.interfaces.OnArticleRemovedListener;

import java.util.ArrayList;

public class SavedArticlesRecyclerViewAdapter extends RecyclerView.Adapter<SavedArticlesRecyclerViewAdapter.SavedArticlesRecyclerViewAdapterViewHolder> {
    Context context;
    ArrayList<ArticleEntity> articleEntityArrayList;
    DatabaseHelper databaseHelper;
    OnArticleRemovedListener listener;
    public static final String ARTICLE_ENTITY_KEY = "my_article_entity_key";

    public SavedArticlesRecyclerViewAdapter(Context context, ArrayList<ArticleEntity> articleEntityArrayList, OnArticleRemovedListener listener) {
        this.context = context;
        this.articleEntityArrayList = articleEntityArrayList;
        databaseHelper = DatabaseHelper.getDatabase(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public SavedArticlesRecyclerViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SavedArticlesRecyclerViewAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.saved_articles_sample, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SavedArticlesRecyclerViewAdapterViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ArticleEntity articleEntity = articleEntityArrayList.get(position);
        holder.binding.newsHeadlineTextView.setText(articleEntity.getTitle());
        if (articleEntity.getUrlToImage() == null) {
            Glide.with(context).load(R.drawable.placeholder).into(holder.binding.newsImageView);
        } else {
            Glide.with(context).load(articleEntity.getUrlToImage()).into(holder.binding.newsImageView);
        }
        holder.binding.newsDescriptionTextView.setText(articleEntity.getDescription());
        holder.binding.removeArticleButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                databaseHelper.articleDao().deleteArticle(articleEntity);
                articleEntityArrayList.remove(position);
                if (articleEntityArrayList.isEmpty()) {
                    if (listener != null) {
                        listener.onArticleRemoved();
                    }
                }
                notifyDataSetChanged();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailNewsActivity.class);
                intent.putExtra(ARTICLE_ENTITY_KEY, articleEntity);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleEntityArrayList.size();
    }

    //Interface for handling the article remove functions
    public interface onArticleRemovedListener {
        void onArticleRemoved();
    }

    public static class SavedArticlesRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {
        SavedArticlesSampleBinding binding;
        public SavedArticlesRecyclerViewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SavedArticlesSampleBinding.bind(itemView);
        }
    }
}
