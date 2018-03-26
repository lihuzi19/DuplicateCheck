package com.lihuzi.duplicatecheck.utils;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.lihuzi.duplicatecheck.application.DuplicateCheckApplication;

import java.util.UUID;

/**
 * Created by cocav on 2018/3/15.
 */

public class getDiviceId
{
    public static String getDevice()
    {
        phoneModel();
        Log.v("androidId", getAndroidId());
        Log.v("imei", getIMEI());
        String device = "";
        if (!(device = getIMEI()).equals(""))
        {
            return device;
        }
        else if (!(device = getAndroidId()).equals(""))
        {
            return device;
        }
        else
        {
            device = getGUIDInGooglePlay();
        }
        return device;
    }

    public static String getIMEI()
    {
        TelephonyManager mTelephonyMgr = (TelephonyManager) DuplicateCheckApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        if (ContextCompat.checkSelfPermission(DuplicateCheckApplication.getInstance(), Manifest.permission.READ_PHONE_STATE) == 0)
        {
            String imei = mTelephonyMgr.getDeviceId(); //获取IMEI号
            if (imei != null)
            {
                return imei;
            }
        }
        return "";
    }

    public static String getAndroidId()
    {
        String androidId = Settings.System.getString(DuplicateCheckApplication.getInstance().getContentResolver(), Settings.System.ANDROID_ID);
        return androidId != null ? androidId : "";
    }

    public static String getGUIDInGooglePlay()//The end solution
    {
        return UUID.randomUUID().toString();
    }

    private static void phoneModel()
    {
        String model = Build.MODEL;
        String carrier = Build.MANUFACTURER;
        Log.v("model", model);
        Log.v("carrier", carrier);
    }
}
