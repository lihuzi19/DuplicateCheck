package com.lihuzi.duplicatecheck.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lihuzi.duplicatecheck.R;
import com.lihuzi.duplicatecheck.broadcast.LHZBroadcast;
import com.lihuzi.duplicatecheck.broadcast.LHZBroadcastAction;
import com.lihuzi.duplicatecheck.broadcast.LHZBroadcastListener;
import com.lihuzi.duplicatecheck.db.FileDB;
import com.lihuzi.duplicatecheck.model.FileBean;
import com.lihuzi.duplicatecheck.ui.adapter.FileRangeAdapter;
import com.lihuzi.duplicatecheck.utils.LHZFileUtils;

import java.io.File;
import java.util.ArrayList;

public class FileRangeActivity extends AppCompatActivity
{
    private RecyclerView _recyclerView;
    private TextView _moveFileTv;
    private TextView _cancelTv;

    private final int REQUEST_CHOOSE_FOLDER = 100;
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
                Intent i = new Intent(FileRangeActivity.this, ChooseFolderActivity.class);
                FileRangeActivity.this.startActivityForResult(i, REQUEST_CHOOSE_FOLDER);
            }
        });

        _cancelTv = findViewById(R.id.act_file_range_cancel_tv);
        _cancelTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                _moveFileTv.setVisibility(View.GONE);
                _cancelTv.setVisibility(View.GONE);
                _adapter.chooseCancel();
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
                _cancelTv.setVisibility(View.VISIBLE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case REQUEST_CHOOSE_FOLDER:
                {
                    if (data.hasExtra("path"))
                    {
                        String path = data.getStringExtra("path");
                        final File f = new File(path);
                        if (f.exists())
                        {
                            final ArrayList<FileBean> list = _adapter.getChooseList();
                            if (list.size() > 0)
                            {
                                new Thread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        try
                                        {
                                            for (int i = 0; i < list.size(); i++)
                                            {
                                                FileBean fileBean = list.get(i);
                                                File source = new File(fileBean.path.get(0));
                                                File dest = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/", fileBean.getName());
                                                File testFolder = dest.getParentFile();
                                                if (!testFolder.exists())
                                                {
                                                    testFolder.mkdir();
                                                }
                                                dest.createNewFile();
                                                LHZFileUtils.copyFileUsingFileChannels(source, dest);
                                                for (String path : fileBean.path)
                                                {
                                                    File p = new File(path);
                                                    if (p.exists())
                                                    {
                                                        p.delete();
                                                    }
                                                }
                                            }
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }

                        }
                    }
                }
                break;
            }
        }
    }
}
