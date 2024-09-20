package com.example.newsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsapp.R;
import com.example.newsapp.databases.DatabaseHelper;
import com.example.newsapp.databinding.ActivitySavedArticlesBinding;
import com.example.newsapp.databinding.SavedArticlesSampleBinding;
import com.example.newsapp.entities.ArticleEntity;

import java.util.ArrayList;

public class SavedArticlesRecyclerViewAdapter extends RecyclerView.Adapter<SavedArticlesRecyclerViewAdapter.SavedArticlesRecyclerViewAdapterViewHolder> {
    Context context;
    ArrayList<ArticleEntity> articleEntityArrayList;
    DatabaseHelper databaseHelper;

    public SavedArticlesRecyclerViewAdapter(Context context, ArrayList<ArticleEntity> articleEntityArrayList) {
        this.context = context;
        this.articleEntityArrayList = articleEntityArrayList;
        databaseHelper = DatabaseHelper.getDatabase(context);
    }

    @NonNull
    @Override
    public SavedArticlesRecyclerViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SavedArticlesRecyclerViewAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.saved_articles_sample, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SavedArticlesRecyclerViewAdapterViewHolder holder, int position) {
        ArticleEntity articleEntity = articleEntityArrayList.get(position);
        holder.binding.newsHeadlineTextView.setText(articleEntity.getTitle());
        Glide.with(context).load(articleEntity.getUrlToImage()).into(holder.binding.newsImageView);
        holder.binding.newsDescriptionTextView.setText(articleEntity.getDescription());
        holder.binding.removeArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.articleDao().deleteArticle(articleEntity);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleEntityArrayList.size();
    }

    public static class SavedArticlesRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {
        SavedArticlesSampleBinding binding;
        public SavedArticlesRecyclerViewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SavedArticlesSampleBinding.bind(itemView);
        }
    }
}
