package com.lihuzi.duplicatecheck;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class BigFilesActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_files);

        RecyclerView recyclerView = findViewById(R.id.act_big_files_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FileAdapter adapter = new FileAdapter();
        adapter.addList(FileDB.getBigFilesList());
        recyclerView.setAdapter(adapter);
    }
}