package com.example.tiangou.bindertest.myContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.tiangou.bindertest.common.DataBaseOpenHelper;

public class BookProvider extends ContentProvider {

    private static final String TAG = "BookProvider";

    public static final String AUTHORITY = "com.example.tiangou.bindertest.book.provider";

    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;

    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        uriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        uriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }

    private Context mContext;

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {

        Log.d(TAG, "onCreate: current thread: " + Thread.currentThread().getName());

        mContext = getContext();

        initProviderData();

        return true;
    }

    private void initProviderData() {
        database = new DataBaseOpenHelper(mContext).getWritableDatabase();

        database.execSQL("delete from " + DataBaseOpenHelper.USER_TABLE_NAME);
        database.execSQL("delete from " + DataBaseOpenHelper.BOOK_TABLE_NAME);
        database.execSQL("insert into book values(1, 'Android')" );
        database.execSQL("insert into book values(2, 'Ios')" );
        database.execSQL("insert into book values(3, 'Html')" );
        database.execSQL("insert into user values(1, 'Jack', 0)" );
        database.execSQL("insert into user values(2, 'Jasmine', 0)" );

    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Log.d(TAG, "query: current thread: " + Thread.currentThread().getName());
        

        String table = getTableName(uri);

        if (table == null) {

            throw new IllegalArgumentException("unsupported uri >>> " + uri);
        }




        return database.query(table, projection, selection, selectionArgs, null, null, sortOrder, null);
    }


    @Override
    public String getType(Uri uri) {

        Log.d(TAG, "getType: ");
        
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Log.d(TAG, "insert: ");
        String table = getTableName(uri);

        if (table == null) {

            throw new IllegalArgumentException("unsupported uri >>> " + uri);
        }




        database.insert(table, null, values);



        mContext.getContentResolver().notifyChange(uri, null);


        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        Log.d(TAG, "delete: ");

        String table = getTableName(uri);

        if (table == null) {

            throw new IllegalArgumentException("unsupported uri >>> " + uri);
        }


        int delete = database.delete(table, selection, selectionArgs);

        if (delete > 0) {


            mContext.getContentResolver().notifyChange(uri, null);

        }


        return delete;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "update: ");

        String table = getTableName(uri);

        if (table == null) {

            throw new IllegalArgumentException("unsupported uri >>> " + uri);
        }


        int update = database.update(table, values, selection, selectionArgs);

        if (update > 0) {


            mContext.getContentResolver().notifyChange(uri, null);

        }


        return update;
    }

    private String getTableName(Uri uri) {

        String tableName = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = DataBaseOpenHelper.BOOK_TABLE_NAME;
                break;

            case USER_URI_CODE:
                tableName = DataBaseOpenHelper.USER_TABLE_NAME;
                break;
        }

        return tableName;
    }
}
