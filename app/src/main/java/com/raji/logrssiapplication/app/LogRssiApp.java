package com.raji.logrssiapplication.app;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.crashlytics.android.Crashlytics;
import com.raji.logrssiapplication.db.WifiDb;

import io.fabric.sdk.android.Fabric;

public class LogRssiApp extends Application {
    private static final String DATABASE_NAME = "movies_db";
    public static WifiDb wifiDb;


    public static WifiDb getWifiDb() {
        return wifiDb;
    }

    public static void closeDb() {
        wifiDb = null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());


        wifiDb = Room.databaseBuilder(getApplicationContext(), WifiDb.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
}
