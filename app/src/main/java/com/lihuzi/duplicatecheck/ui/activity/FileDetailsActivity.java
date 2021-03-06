package com.lihuzi.duplicatecheck.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lihuzi.duplicatecheck.R;
import com.lihuzi.duplicatecheck.ui.adapter.FileDetailsPathAdapter;

public class FileDetailsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_details);

        String hash = getIntent().getStringExtra("hash");
        RecyclerView recyclerView = findViewById(R.id.act_file_details_file_path_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new FileDetailsPathAdapter(hash));
    }
}
