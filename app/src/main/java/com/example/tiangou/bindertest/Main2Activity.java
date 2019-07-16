package com.example.tiangou.bindertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.tiangou.bindertest.parcel_model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//2.文件共享

public class Main2Activity extends AppCompatActivity {

    private Button button1;
    private Button button2;

    public static final String CACHE_FILE = "/persistToFile";

    private static final String TAG = "Main2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                persistToFile();

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                recoverFromFile();
            }
        });

    }


    private void recoverFromFile() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                User user = null;

                File path = getExternalCacheDir();

                File cacheFile = new File(path.getAbsolutePath() + CACHE_FILE);

                Log.d(TAG, "button2 onClick: " + cacheFile.getAbsolutePath());

                ObjectInputStream objectInputStream = null;


                if (cacheFile.exists()) {

                    try{

                        objectInputStream = new ObjectInputStream(new FileInputStream(cacheFile.getAbsolutePath()));

                        user = (User) objectInputStream.readObject();

                        Log.d(TAG, "button2 onClick: recover user " + user);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } finally {

                        try {

                            objectInputStream.close();

                        } catch (IOException e) {

                            e.printStackTrace();
                        }
                    }
                }


            }
        }).start();
    }

    private void persistToFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User(
                        1,
                        "hello world",
                        true);

                File path = getExternalCacheDir();

                File cacheFile = new File(path.getAbsolutePath() + CACHE_FILE);

                Log.d(TAG, "button1 onClick: " + cacheFile.getAbsolutePath());

                ObjectOutputStream objectOutputStream = null;

                try{

                    objectOutputStream = new ObjectOutputStream(new FileOutputStream(cacheFile.getAbsolutePath()));

                    objectOutputStream.writeObject(user);


                    Log.d(TAG, "button1 onClick: persist user " + user);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                    try {

                        objectOutputStream.close();

                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
