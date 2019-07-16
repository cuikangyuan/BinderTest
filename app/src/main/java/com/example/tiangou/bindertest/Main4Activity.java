package com.example.tiangou.bindertest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tiangou.bindertest.aidlService.BookManagerService;
import com.example.tiangou.bindertest.parcel_model.Book;

import java.util.List;

//使用AIDL
public class Main4Activity extends AppCompatActivity {

    private static final String TAG = "Main4Activity";

    private static final int MESSAGE_NEW_BOOK_ARRIVED = 3;

    IBookManager iBookManager;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.d(TAG, "handleMessage: receive new book " + msg.obj);

                    Book newBook = (Book) msg.obj;

                    textView.setText(newBook.bookName);

                    break;
                default:
                    super.handleMessage(msg);

            }
        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            Log.d(TAG, "onServiceConnected: ComponentName >>> "+ name + " service >>> " + service);
            
            iBookManager = IBookManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.d(TAG, "onServiceDisconnected: ComponentName >>> " + name);
        }
    };



    private IOnNewBookArrivedListener listener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {

            //执行在客户端的Binder线程池中 所以不能直接更新ui

            Message message  = handler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook);


            handler.sendMessage(message);

        }
    };

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);


        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        textView = findViewById(R.id.text2);


        Intent intent = new Intent(Main4Activity.this, BookManagerService.class);

        bindService(intent, connection, BIND_AUTO_CREATE);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //避免 客户端ANR
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (iBookManager != null) {
                            try {
                                List<Book> bookList = iBookManager.getBookList();

                                Log.d(TAG, "query book list >>> " + bookList.toString());

                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();


            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iBookManager != null) {

                    try {
                        List<Book> bookList1 = iBookManager.getBookList();

                        Log.d(TAG, "before book list >>> " + bookList1.toString());


                        Book newBook = new Book(2, "开发艺术探索");
                        iBookManager.addBook(newBook);

                        List<Book> bookList2 = iBookManager.getBookList();

                        Log.d(TAG, "after book list >>> " + bookList2.toString());

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }


                }
            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iBookManager != null) {


                    try {
                        iBookManager.registerListener(listener);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iBookManager != null) {
                    try {
                        iBookManager.unregisterListener(listener);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


    }

    @Override
    protected void onDestroy() {
        if (iBookManager != null
                && iBookManager.asBinder().isBinderAlive()) {


            Log.d(TAG, "onDestroy: unregister listener " + listener);

            try {
                iBookManager.unregisterListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        unbindService(connection);
        super.onDestroy();
    }
}
