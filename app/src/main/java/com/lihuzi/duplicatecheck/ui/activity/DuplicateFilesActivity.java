package com.lihuzi.duplicatecheck.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lihuzi.duplicatecheck.R;
import com.lihuzi.duplicatecheck.db.FileDB;
import com.lihuzi.duplicatecheck.model.FileBean;
import com.lihuzi.duplicatecheck.ui.adapter.FileBeanAdapter;

import java.util.ArrayList;

public class DuplicateFilesActivity extends AppCompatActivity
{
    private RecyclerView _recyclerView;
    private FileBeanAdapter _adapter;

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
        _adapter = new FileBeanAdapter();
        _recyclerView.setAdapter(_adapter);
    }

    private void initData()
    {
        ArrayList<FileBean> list = FileDB.getDuplicateList();
        _adapter.addList(list);
    }
}
