package com.lihuzi.duplicatecheck.db;

import android.util.Log;

import com.lihuzi.duplicatecheck.application.DuplicateCheckApplication;
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
                    //                    Log.v("FileDB", "_daoSession init start" + System.currentTimeMillis());
                    DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(DuplicateCheckApplication.getInstance(), "fileBean-db");
                    _daoSession = new DaoMaster(helper.getWritableDb()).newSession();
                    //                    Log.v("FileDB", "_daoSession init end " + System.currentTimeMillis());
                }
            }
        }
    }

    public static synchronized ArrayList<FileBean> getDuplicateList()
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

    public static synchronized ArrayList<FileBean> getListWithType(String type)
    {
        initDB();
        ArrayList<FileBean> list = new ArrayList<>();
        List<FileBean> result = _daoSession.queryBuilder(FileBean.class).//
                where(FileBeanDao.Properties.Name.like("%" + type)).//
                orderDesc(FileBeanDao.Properties.Length).//
                build().//
                list();
        if (result != null && result.size() > 0)
        {
            list.addAll(result);
        }
        return list;
    }

    public static synchronized ArrayList<FileBean> getBigFileList()
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

    public static synchronized FileBean getFileBean(String hash)
    {
        initDB();
        FileBean fileBean = _daoSession.queryBuilder(FileBean.class).//
                where(FileBeanDao.Properties.Hash.eq(hash)).//
                build().//
                unique();
        return fileBean;
    }

    public static synchronized FileBean insert(FileBean fileBean)
    {
        initDB();
        FileBean file = getFileBean(fileBean.hash);
        if (file == null)
        {
            try
            {
                fileBean.path = new ArrayList<>();
                fileBean.path.add(fileBean.localPath);
                fileBean.duplicateCount++;
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
            file.duplicateCount++;
            _daoSession.update(file);
            fileBean = file;
        }
        Log.v("fileBean insert", fileBean.toString());
        return fileBean;
    }

    public static synchronized void update(FileBean fileBean)
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

    public static synchronized void delete(FileBean fileBean)
    {
        initDB();
        _daoSession.delete(fileBean);
    }

    public static synchronized void deleteAll()
    {
        initDB();
        _daoSession.deleteAll(FileBean.class);
    }
}
