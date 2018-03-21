package com.lihuzi.duplicatecheck;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.channels.FileChannel;

/**
 * Created by cocav on 2018/3/21.
 */

public class LHZFileUtils
{
    public static void openFile(Activity activity, File file)
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //        File dataFile = new File(DuplicateCheckApplication.getInstance().getFilesDir().getAbsolutePath() + "/test/" + file.getName());
        //        File dataFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/" + file.getName());
        //        copyFileUsingFileChannels(file, dataFile);
        Uri uri = getUriFromFile(file);
        i.setDataAndType(uri, getMimeType(file.getName()));
        activity.startActivity(i);
    }

    public static void copyFileUsingFileChannels(File source, File dest)
    {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try
        {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            inputChannel.close();
            outputChannel.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static Uri getUriFromFile(File f)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            return FileProvider.getUriForFile(DuplicateCheckApplication.getInstance(), "com.lihuzi.duplicatecheck.fileprovider", f);
        }
        else
        {
            return Uri.fromFile(f);
        }
    }

    public static String getMimeType(String filename)
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(filename);
        return type;
    }
}
