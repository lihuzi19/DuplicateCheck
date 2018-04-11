package com.lihuzi.duplicatecheck.application;

import android.app.Application;

import com.lihuzi.duplicatecheck.broadcast.LHZBroadcast;
import com.lihuzi.duplicatecheck.db.FileDB;

/**
 * Created by cocav on 2018/3/15.
 */

public class DuplicateCheckApplication extends Application
{
    private static Application _instance;

    @Override
    public void onCreate()
    {
        super.onCreate();
        _instance = this;
        LHZBroadcast.init();
        initDB();
    }

    public static Application getInstance()
    {
        return _instance;
    }

    private void initDB()
    {
        FileDB.initDB();
    }
}
