package com.example.tiangou.bindertest;

import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.tiangou.bindertest.binderPool.BinderPool;
import com.example.tiangou.bindertest.binderPool.ComputeImpl;
import com.example.tiangou.bindertest.binderPool.SecurityCenterImpl;

import java.util.Random;

//Binder池
public class Main6Activity extends AppCompatActivity {


    private static final String TAG = "Main6Activity";

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);



        button = findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        doWork();

                    }
                }).start();

            }
        });
    }

    private void doWork() {
        BinderPool binderPool = BinderPool.getInstance(Main6Activity.this);

        IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);

        ISecurityCenter iSecurityCenter = SecurityCenterImpl.asInterface(securityBinder);

        String msg = "helloworld";

        Log.d(TAG, "doWork: start >>>>>>>>>>>>>>>>>>> ISecurityCenter" );


        Log.d(TAG, "doWork: original msg >>> " + msg);

        try {
            if (iSecurityCenter != null) {

                String result = iSecurityCenter.encrypt(msg);

                Log.d(TAG, "doWork: encrypted result >>> " + result);


            }


        } catch (RemoteException e) {
            e.printStackTrace();
        }


        IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);


        ICompute iCompute = ComputeImpl.asInterface(computeBinder);

        int a = 1;
        int b = 2;

        Log.d(TAG, "doWork: start >>>>>>>>>>>>>>>>>>> ISecurityCenter" );

        try {

            if (iCompute != null) {
                int add = iCompute.add(a, b);

                Log.d(TAG, "doWork: compute >>> " + a + " + " + b + " = " + add);
            }



        } catch (RemoteException e) {
            e.printStackTrace();
        }




    }


}
