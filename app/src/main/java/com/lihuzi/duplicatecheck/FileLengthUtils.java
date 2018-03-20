package com.lihuzi.duplicatecheck;

import java.text.DecimalFormat;

/**
 * Created by cocav on 2018/3/17.
 */

public class FileLengthUtils
{
    private final static DecimalFormat df = new DecimalFormat("0.00");

    private final static float _KB = 1024;
    private final static float _MB = 1024 * _KB;
    private final static float _GB = 1024 * _MB;
    private final static float _TB = 1024 * _GB;
    private final static float _PB = 1024 * _TB;
    //    private final static float _EB = 1024 * _PB;
    //    private final static float _ZB = 1024 * _EB;
    //    private final static float _YB = 1024 * _ZB;
    //    private final static float _BB = 1024 * _YB;
    //    private final static float _NB = 1024 * _BB;

    public static String getLengthStr(long length)
    {
        StringBuilder sb = new StringBuilder();
        if (length < _KB)
        {
            sb.append(length).append(" k");
        }
        else if (length < _MB)
        {
            sb.append(df.format(length / _KB)).append(" k");
        }
        else if (length < _GB)
        {
            sb.append(df.format(length / _MB)).append(" MB");
        }
        else if (length < _TB)
        {
            sb.append(df.format(length / _GB)).append(" GB");
        }
        else if (length < _PB)
        {
            sb.append(df.format(length / _TB)).append(" TB");
        }
        else
        {
            sb.append(df.format(length / _PB)).append(" PB");
        }
        return sb.toString();
    }
}
