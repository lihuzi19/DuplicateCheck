package com.lihuzi.duplicatecheck;

import com.lihuzi.duplicatecheck.utils.FileLengthUtils;

/**
 * Created by cocav on 2018/3/15.
 */

public class FileModel
{
    public long _id;
    public String name;
    public String hash;
    public String path;
    public long length;
    public int duplicateCount;

    @Override
    public String toString()
    {
        return "name:" + name + ",hash:" + hash + ",path:" + path + ",length:" + FileLengthUtils.getLengthStr(length) + ",duplicateCount:" + duplicateCount;
    }
}
