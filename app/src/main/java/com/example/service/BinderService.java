package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2016/12/9.
 */

public class BinderService extends Service {
    String TAG = getClass().getSimpleName();
    private int count;
    private boolean quit;

    /** 定义onBinder方法需要返回的对象*/
    private MyBinder binder = new MyBinder();
    // 继承Binder实现IBinder类
    public class MyBinder extends Binder {
        public int getCount() {
            Log.d(TAG, "MyBinder getCount: ");
            return count;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return binder;
    }

    // Service第一次进来会被创建（多次startService、bindService不会调用此方法）
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!quit) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                }
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    // 主动unbindService的时候调用
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
