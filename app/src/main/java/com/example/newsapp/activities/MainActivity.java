package com.example.newsapp.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newsapp.R;
import com.example.newsapp.adapters.CategoryRecyclerViewAdapter;
import com.example.newsapp.adapters.NewsRecyclerViewAdapter;
import com.example.newsapp.databinding.ActivityMainBinding;
import com.example.newsapp.databinding.CloseAppCustomDialogLayoutBinding;
import com.example.newsapp.interfaces.RetrofitAPI;
import com.example.newsapp.models.ArticleModel;
import com.example.newsapp.models.CategoryRecyclerViewModel;
import com.example.newsapp.models.NewsModel;
import com.example.newsapp.utils.CustomDialogFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRecyclerViewAdapter.CategoryClickInterface {
    ActivityMainBinding binding;
    ArrayList<CategoryRecyclerViewModel> categoryRecyclerViewModelArrayList;
    CategoryRecyclerViewModel categoryRecyclerViewModel;
    CategoryRecyclerViewAdapter categoryRecyclerViewAdapter;
    NewsRecyclerViewAdapter newsRecyclerViewAdapter;
    ArrayList<ArticleModel> articleModelArrayList;
    String pageNo = "1";
    EditText searchEditText;

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

        searchEditText = binding.newsSearchView.findViewById(androidx.appcompat.R.id.search_src_text);

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
        designEditText(searchEditText);
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
                startActivity(new Intent(MainActivity.this, SavedArticlesActivity.class));
                finish();
            }
        });

        //Performing search operations
        binding.newsSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchNews(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    //Overriding onBackPressed() method to show custom dialog asking permission to close the app
    @Override
    public void onBackPressed() {
        showCustomDialog();
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
    private void fetchNews(String query) {
        articleModelArrayList.clear();
        //Setting progress bar to visible while fetching the news
        binding.newsRecyclerView.setVisibility(View.GONE);
        binding.fetchingNewsProgressBarAnimation.setVisibility(View.VISIBLE);

        String BASE_URL = "https://newsapi.org/";

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        //Calling the Retrofit api
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModel> call;
        call = retrofitAPI.getNewsByCategory(
                query,
                "1",
                "918a046006d744eb9a3780dd93b9ee4d",
                "10"
        );

        //enqueuing the call method and handling the onResponse and onFailure methods
        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                Log.d("Afwan : ", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    NewsModel newsModel = response.body();
                    for (ArticleModel articleModel : newsModel.getArticles()) {
                        if (articleModel != null && articleModel.getTitle() != null && articleModel.getDescription() != null && articleModel.getTitle().length() > 15 && articleModel.getDescription().length() > 15) {
                            articleModelArrayList.add(articleModel);
                        }
                    }
                    setUpNews();
                } else {
                    Toast.makeText(MainActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                }
                binding.fetchingNewsProgressBarAnimation.setVisibility(View.GONE);
                binding.newsRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
                Log.d("Afwan : ", "message");
                t.printStackTrace();
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

    //Method for displaying custom dialog to exit the app
    private void showCustomDialog() {
        CustomDialogFragment customDialogFragment = new CustomDialogFragment();
        customDialogFragment.show(getSupportFragmentManager(), "customDialog");
    }

    //Method for designing the editText inside Textview
    private void designEditText(EditText editText) {
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.search_hint_text_color));
        int editTextPadding = 5;
        searchEditText.setPadding(dpToPx(editTextPadding), dpToPx(editTextPadding), dpToPx(editTextPadding), dpToPx(editTextPadding));
        searchEditText.setTextSize(18);
        searchEditText.setHint("search news");
    }

    //Method to convert dp to px
    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRecyclerViewModelArrayList.get(position).getCategoryName();
        fetchNews(category);
    }
}