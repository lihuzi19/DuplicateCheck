package com.lihuzi.duplicatecheck.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lihuzi.duplicatecheck.FileBeanSearchingAdapter;
import com.lihuzi.duplicatecheck.FileDB;
import com.lihuzi.duplicatecheck.MD5Utils;
import com.lihuzi.duplicatecheck.R;
import com.lihuzi.duplicatecheck.SearchingRecyclerAdapter;
import com.lihuzi.duplicatecheck.ShowActivity;
import com.lihuzi.duplicatecheck.utils.getDiviceId;
import com.lihuzi.duplicatecheck.model.FileBean;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity
{
    private static String PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static String PERMISSION_READ_PHONE = Manifest.permission.READ_PHONE_STATE;
    private int dirCount;
    private int fileCount;
    private ExecutorService executorService;
    private AtomicInteger _threadCount;
    private Handler _handler;

    private TextView _searchingTv;
    private RecyclerView _searchingRecyclerView;
    private SearchingRecyclerAdapter _adapter;
    private FileBeanSearchingAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _handler = new Handler();
        initView();
        //        requestGetDeviceId();
    }

    private void initView()
    {
        _searchingTv = findViewById(R.id.act_main_searching);
        _searchingRecyclerView = findViewById(R.id.act_main_searching_recyclerview);
        _searchingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        _adapter = new SearchingRecyclerAdapter();
        adapter = new FileBeanSearchingAdapter();
        _searchingRecyclerView.setAdapter(adapter);
        findViewById(R.id.act_main_start).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startTask();
            }
        });
        findViewById(R.id.act_main_show).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                v.getContext().startActivity(new Intent(v.getContext(), ShowActivity.class));
            }
        });
        findViewById(R.id.act_main_show_big_files).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                v.getContext().startActivity(new Intent(v.getContext(), BigFilesActivity.class));
            }
        });
    }

    private void startTask()
    {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, PERMISSION) == 0)
        {
            initSDCard();
        }
        else
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{PERMISSION}, 100);
        }
    }

    private Runnable searching = new Runnable()
    {
        @Override
        public void run()
        {
            _searchingTv.setText("搜索中...");
        }
    };
    private Runnable close = new Runnable()
    {
        @Override
        public void run()
        {
            if (0 == _threadCount.get())
            {
                _searchingTv.setText("搜索结束！");
                executorService.shutdown();
                Log.v("executorService", "shutdown");
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == 100 && permissions[0].equals(PERMISSION) && grantResults[0] == 0)
        {
            initSDCard();
        }
        if (requestCode == 200 && permissions[0].equals(PERMISSION) && grantResults[0] == 0)
        {
            getDeviceId();
        }
    }

    private void initSDCard()
    {
        initThread();
        _adapter.clear();
        _handler.post(searching);
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                File sdcard = Environment.getExternalStorageDirectory();
                if (sdcard.exists())
                {
                    loopDir(sdcard);
                }
                try
                {
                    int i = 0;
                    while (i != 2)
                    {
                        if (0 == _threadCount.get())
                        {
                            i++;
                        }
                        else
                        {
                            i = 0;
                        }
                        Thread.sleep(1000);
                    }
                    _handler.post(close);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
        //        Log.v("count", "dirCount:" + dirCount + ",fileCount:" + fileCount);
    }

    private void loopDir(File dir)
    {
        File[] files = dir.listFiles();
        if (files == null)
        {
            return;
        }
        for (File f : files)
        {
            if (f.isDirectory())
            {
                dirCount++;
                loopDir(f);
            }
            else
            {
                fileCount++;
                if (f.length() > STANDARD)
                {
                    insertFile(f);
                }
            }
        }
    }

    private static long STANDARD = 1024 * 1024;

    private void insertFile(File f)
    {
        _threadCount.incrementAndGet();
        executorService.execute(new FileRunnable(f));
    }

    class FileRunnable implements Runnable
    {
        private File f;

        public FileRunnable(File f)
        {
            this.f = f;
        }

        @Override
        public void run()
        {
            //            FileModel model = new FileModel();
            //            model.name = f.getName();
            //            model.path = f.getAbsolutePath();
            //            model.hash = MD5Utils.fileToMD5(f);
            //            model.length = f.length();
            //            model.duplicateCount = 1;
            //            FileDB.insertFile(model);
            FileBean fileBean = new FileBean();
            fileBean.name = f.getName();
            fileBean.localPath = f.getAbsolutePath();
            fileBean.hash = MD5Utils.fileToMD5(f);
            fileBean.length = f.length();
            _threadCount.decrementAndGet();
            com.lihuzi.duplicatecheck.db.FileDB.insert(fileBean);
            _handler.post(new NotifyAdapterRunnable(fileBean));
        }
    }

    private void initThread()
    {
        FileDB.deleteAll();
        _threadCount = new AtomicInteger();
        executorService = Executors.newFixedThreadPool(50);
    }

    private void requestGetDeviceId()
    {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, PERMISSION_READ_PHONE) == 0)
        {
            getDeviceId();
        }
        else
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{PERMISSION_READ_PHONE}, 200);
        }
    }

    private void getDeviceId()
    {
        getDiviceId.getDevice();
    }

    class NotifyAdapterRunnable implements Runnable
    {
        private FileBean fileBean;

        public NotifyAdapterRunnable(FileBean f)
        {
            this.fileBean = f;
        }

        @Override
        public void run()
        {
            adapter.add(fileBean);
            _searchingRecyclerView.smoothScrollToPosition(0);
        }
    }
}
