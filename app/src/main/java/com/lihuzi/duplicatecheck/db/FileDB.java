package com.lihuzi.duplicatecheck.db;

import android.util.Log;

import com.lihuzi.duplicatecheck.DuplicateCheckApplication;
import com.lihuzi.duplicatecheck.model.DaoMaster;
import com.lihuzi.duplicatecheck.model.DaoSession;
import com.lihuzi.duplicatecheck.model.FileBean;
import com.lihuzi.duplicatecheck.model.FileBeanDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cocav on 2018/3/22.
 */

public class FileDB
{
    private static DaoSession _daoSession;

    public static void initDB()
    {
        if (_daoSession == null)
        {
            synchronized (FileDB.class)
            {
                if (_daoSession == null)
                {
                    DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(DuplicateCheckApplication.getInstance(), "fileBean.db", null);
                    _daoSession = new DaoMaster(helper.getWritableDb()).newSession();
                }
            }
        }
    }

    public static ArrayList<FileBean> getDuplicateList()
    {
        initDB();
        ArrayList<FileBean> list = new ArrayList<>();
        List<FileBean> result = _daoSession.queryBuilder(FileBean.class).//
                orderDesc(FileBeanDao.Properties.DuplicateCount).//
                build().//
                list();
        if (result != null && result.size() > 0)
        {
            list.addAll(result);
        }
        return list;
    }

    public static ArrayList<FileBean> getBigFileList()
    {
        initDB();
        ArrayList<FileBean> list = new ArrayList<>();
        List<FileBean> result = _daoSession.queryBuilder(FileBean.class).//
                orderDesc(FileBeanDao.Properties.Length).//
                build().//
                list();
        if (result != null && result.size() > 0)
        {
            list.addAll(result);
        }
        return list;
    }

    public static FileBean getFileBean(String hash)
    {
        initDB();
        FileBean fileBean = _daoSession.queryBuilder(FileBean.class).//
                where(FileBeanDao.Properties.Hash.eq(hash)).//
                build().//
                unique();
        return fileBean;
    }

    public static void insert(FileBean fileBean)
    {
        initDB();
        FileBean file = getFileBean(fileBean.hash);
        if (file == null)
        {
            try
            {
                fileBean.path = new ArrayList<>();
                fileBean.path.add(fileBean.localPath);
                _daoSession.insert(fileBean);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            file.path.add(fileBean.localPath);
            _daoSession.update(file);

        }
        Log.v("fileBean insert", fileBean.toString());
    }

    public static void update(FileBean fileBean)
    {
        initDB();
        if (fileBean.path != null && fileBean.path.size() == 0)
        {
            delete(fileBean);
        }
        else
        {
            _daoSession.update(fileBean);
        }
    }

    public static void delete(FileBean fileBean)
    {
        initDB();
        _daoSession.delete(fileBean);
    }

    public static void deleteAll()
    {
        initDB();
        _daoSession.deleteAll(FileBean.class);
    }
}
