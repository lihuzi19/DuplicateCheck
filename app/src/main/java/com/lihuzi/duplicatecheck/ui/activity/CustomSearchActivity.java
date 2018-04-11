package com.lihuzi.duplicatecheck.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lihuzi.duplicatecheck.R;
import com.lihuzi.duplicatecheck.db.FileDB;
import com.lihuzi.duplicatecheck.model.FileBean;
import com.lihuzi.duplicatecheck.ui.adapter.CustomSearchAdapter;

import java.util.ArrayList;

public class CustomSearchActivity extends AppCompatActivity
{
    private RecyclerView _recyclerView;
    private CustomSearchAdapter _adapter;
    private String _fileExtension;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_search);
        initView();
        initData();
    }

    private void initView()
    {
        _recyclerView = findViewById(R.id.act_custom_search_recyclerview);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        _adapter = new CustomSearchAdapter();
        _recyclerView.setAdapter(_adapter);
    }

    private void initData()
    {
        _fileExtension = getIntent().getStringExtra("file_extension");
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
                _recyclerView.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        _adapter.setData(list);
                    }
                });
            }
        }).start();
    }
}
