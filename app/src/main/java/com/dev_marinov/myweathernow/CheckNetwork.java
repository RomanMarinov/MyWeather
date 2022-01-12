package com.dev_marinov.myweathernow;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

class CheckNetwork { // класс проверки наличия интернета

    private static final String TAG = CheckNetwork.class.getSimpleName();

    public static boolean isInternetAvailable(Context context)
    {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)
        {
           // Toast.makeText(context.getApplicationContext(), "no internet connection checkNetwor", Toast.LENGTH_SHORT).show();
            Log.e(TAG,"no internet connection");
            return false;
        }
        else
        {
            if(info.isConnected())
            {
               // Toast.makeText(context.getApplicationContext(), " internet connection available...", Toast.LENGTH_SHORT).show();
                Log.e(TAG," internet connection available...");
                return true;
            }
            else
            {
                Toast.makeText(context.getApplicationContext(), " internet connection checkNetwor", Toast.LENGTH_SHORT).show();
                Log.d(TAG," internet connection");
                return true;
            }

        }
    }
}
