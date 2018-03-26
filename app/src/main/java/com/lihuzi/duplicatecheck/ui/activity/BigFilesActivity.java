package com.lihuzi.duplicatecheck.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lihuzi.duplicatecheck.R;
import com.lihuzi.duplicatecheck.ui.adapter.FileBeanAdapter;

public class BigFilesActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_files);

        RecyclerView recyclerView = findViewById(R.id.act_big_files_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FileBeanAdapter a = new FileBeanAdapter();
        a.addList(com.lihuzi.duplicatecheck.db.FileDB.getBigFileList());
        recyclerView.setAdapter(a);
    }
}
