package com.lihuzi.duplicatecheck;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Created by cocav on 2018/3/15.
 */

public class MD5Utils
{
    public static String fileToMD5(File f)
    {
        try
        {
            InputStream is = new FileInputStream(f);
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int len = -1;
            while ((len = is.read(buffer)) != -1)
            {
                digest.update(buffer, 0, len);
            }
            byte[] bytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes)
            {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1)
                {
                    temp = "0" + temp;
                }
                sb.append(temp);
            }
            return sb.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
