package com.bkdn.nqminh.hearingsaver.application;

import android.app.Application;
import android.content.Context;

public class HearingSaver extends Application {
//    private static Context context;

    @Override
    public void onCreate() {
//        ACRA.init(this);
        super.onCreate();
//        HearingSaver.context = getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        ACRA.init(this);
    }

//    public static Context getAppContext() {
//        return HearingSaver.context;
//    }
}
