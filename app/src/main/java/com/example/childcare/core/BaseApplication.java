package com.example.childcare.core;

import android.app.Application;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Prefs.initPrefs(this);
    }
}
