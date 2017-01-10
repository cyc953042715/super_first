package com.example.chaoren.myapplication;

import android.app.Application;

public class AppContext extends Application {

    private static AppContext instance;

    @Override
    public void onCreate() {
        super.onCreate();

        if (instance == null) {
            instance = this;
        }
    }

    public static AppContext getInstance() {
        return instance;
    }
}
