package com.example.tiangou.bindertest.messengerService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.example.tiangou.bindertest.common.Constants;

import java.lang.ref.WeakReference;

public class MessengerService extends Service {

    private static final String TAG = "MessengerService";


    private static class MessengerHandler extends Handler {

        private WeakReference<Context> contextWeakReference;

        public MessengerHandler(Context context) {

            contextWeakReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case Constants.MSG_FROM_CLIENT:

                    Log.d(TAG, "receive msg from Client >>> " + msg.getData().getString("msg"));


                    String s = "receive msg from Client >>> " + msg.getData().getString("msg");

                    if (contextWeakReference.get() != null) {

                        Toast.makeText(contextWeakReference.get(), s, Toast.LENGTH_SHORT).show();

                    }

                    Messenger messenger = msg.replyTo;

                    Message replyMessage = Message.obtain(null, Constants.MSG_FROM_SERVER);


                    Bundle bundle = new Bundle();

                    bundle.putString("msg", "server reply msg");

                    replyMessage.setData(bundle);

                    if (messenger != null) {

                        try {
                            messenger.send(replyMessage);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new MessengerHandler(MessengerService.this));

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
