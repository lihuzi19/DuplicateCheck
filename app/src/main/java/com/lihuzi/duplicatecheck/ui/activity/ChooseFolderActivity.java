package com.lihuzi.duplicatecheck.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lihuzi.duplicatecheck.R;
import com.lihuzi.duplicatecheck.ui.adapter.ChooseFolderAdapter;
import com.lihuzi.duplicatecheck.utils.LHZCallback;

import java.io.File;
import java.util.ArrayList;

public class ChooseFolderActivity extends AppCompatActivity
{
    private TextView _chooseTv;
    private RecyclerView _recyclerView;
    private ChooseFolderAdapter _adapter;
    private File _parentFolder;
    private String sdcardPath;
    private LHZCallback<String> _callback;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_folder);
        _callback = new LHZCallback<String>()
        {
            @Override
            public void callback(String s)
            {
                File f = new File(s);
                if (f.exists())
                {
                    findChild(f);
                }
            }
        };
        initView();
        sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        findChild(Environment.getExternalStorageDirectory());
    }

    private void initView()
    {
        _chooseTv = findViewById(R.id.act_choose_folder_choose_tv);
        _chooseTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent();
                String path;
                if (_adapter.getChoosePath() != null)
                {
                    path = _adapter.getChoosePath();
                }
                else
                {
                    path = _parentFolder.getAbsolutePath();
                }
                i.putExtra("path", path);
                setResult(RESULT_OK, i);
                finish();
            }
        });
        _recyclerView = findViewById(R.id.act_choose_folder_recyclerview);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        _adapter = new ChooseFolderAdapter();
        _recyclerView.setAdapter(_adapter);
        _adapter.setCallback(_callback);
    }

    private void findChild(File parent)
    {
        ArrayList<String> paths = new ArrayList<>();
        _parentFolder = parent;
        File[] childs = _parentFolder.listFiles();
        for (int i = 0; i < childs.length; i++)
        {
            File child = childs[i];
            if (child.isDirectory())
            {
                paths.add(child.getAbsolutePath());
            }
        }
        _adapter.setData(paths);
    }

    @Override
    public void onBackPressed()
    {
        if (!_parentFolder.getAbsolutePath().equals(sdcardPath))
        {
            File f = _parentFolder;
            if (f.exists())
            {
                findChild(f.getParentFile());
            }
        }
        else
        {
            super.onBackPressed();
        }
    }
}
