package com.lihuzi.duplicatecheck;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class ShowActivity extends AppCompatActivity
{
    private RecyclerView _recyclerView;
    private FileAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        initFindView();
        initData();
    }

    private void initFindView()
    {
        _recyclerView = findViewById(R.id.act_show_recyclerview);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        _adapter = new FileAdapter();
        _recyclerView.setAdapter(_adapter);
    }

    private void initData()
    {
        ArrayList<FileModel> list = FileDB.getList();
        _adapter.addList(list);
    }
}
