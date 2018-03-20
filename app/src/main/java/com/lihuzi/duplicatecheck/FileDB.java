package com.lihuzi.duplicatecheck;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by cocav on 2018/3/14.
 */

public class FileDB
{
    private static String TABLE_NAME = "file_info";

    private static String FILE_ID = "file_id";
    private static String FILE_NAME = "file_name";
    private static String FILE_HASH = "file_hash";
    private static String FILE_PATH = "file_path";
    private static String FILE_LENGTH = "file_length";
    private static String FILE_DUPLICATE_COUNT = "file_duplicate_count";

    private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + //
            FILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +//
            FILE_NAME + " VARCHAR(50) , " +//
            FILE_HASH + " VARCHAR(50) , " + //
            FILE_LENGTH + " INTEGER, " + //
            FILE_DUPLICATE_COUNT + " INTEGER, " + //
            FILE_PATH + " TEXT);";
    private static String CREATE_UNIQUE_INDEX = "CREATE UNIQUE INDEX IF NOT EXISTS file_md5 ON " + TABLE_NAME + " ( " + FILE_HASH + " );";

    private static SQLiteDatabase _DB;

    private static String[] COLUMNS;

    static
    {
        COLUMNS = new String[6];
        COLUMNS[0] = FILE_ID;
        COLUMNS[1] = FILE_NAME;
        COLUMNS[2] = FILE_HASH;
        COLUMNS[3] = FILE_LENGTH;
        COLUMNS[4] = FILE_DUPLICATE_COUNT;
        COLUMNS[5] = FILE_PATH;
    }

    public static void initDB()
    {
        if (_DB == null)
        {
            synchronized (FileDB.class)
            {
                if (_DB == null)
                {
                    _DB = DuplicateCheckApplication.getInstance().openOrCreateDatabase("file_info.db", Context.MODE_PRIVATE, null);
                    _DB.execSQL(CREATE_TABLE);
                    _DB.execSQL(CREATE_UNIQUE_INDEX);
                    Log.v("file_info_db", "db create successful");
                }
            }
        }
    }

    public static synchronized void insertFile(FileModel fileModel)
    {
        initDB();
        long result = -1;
        ContentValues values = new ContentValues();
        values.put(FILE_NAME, fileModel.name);
        values.put(FILE_HASH, fileModel.hash);
        values.put(FILE_PATH, fileModel.path);
        values.put(FILE_LENGTH, fileModel.length);
        values.put(FILE_DUPLICATE_COUNT, fileModel.duplicateCount);
        try
        {
            result = _DB.insert(TABLE_NAME, null, values);
        }
        catch (Exception e)
        {
            //            e.printStackTrace();
        }
        if (result < 0)
        {
            if (getPathByName(fileModel))
            {
                values.put(FILE_PATH, fileModel.path);
                values.put(FILE_DUPLICATE_COUNT, fileModel.duplicateCount);
                try
                {
                    _DB.update(TABLE_NAME, values, FILE_HASH + " =?", new String[]{fileModel.hash});
                    //                    Log.d("duplicateFile", fileModel.toString());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static synchronized boolean getPathByName(FileModel model)
    {
        initDB();
        Cursor cursor = _DB.query(TABLE_NAME, new String[]{FILE_PATH, FILE_DUPLICATE_COUNT}, FILE_HASH + " = ?", new String[]{model.hash}, null, null, null);
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                model.path = cursor.getString(cursor.getColumnIndex(FILE_PATH)) + "," + model.path;
                model.duplicateCount = cursor.getInt(cursor.getColumnIndex(FILE_DUPLICATE_COUNT)) + 1;
            }
            cursor.close();
            return true;
        }
        return false;
    }

    public static synchronized FileModel getFileModel(String hash)
    {
        initDB();
        FileModel model = null;
        Cursor cursor = _DB.query(TABLE_NAME, COLUMNS, FILE_HASH + " =?", new String[]{hash}, null, null, null);
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    model = new FileModel();
                    model._id = cursor.getLong(cursor.getColumnIndex(FILE_ID));
                    model.name = cursor.getString(cursor.getColumnIndex(FILE_NAME));
                    model.hash = cursor.getString(cursor.getColumnIndex(FILE_HASH));
                    model.path = cursor.getString(cursor.getColumnIndex(FILE_PATH));
                    model.length = cursor.getLong(cursor.getColumnIndex(FILE_LENGTH));
                    model.duplicateCount = cursor.getInt(cursor.getColumnIndex(FILE_DUPLICATE_COUNT));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        return model;
    }

    public static synchronized ArrayList<FileModel> getList()
    {
        ArrayList<FileModel> list = new ArrayList<>();
        initDB();
        Cursor cursor = _DB.query(TABLE_NAME, COLUMNS, FILE_DUPLICATE_COUNT + " >?", new String[]{1 + ""}, null, null, FILE_LENGTH + " DESC");
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    FileModel model = new FileModel();
                    model._id = cursor.getLong(cursor.getColumnIndex(FILE_ID));
                    model.name = cursor.getString(cursor.getColumnIndex(FILE_NAME));
                    model.hash = cursor.getString(cursor.getColumnIndex(FILE_HASH));
                    model.path = cursor.getString(cursor.getColumnIndex(FILE_PATH));
                    model.length = cursor.getLong(cursor.getColumnIndex(FILE_LENGTH));
                    model.duplicateCount = cursor.getInt(cursor.getColumnIndex(FILE_DUPLICATE_COUNT));
                    list.add(model);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }

    public static synchronized ArrayList<FileModel> getBigFilesList()
    {
        ArrayList<FileModel> list = new ArrayList<>();
        initDB();
        Cursor cursor = _DB.query(TABLE_NAME, COLUMNS, null, null, null, null, FILE_LENGTH + " DESC");
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    FileModel model = new FileModel();
                    model._id = cursor.getLong(cursor.getColumnIndex(FILE_ID));
                    model.name = cursor.getString(cursor.getColumnIndex(FILE_NAME));
                    model.hash = cursor.getString(cursor.getColumnIndex(FILE_HASH));
                    model.path = cursor.getString(cursor.getColumnIndex(FILE_PATH));
                    model.length = cursor.getLong(cursor.getColumnIndex(FILE_LENGTH));
                    model.duplicateCount = cursor.getInt(cursor.getColumnIndex(FILE_DUPLICATE_COUNT));
                    list.add(model);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }

    public static synchronized void update(FileModel model)
    {
        initDB();
        if (model.path.equals(""))
        {
            delete(model);
        }
        else
        {
            ContentValues values = new ContentValues();
            values.put(FILE_PATH, model.path);
            try
            {
                long result = _DB.update(TABLE_NAME, values, FILE_HASH + " =?", new String[]{model.hash});
                Log.v("FileModel update", result + " ," + model.toString());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static synchronized void delete(FileModel model)
    {
        initDB();
        _DB.delete(TABLE_NAME, FILE_HASH + " =?", new String[]{model.hash});
    }

    public static synchronized void deleteAll()
    {
        initDB();
        _DB.delete(TABLE_NAME, null, null);
    }
}
