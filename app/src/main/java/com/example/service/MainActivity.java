package com.example.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * 用startService的service生命周期：onCreate->onStartCommand
 * 用bindService的service生命周期：onCreate->onBind->访问者MainActivity中的onServiceConnected
 *
 * 1.startService（访问者退出了，Service任然运行）和bindService（访问者退出了，Service跟随着终止）区别
 * 2.生命周期问题：startService多次，Service中onCreate只在第一次会运行，onStartCommand运行一次startService都会被调用
 * 注意：bindService的时候，onStartCommand不会被调用，多次bindService时，onCreate也只会调用一次。
 * 3.在Service中实现MyBinder内部类作用。（好比一个钩子，用于和访问者通信的）
 * 4.ServiceConnetion中两个方法的作用。注意：其中onServiceDisconnected，只有异常中断才会调用。（主动unBindService结束不会调用）
 * 5.先startService->再bindService->unbindService->stopService：这么运行，Service的生命周期是怎么样的
 *
 * 原理总结：Service中的onBind方法会返回一个IBinder对象，并将IBinder传给ServiceConnection对象的onServiceConnected中的IBinder参数，
 * 这样就可以通过IBinder对象和Service进行通信了。
 */
public class MainActivity extends AppCompatActivity {
    String TAG = getClass().getSimpleName();
    BinderService.MyBinder binder;
    /**新建的Service连接对象*/
    private ServiceConnection conn = new ServiceConnection() {
        //Activity和Service连接成功的时候调用
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: ");
            binder = (BinderService.MyBinder) iBinder;
        }

        //Activity和Service异常断开的时候会调用（主动unbindService不会调用）
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = new Intent(this, BinderService.class);

        findViewById(R.id.btn_bindservice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bindService(intent, conn, Service.BIND_AUTO_CREATE);
            }
        });

        findViewById(R.id.btn_unbindservice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unbindService(conn);//注意：不会调用onServiceDisconnected方法
            }
        });

        findViewById(R.id.btn_getservicestatus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Service的count值为："+binder.getCount(), Toast.LENGTH_SHORT).show();
            }
        });

        //下面是用于测试startServie和stopService的
        findViewById(R.id.btn_startservice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(intent);
            }
        });

        findViewById(R.id.btn_stoptservice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(intent);
            }
        });

        findViewById(R.id.btn_testservice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(MainActivity.this, MyService.class));
            }
        });

        findViewById(R.id.btn_testIntentService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(MainActivity.this, MyIntentService.class));
            }
        });
    }
}
