package com.example.society;

import android.app.Application;
import android.content.Context;

public class SocietyApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
