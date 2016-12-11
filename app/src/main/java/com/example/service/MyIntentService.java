package com.example.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Administrator on 2016/12/9.
 */

public class MyIntentService extends IntentService {
    String TAG = getClass().getSimpleName();

    public MyIntentService() {
        super("MyIntentService");
    }

    public MyIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onStartCommand: [开始，耗时任务]");
        long endTime = System.currentTimeMillis() + 20 * 1000;
        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    wait(endTime - System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(TAG, "onStartCommand: [耗时任务,执行结束]");
    }
}
