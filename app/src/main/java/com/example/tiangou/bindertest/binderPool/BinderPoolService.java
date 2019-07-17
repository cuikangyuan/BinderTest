package com.example.tiangou.bindertest.binderPool;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BinderPoolService extends Service {

    private static final String TAG = "BinderPoolService";


    private IBinder mBinderPool = new BinderPool.BinderPoolImpl();

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {

        Log.d(TAG, "onBind: ");

        return mBinderPool;
    }
}
