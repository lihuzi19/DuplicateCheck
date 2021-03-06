package com.lihuzi.duplicatecheck.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.lihuzi.duplicatecheck.utils.LHZFileUtils;

import java.io.File;
import java.util.ArrayList;

public abstract class ModelActivity extends AppCompatActivity
{
    public abstract void initData();

    //    private void initDataDemo()
    //    {
    //        System.err.println("ModelActivity initData");
    //        new Thread(new Runnable()
    //        {
    //            @Override
    //            public void run()
    //            {
    //                String[] typeArray = new String[]{"mp3", "ape", "flac", "aac", "wav", "ogg", "amr", "silk"};
    //                final ArrayList<FileBean> list = new ArrayList<>();
    //                for (int i = 0; i < typeArray.length; i++)
    //                {
    //                    String type = typeArray[i];
    //                    list.addAll(FileDB.getListWithType(type));
    //                }
    //                runOnUiThread(new Runnable()
    //                {
    //                    @Override
    //                    public void run()
    //                    {
    //                        _adapter.setData(list);
    //                    }
    //                });
    //            }
    //        }).start();
    //    }

    private RecyclerView _recyclerView;
    private TextView _moveFileTv;
    private TextView _cancelTv;
    private ProgressDialog _progressDialog;

    private final int REQUEST_CHOOSE_FOLDER = 100;
    private ArrayList<FileBean> _list;

    private FileRangeAdapter _adapter;

    private LHZBroadcastListener chooseListener = new LHZBroadcastListener()
    {
        @Override
        public void onReceive(Intent i)
        {
            _moveFileTv.setVisibility(View.VISIBLE);
            _cancelTv.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        init();
        LHZBroadcast.registerBroadcast(LHZBroadcastAction.ACTION_RANGE_CHOOSE, chooseListener);
    }

    public void setDataToAdapter(ArrayList<FileBean> list)
    {
        _list = list;
        if (_adapter != null)
        {
            _adapter.setData(list);
        }
    }

    private void init()
    {
        _progressDialog = new ProgressDialog(this);
        initView();
        initData();
    }

    private void initView()
    {
        _recyclerView = findViewById(R.id.act_file_range_recyclerview);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        _adapter = new FileRangeAdapter();
        _recyclerView.setAdapter(_adapter);
        if (_list != null && _list.size() > 0)
        {
            _adapter.setData(_list);
        }

        _moveFileTv = findViewById(R.id.act_file_range_move_file_tv);
        _moveFileTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(v.getContext(), ChooseFolderActivity.class);
                startActivityForResult(i, REQUEST_CHOOSE_FOLDER);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
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
                                _progressDialog.show();
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
                                                File dest = new File(f, fileBean.getName());
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
                                                fileBean.path.clear();
                                                fileBean.path.add(dest.getAbsolutePath());
                                                FileDB.update(fileBean);
                                            }
                                            _recyclerView.post(new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    _cancelTv.performClick();
                                                    _progressDialog.dismiss();
                                                    initData();
                                                }
                                            });
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

    @Override
    public void onBackPressed()
    {
        if (!_adapter.chooseCancel())
        {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        LHZBroadcast.unRegisterBroadcast(LHZBroadcastAction.ACTION_RANGE_CHOOSE, chooseListener);
    }


}
