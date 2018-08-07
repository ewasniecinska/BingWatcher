package com.sniecinska.bingwatcher.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.sniecinska.bingwatcher.R;

/**
 * Created by ewasniecinska on 07.08.2018.
 */

public class InternetConnection extends AsyncTask<Void, Void, Boolean>{
    Context context;
    Snackbar snackbar;

    public InternetConnection(Context context){
        this.context = context;
    }

    protected Boolean doInBackground(Void... params) {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null &&
                    cm.getActiveNetworkInfo().isConnectedOrConnecting();

    }


    protected void onPostExecute(Boolean hasActiveConnection) {
        Log.d(context.getString(R.string.INTERNET_CONNECTION), String.valueOf(hasActiveConnection));
    }
}