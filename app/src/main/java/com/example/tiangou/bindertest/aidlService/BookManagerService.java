package com.example.tiangou.bindertest.aidlService;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.example.tiangou.bindertest.IBookManager;
import com.example.tiangou.bindertest.IOnNewBookArrivedListener;
import com.example.tiangou.bindertest.parcel_model.Book;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {


    private static final String TAG = "BookManagerService";

    private AtomicBoolean mIsServiceDestroyed = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();

    //private CopyOnWriteArrayList<IOnNewBookArrivedListener> mListeners = new CopyOnWriteArrayList<IOnNewBookArrivedListener>();


    private RemoteCallbackList<IOnNewBookArrivedListener> mListeners = new RemoteCallbackList<IOnNewBookArrivedListener>();

    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void addBook(Book book) throws RemoteException {

            mBookList.add(book);
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {

//            if (!mListeners.contains(listener)) {
//
//                mListeners.add(listener);
//
//                Log.d(TAG, "registerListener: " + listener + " success");
//
//
//            } else {
//
//                Log.d(TAG, "registerListener: " + listener + " already exist");
//            }

            mListeners.register(listener);



            Log.d(TAG, "after registerListener: " + listener + " listeners size >>> " + mListeners.getRegisteredCallbackCount());



        }
        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {

//            if (mListeners.contains(listener)) {
//
//                mListeners.remove(listener);
//
//                Log.d(TAG, "unregisterListener: " + listener + " success");
//
//
//            } else {
//
//                Log.d(TAG, "unregisterListener: " + listener + " not found, can not unregister");
//            }

            mListeners.unregister(listener);



            Log.d(TAG, "after unregisterListener: " + listener + " listeners size >>> " + mListeners.getRegisteredCallbackCount());

        }

    };

    @Override
    public void onCreate() {
        super.onCreate();

        mBookList.add(new Book(0, "Android"));
        mBookList.add(new Book(1, "IOS"));

        new Thread(new ServiceWorker()).start();

    }


    @Override
    public void onDestroy() {
        mIsServiceDestroyed.set(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    private void onNewBookArrived(final Book newBook) throws RemoteException {


        new Thread(new Runnable() {
            @Override
            public void run() {

                mBookList.add(newBook);

                Log.d(TAG, "onNewBookArrived: notify listeners listeners' size  >>> " + mListeners.getRegisteredCallbackCount());

                int registeredCallbackCount = mListeners.beginBroadcast();

//        for (int i = 0; i < mListeners.size(); i++) {
//
//            IOnNewBookArrivedListener listener = mListeners.get(i);
//
//            Log.d(TAG, "onNewBookArrived: notify listener " + listener);
//
//            listener.onNewBookArrived(newBook);
//
//        }


                for (int i = 0; i < registeredCallbackCount; i++) {

                    IOnNewBookArrivedListener listener = mListeners.getBroadcastItem(i);

                    Log.d(TAG, "onNewBookArrived: notify listener " + listener);

                    if (listener != null) {

                        try {
                            listener.onNewBookArrived(newBook);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                    }


                }

                mListeners.finishBroadcast();

            }
        }).start();



    }

    private class ServiceWorker implements Runnable {
        @Override
        public void run() {

            while (!mIsServiceDestroyed.get()) {

                try {

                    Thread.sleep(5000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int bookId = mBookList.size();
                String bookName = "newBook#" + bookId;

                Book newBook = new Book(bookId, bookName);

                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }


            }

        }
    }
}
