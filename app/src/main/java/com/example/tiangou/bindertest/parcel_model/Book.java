package com.example.tiangou.bindertest.parcel_model;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {

    public int bookId;
    public String bookName;

    public Book(int bookId, String bookName) {

        this.bookId = bookId;
        this.bookName = bookName;
    }

    private Book(Parcel in) {

        this.bookId = in.readInt();
        this.bookName = in.readString();

    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {

        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[0];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(bookId);
        dest.writeString(bookName);
    }

    @Override
    public String toString() {
        String s = " [bookId: " + bookId + " bookName: " + bookName + "] ";
        return  s;
    }
}
