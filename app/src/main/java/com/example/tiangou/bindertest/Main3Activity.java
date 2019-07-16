package com.example.tiangou.bindertest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tiangou.bindertest.messengerService.MessengerService;
import com.example.tiangou.bindertest.common.Constants;

import java.lang.ref.WeakReference;

//使用Messenger
public class Main3Activity extends AppCompatActivity {


    private static final String TAG = "Main3Activity";


    private Messenger messengerFromServer;

    private Button button;

    private TextView textView;


    private Messenger messengerInClient = new Messenger(new MyHandler(Main3Activity.this));

    private void setText(String s) {

        textView.setText(s);

    }


    public static class MyHandler extends android.os.Handler {


        WeakReference<Main3Activity> main3ActivityWeakReference;

        public MyHandler(Main3Activity context) {

            main3ActivityWeakReference = new WeakReference<>(context);

        }
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case Constants.MSG_FROM_SERVER:

                    String s = "receive msg from Server >>> " + msg.getData().getString("msg");

                    if (main3ActivityWeakReference != null) {

                        main3ActivityWeakReference.get().setText(s);
                    }


                    break;

                default:
                    super.handleMessage(msg);
            }

        }
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {


            messengerFromServer = new Messenger(service);



        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        button = findViewById(R.id.button1);

        textView = findViewById(R.id.text);


        Intent intent = new Intent(Main3Activity.this, MessengerService.class);

        boolean b = bindService(intent, serviceConnection, BIND_AUTO_CREATE);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (messengerFromServer != null) {

                    Message message = Message.obtain(null, Constants.MSG_FROM_CLIENT);

                    message.replyTo = messengerInClient;

                    Bundle data = new Bundle();

                    data.putString("msg", "this is client");

                    message.setData(data);

                    try {

                        messengerFromServer.send(message);

                    } catch (RemoteException e) {

                        e.printStackTrace();
                    }
                }

            }
        });

    }


    @Override
    protected void onDestroy() {

        unbindService(serviceConnection);

        super.onDestroy();

    }
}
