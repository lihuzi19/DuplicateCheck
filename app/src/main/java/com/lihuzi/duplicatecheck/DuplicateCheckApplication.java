package com.lihuzi.duplicatecheck;

import android.app.Application;

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
