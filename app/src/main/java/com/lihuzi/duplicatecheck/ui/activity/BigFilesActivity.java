package com.lihuzi.duplicatecheck.ui.activity;

import com.lihuzi.duplicatecheck.db.FileDB;
import com.lihuzi.duplicatecheck.model.FileBean;

import java.util.ArrayList;

public class BigFilesActivity extends ModelActivity
{
    @Override
    public void initData()
    {
        System.err.println("BigFilesActivity initData");
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                final ArrayList<FileBean> list = FileDB.getBigFileList();
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setDataToAdapter(list);
                    }
                });
            }
        }).start();
    }

}
