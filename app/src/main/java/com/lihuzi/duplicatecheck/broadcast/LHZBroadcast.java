package com.lihuzi.duplicatecheck.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.lihuzi.duplicatecheck.application.DuplicateCheckApplication;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by cocav on 2018/3/26.
 */

public class LHZBroadcast
{
    private static final String LOCAL_BROADCAST_ACTION = "com.lihuzi.duplicateapplicadtion";

    private static final String ACTION_NUMBER = "action_number";

    private static final ConcurrentHashMap<Integer, ConcurrentLinkedQueue<LHZBroadcastListener>> _listeners = new ConcurrentHashMap<>();

    public static void init()
    {
        DuplicateCheckBroadcastReceiver receiver = new DuplicateCheckBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(LOCAL_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(DuplicateCheckApplication.getInstance()).registerReceiver(receiver, intentFilter);
    }

    public static void registerBroadcast(int action, LHZBroadcastListener listener)
    {
        ConcurrentLinkedQueue<LHZBroadcastListener> queue = _listeners.get(action);
        if (queue == null)
        {
            queue = new ConcurrentLinkedQueue<>();
        }
        queue.add(listener);
    }

    public static void unRegisterBroadcast(int action, LHZBroadcastListener listener)
    {
        ConcurrentLinkedQueue<LHZBroadcastListener> queue = _listeners.get(action);
        if (queue != null)
        {
            queue.remove(listener);
            if (queue.isEmpty())
            {
                Log.v("LHZBroadcast", "queue is empty");
                _listeners.remove(action);
            }
        }
    }

    public static void sendBroadcast(int action, Intent i)
    {
        if (i == null)
        {
            i = new Intent();
        }
        i.putExtra(ACTION_NUMBER, action);
        LocalBroadcastManager.getInstance(DuplicateCheckApplication.getInstance()).sendBroadcast(i);
    }

    private static void onReceive(Intent intent)
    {
        if (intent.hasExtra(ACTION_NUMBER))
        {
            int action = intent.getIntExtra(ACTION_NUMBER, -1);
            if (action != -1)
            {
                intent.removeExtra(ACTION_NUMBER);
                ConcurrentLinkedQueue<LHZBroadcastListener> queue = _listeners.get(action);
                if (queue != null)
                {
                    for (LHZBroadcastListener listener : queue)
                    {
                        listener.onReceive(intent);
                    }
                }
            }
        }
    }

    private static class DuplicateCheckBroadcastReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            LHZBroadcast.onReceive(intent);
        }
    }

}
