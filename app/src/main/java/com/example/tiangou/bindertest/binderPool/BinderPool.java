package com.example.tiangou.bindertest.binderPool;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.tiangou.bindertest.IBinderPool;

import java.util.concurrent.CountDownLatch;

public class BinderPool {

    private static final String TAG = "BinderPool";

    public static final int BINDER_NONE = -1;
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY_CENTER = 1;


    private Context mContext;
    private IBinderPool mBinderPool;
    private static volatile BinderPool mInstance;
    private CountDownLatch mConnectionBinderPoolCountDownLatch;

    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {

            Log.d(TAG, "binderDied: ");

            if (mBinderPool != null) {
                mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
            }
            mBinderPool = null;

            connectBinderPoolService();

        }
    };

    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            Log.d(TAG, "onServiceConnected: ");
            
            mBinderPool = IBinderPool.Stub.asInterface(service);

            try {

                if (mBinderPool != null) {

                    mBinderPool.asBinder().linkToDeath(
                            mBinderPoolDeathRecipient,
                            0);

                }


            } catch (RemoteException e) {
                e.printStackTrace();
            }


            mConnectionBinderPoolCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.d(TAG, "onServiceDisconnected: ");
        }
    };

    private BinderPool(Context context) {

        mContext = context.getApplicationContext();

        Log.d(TAG, "BinderPool: constructor called ");
        
        connectBinderPoolService();

    }


    public IBinder queryBinder(int binderCode) {

        IBinder binder = null;

        if (mBinderPool != null) {

            try {
                binder = mBinderPool.queryBinder(binderCode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return binder;
    }


    private synchronized void connectBinderPoolService() {

        mConnectionBinderPoolCountDownLatch = new CountDownLatch(1);

        Intent intent = new Intent(mContext, BinderPoolService.class);

        mContext.bindService(intent, mBinderPoolConnection, Context.BIND_AUTO_CREATE);

        try {

            mConnectionBinderPoolCountDownLatch.await();

        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    public static BinderPool getInstance(Context context) {

        if (mInstance == null) {

            synchronized (BinderPool.class) {

                if (mInstance == null) {

                    mInstance = new BinderPool(context);
                }

            }

        }

        return mInstance;
    }

    public static class BinderPoolImpl extends IBinderPool.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {

            IBinder binder = null;


            switch (binderCode) {
                case BINDER_SECURITY_CENTER:
                    binder = new SecurityCenterImpl();
                    break;
                case BINDER_COMPUTE:
                    binder = new ComputeImpl();
                    break;
                default:
                    break;
            }

            return binder;
        }
    }

}
