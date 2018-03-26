package com.lihuzi.duplicatecheck.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lihuzi.duplicatecheck.R;
import com.lihuzi.duplicatecheck.broadcast.LHZBroadcast;
import com.lihuzi.duplicatecheck.broadcast.LHZBroadcastAction;
import com.lihuzi.duplicatecheck.broadcast.LHZBroadcastListener;
import com.lihuzi.duplicatecheck.db.FileDB;
import com.lihuzi.duplicatecheck.model.FileBean;
import com.lihuzi.duplicatecheck.ui.adapter.FileRangeAdapter;

import java.util.ArrayList;

public class FileRangeActivity extends AppCompatActivity
{
    private RecyclerView _recyclerView;
    private TextView _moveFileTv;

    private String _fileType;

    private FileRangeAdapter _adapter;
    private LHZBroadcastListener chooseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_range);
        //        _fileType = getIntent().getStringExtra("mp3");
        _fileType = "mp3";
        initView();
        initData();
        LHZBroadcast.registerBroadcast(LHZBroadcastAction.ACTION_RANGE_CHOOSE, chooseListener);
    }

    @Override
    protected void onDestroy()
    {
        LHZBroadcast.unRegisterBroadcast(LHZBroadcastAction.ACTION_RANGE_CHOOSE, chooseListener);
        super.onDestroy();
    }

    private void initView()
    {
        _recyclerView = findViewById(R.id.act_file_range_recyclerview);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        _adapter = new FileRangeAdapter();
        _recyclerView.setAdapter(_adapter);

        _moveFileTv = findViewById(R.id.act_file_range_move_file_tv);
        _moveFileTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ArrayList<FileBean> list = _adapter.getChooseList();
            }
        });
    }

    private void initData()
    {
        chooseListener = new LHZBroadcastListener()
        {
            @Override
            public void onReceive(Intent i)
            {
                _moveFileTv.setVisibility(View.VISIBLE);
            }
        };
        setAdapter();
    }

    private void setAdapter()
    {
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
