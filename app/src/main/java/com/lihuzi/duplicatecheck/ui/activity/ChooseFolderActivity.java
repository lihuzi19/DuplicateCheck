package com.lihuzi.duplicatecheck.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lihuzi.duplicatecheck.R;
import com.lihuzi.duplicatecheck.ui.adapter.ChooseFolderAdapter;

import java.io.File;

public class ChooseFolderActivity extends AppCompatActivity
{
    private RecyclerView _recyclerView;
    private ChooseFolderAdapter _adapter;
    private File _parentFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_folder);
        initView();
    }

    private void initView()
    {
        _recyclerView = findViewById(R.id.act_choose_folder_recyclerview);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        _adapter = new ChooseFolderAdapter();
        _recyclerView.setAdapter(_adapter);
    }

    private void findChild(File parent)
    {
        File[] childs = _parentFolder.listFiles();
        for (int i = 0; i < childs.length; i++)
        {
            File child = childs[i];
            if (child.isDirectory())
            {

            }
        }
    }
}
