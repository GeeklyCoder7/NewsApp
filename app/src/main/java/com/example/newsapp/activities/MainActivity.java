package com.example.newsapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newsapp.R;
import com.example.newsapp.adapters.CategoryRecyclerViewAdapter;
import com.example.newsapp.adapters.NewsRecyclerViewAdapter;
import com.example.newsapp.databinding.ActivityMainBinding;
import com.example.newsapp.interfaces.RetrofitAPI;
import com.example.newsapp.models.ArticleModel;
import com.example.newsapp.models.CategoryRecyclerViewModel;
import com.example.newsapp.models.NewsModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRecyclerViewAdapter.CategoryClickInterface{
    ActivityMainBinding binding;
    ArrayList<CategoryRecyclerViewModel> categoryRecyclerViewModelArrayList;
    CategoryRecyclerViewModel categoryRecyclerViewModel;
    CategoryRecyclerViewAdapter categoryRecyclerViewAdapter;
    NewsRecyclerViewAdapter newsRecyclerViewAdapter;
    ArrayList<ArticleModel> articleModelArrayList;
    String pageNo = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Initializing variables and objects here
        categoryRecyclerViewModelArrayList = new ArrayList<>();
        articleModelArrayList = new ArrayList<>();

        //Category
        categoryRecyclerViewAdapter = new CategoryRecyclerViewAdapter(MainActivity.this, categoryRecyclerViewModelArrayList, this::onCategoryClick);
        binding.categoryRecyclerView.setAdapter(categoryRecyclerViewAdapter);

        //News
        newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(MainActivity.this, articleModelArrayList);
        binding.newsRecyclerView.setAdapter(newsRecyclerViewAdapter);

        //Calling important methods here
        fetchCategories();
        fetchNews("All");
        newsRecyclerViewAdapter.notifyDataSetChanged();

        //Setting the functionality for refreshing the page
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (articleModelArrayList != null) {
                    refreshPage();
                }
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });

        //Opening the saved articles activity on icon click
        binding.savedArticlesScreenIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, SavedArticlesActivity.class));
            }
        });
    }

    //Method for setting up the category names
    @SuppressLint("NotifyDataSetChanged")
    private void fetchCategories() {
        categoryRecyclerViewModelArrayList.add(new CategoryRecyclerViewModel(getString(R.string.all_category_name), "https://images.unsplash.com/photo-1495020689067-958852a7765e?q=80&w=869&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRecyclerViewModelArrayList.add(new CategoryRecyclerViewModel(getString(R.string.tech_category_name), "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?q=80&w=870&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRecyclerViewModelArrayList.add(new CategoryRecyclerViewModel(getString(R.string.science_category_name), "https://plus.unsplash.com/premium_photo-1676325101955-1089267548d4?q=80&w=870&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRecyclerViewModelArrayList.add(new CategoryRecyclerViewModel(getString(R.string.sports_category_name), "https://images.unsplash.com/photo-1599474924187-334a4ae5bd3c?q=80&w=783&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRecyclerViewModelArrayList.add(new CategoryRecyclerViewModel(getString(R.string.general_category_name), ""));
        categoryRecyclerViewModelArrayList.add(new CategoryRecyclerViewModel(getString(R.string.business_category_name), "https://plus.unsplash.com/premium_photo-1682309547509-dba279a54939?q=80&w=912&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRecyclerViewModelArrayList.add(new CategoryRecyclerViewModel(getString(R.string.entertainment_category_name), "https://images.unsplash.com/photo-1496337589254-7e19d01cec44?q=80&w=870&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRecyclerViewModelArrayList.add(new CategoryRecyclerViewModel(getString(R.string.health_category_name), "https://plus.unsplash.com/premium_photo-1673953509975-576678fa6710?q=80&w=870&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        setUpCategories();
        categoryRecyclerViewAdapter.notifyDataSetChanged();
    }


    //Method for fetching news
    private void fetchNews(String q) {
        articleModelArrayList.clear();
        //Setting progress bar to visible while fetching the news
        binding.newsRecyclerView.setVisibility(View.GONE);
        binding.fetchingNewsProgressBarAnimation.setVisibility(View.VISIBLE);

        //Different API URLs for fetching data in different forms
        String categoryApiUrl = "https://newsapi.org/v2/everything?q=" + q + "&page=1&apikey=7c62dd4481e04d6e8a9c9b5ecf973603";
        String allNewsApiUrl = "https://newsapi.org/v2/everything?q=all&language=en&excludeDomains=stackoverflow.com&page=1&apikey=7c62dd4481e04d6e8a9c9b5ecf973603";
        String BASE_URL = "https://newsapi.org/";

        //Calling the Retrofit api
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModel> call;
        if (q.equals("All")) {
            call = retrofitAPI.getAllNews(allNewsApiUrl);
        } else {
            call = retrofitAPI.getNewsByCategory(categoryApiUrl);
        }

        //enqueuing the call method and handling the onResponse and onFailure methods
        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                NewsModel newsModel = response.body();
                assert newsModel != null;
                ArrayList<ArticleModel> article = newsModel.getArticles();
                for (int i = 0; i < article.size(); i++) {
                    //Adding a condition to check whether the news thumbnail is null. If it is null, then we won't load that news
                    if (article.get(i).getUrlToImage() != null && !Objects.equals(article.get(i).getUrlToImage(), "")) {
                        articleModelArrayList.add(new ArticleModel(article.get(i).getAuthor(), article.get(i).getTitle(), article.get(i).getDescription(), article.get(i).getUrlToImage(), article.get(i).getPublishedAt(), article.get(i).getContent(), article.get(i).getUrl()));
                    }
                }
                Log.d("data mila1 : ", "" + articleModelArrayList.get(0).getTitle() + articleModelArrayList.get(0).getDescription());
                Log.d("data mila2 : ", "" + articleModelArrayList.get(1).getTitle() + articleModelArrayList.get(1).getDescription());
                setUpNews();
                newsRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to load news!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Method for setting up the category recycler view
    private void setUpCategories() {
        binding.categoryRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        binding.categoryRecyclerView.setAdapter(categoryRecyclerViewAdapter);
    }

    //Method for setting up the news recycler view
    private void setUpNews() {
        binding.newsRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        binding.newsRecyclerView.setAdapter(newsRecyclerViewAdapter);
        binding.newsRecyclerView.setVisibility(View.VISIBLE);
        binding.fetchingNewsProgressBarAnimation.setVisibility(View.GONE);
    }

    //Method for refreshing the page
    private void refreshPage() {
        Collections.shuffle(articleModelArrayList);
        setUpNews();
        newsRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRecyclerViewModelArrayList.get(position).getCategoryName();
        fetchNews(category);
    }
}