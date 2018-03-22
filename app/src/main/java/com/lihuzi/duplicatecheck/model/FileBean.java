package com.lihuzi.duplicatecheck.model;

import com.lihuzi.duplicatecheck.utils.FileLengthUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

/**
 * Created by cocav on 2018/3/22.
 */
@Entity
public class FileBean
{
    @Id(autoincrement = true)
    public Long _id;
    @Property
    public String name;
    @Unique
    public String hash;
    @Convert(columnType = String.class, converter = StringConverter.class)
    public List<String> path;
    @Property
    public long length;
    @Property
    public int duplicateCount;
    @Transient
    public String localPath;

    public FileBean(String name, String hash, List<String> path, long length, int duplicateCount)
    {
        this.name = name;
        this.hash = hash;
        this.path = path;
        this.length = length;
        this.duplicateCount = duplicateCount;
    }

    @Generated(hash = 1712987814)
    public FileBean(Long _id, String name, String hash, List<String> path, long length, int duplicateCount)
    {
        this._id = _id;
        this.name = name;
        this.hash = hash;
        this.path = path;
        this.length = length;
        this.duplicateCount = duplicateCount;
    }

    @Generated(hash = 1910776192)
    public FileBean()
    {
    }

    @Override
    public String toString()
    {
        return "name:" + name + ",hash:" + hash + ",path:" + path + ",length:" + FileLengthUtils.getLengthStr(length) + ",duplicateCount:" + duplicateCount;
    }

    public Long get_id()
    {
        return this._id;
    }

    public void set_id(Long _id)
    {
        this._id = _id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getHash()
    {
        return this.hash;
    }

    public void setHash(String hash)
    {
        this.hash = hash;
    }

    public List<String> getPath()
    {
        return this.path;
    }

    public void setPath(List<String> path)
    {
        this.path = path;
    }

    public long getLength()
    {
        return this.length;
    }

    public void setLength(long length)
    {
        this.length = length;
    }

    public int getDuplicateCount()
    {
        return this.duplicateCount;
    }

    public void setDuplicateCount(int duplicateCount)
    {
        this.duplicateCount = duplicateCount;
    }

}
