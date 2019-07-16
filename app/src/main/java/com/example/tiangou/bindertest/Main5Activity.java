package com.example.tiangou.bindertest;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.tiangou.bindertest.myContentProvider.BookProvider;
import com.example.tiangou.bindertest.parcel_model.Book;

import static com.example.tiangou.bindertest.myContentProvider.BookProvider.BOOK_CONTENT_URI;

//使用ContentProvider

public class Main5Activity extends AppCompatActivity {

    private Button button1;
    private Button button2;

    private static final String TAG = "Main5Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);


        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);



        //getContentResolver().query(BOOK_CONTENT_URI, null, null, null, null);
//
//        getContentResolver().query(uri, null, null, null, null);
//
//        getContentResolver().query(uri, null, null, null, null);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor query = getContentResolver().query(
                        BOOK_CONTENT_URI,
                        new String[]{"_id", "name"},
                        null,
                        null,
                        null);

                if (query != null) {

                    while (query.moveToNext()) {

                        Book book = new Book(
                                query.getInt(0),
                                query.getString(1));

                        Log.d(TAG, "query book : " + book.toString());

                    }

                    query.close();
                }





            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index = 0;

                Cursor query = getContentResolver().query(
                        BOOK_CONTENT_URI,
                        new String[]{"_id", "name"},
                        null,
                        null,
                        null);

                if (query != null) {

                    index = query.getCount();

                    query.close();
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", index+1);
                contentValues.put("name", "newBook#" +(index+1));

                getContentResolver().insert(BOOK_CONTENT_URI, contentValues);

            }
        });
    }
}
