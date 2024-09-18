package com.example.newsapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsapp.R;
import com.example.newsapp.databinding.CategoryOptionSampleCardBinding;
import com.example.newsapp.models.ArticleModel;
import com.example.newsapp.models.CategoryRecyclerViewModel;

import java.util.ArrayList;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryRecyclerViewAdapterViewHolder> {
    Context context;
    ArrayList<CategoryRecyclerViewModel> categoryRecyclerViewModelArrayList;
    CategoryClickInterface categoryClickInterface;

    public CategoryRecyclerViewAdapter(Context context, ArrayList<CategoryRecyclerViewModel> categoryRecyclerViewModelArrayList, CategoryClickInterface categoryClickInterface) {
        this.context = context;
        this.categoryRecyclerViewModelArrayList = categoryRecyclerViewModelArrayList;
        this.categoryClickInterface = categoryClickInterface;
    }

    @NonNull
    @Override
    public CategoryRecyclerViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryRecyclerViewAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.category_option_sample_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRecyclerViewAdapterViewHolder holder, int position) {
        CategoryRecyclerViewModel categoryRecyclerViewModel = categoryRecyclerViewModelArrayList.get(position);
        holder.binding.categoryNameTextView.setText(categoryRecyclerViewModel.getCategoryName());
        Glide.with(context).load(categoryRecyclerViewModel.getCategoryImage()).into(holder.binding.categoryImageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryClickInterface.onCategoryClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryRecyclerViewModelArrayList.size();
    }

    public interface CategoryClickInterface {
        void onCategoryClick(int position);
    }

    public static class CategoryRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {
        CategoryOptionSampleCardBinding binding;

        public CategoryRecyclerViewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CategoryOptionSampleCardBinding.bind(itemView);
        }
    }
}
