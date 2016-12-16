package com.example.jiji.dataacquisition;

import android.app.Application;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

/**
 * Created by JiJi on 12/16/2016.
 */

public class ServiceApp extends Application {
    private final String TAG = ServiceApp.class.getSimpleName();
    public static Location LOCATION = null;

    @Override
    public void onCreate() {
        Log.d(TAG, "OnCreate");
        Intent location = new Intent(getApplicationContext(), LocationService.class);
        startService(location);
    }
}
