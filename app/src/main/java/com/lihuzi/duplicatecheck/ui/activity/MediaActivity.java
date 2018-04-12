package com.lihuzi.duplicatecheck.ui.activity;

import com.lihuzi.duplicatecheck.db.FileDB;
import com.lihuzi.duplicatecheck.model.FileBean;

import java.util.ArrayList;

/**
 * Created by cocav on 2018/4/12.
 */

public class MediaActivity extends ModelActivity
{
    @Override
    public void initData()
    {
        System.err.println("MediaActivity initData");
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String[] typeArray = new String[]{"mp3", "ape", "flac", "aac", "wav", "ogg", "amr", "silk"};
                final ArrayList<FileBean> list = new ArrayList<>();
                for (int i = 0; i < typeArray.length; i++)
                {
                    String type = typeArray[i];
                    list.addAll(FileDB.getListWithType(type));
                }
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
