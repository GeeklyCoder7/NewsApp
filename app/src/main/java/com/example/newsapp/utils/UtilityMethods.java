package com.example.newsapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class UtilityMethods {
    //Method for sharing the article url to whatsapp
    public static void shareArticleLink(Context context, String articleUrl) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Read this : \n" + articleUrl);
        context.startActivity(Intent.createChooser(intent, "Share using"));
    }

    //Method to check device internet connection status
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        //Returning values bases on the activeNetworkInfo
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.isConnectedOrConnecting();
        } else {
            return false;
        }
    }
}
