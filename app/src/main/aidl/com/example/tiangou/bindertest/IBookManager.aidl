// IBookManager.aidl
package com.example.tiangou.bindertest;
import com.example.tiangou.bindertest.parcel_model.Book;
import com.example.tiangou.bindertest.IOnNewBookArrivedListener;


// Declare any non-default types here with import statements

interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    void addBook(in Book book);

    List<Book> getBookList();

    void registerListener(IOnNewBookArrivedListener listener);

    void unregisterListener(IOnNewBookArrivedListener listener);

}
