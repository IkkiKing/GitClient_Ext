package com.ikkikingg.gitclient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class BrowseConnection {
    private Context context;


    public BrowseConnection() {
        this.context = context;
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
