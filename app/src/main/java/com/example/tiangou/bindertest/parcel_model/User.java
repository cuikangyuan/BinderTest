package com.example.tiangou.bindertest.parcel_model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class User implements Parcelable, Serializable {

    public int userId;
    public String userName;
    public boolean isMale;

    public Book book;


    public User(int userId, String userName, boolean isMale) {

        this.userId = userId;
        this.userName = userName;
        this.isMale = isMale;
    }

    private User(Parcel parcel) {

        this.userId = parcel.readInt();
        this.userName = parcel.readString();
        this.isMale = parcel.readInt() == 1;
        this.book = parcel.readParcelable(Thread.currentThread().getContextClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeInt(isMale ? 1 : 0);
        dest.writeParcelable(book, 0);

    }


    @Override
    public String toString() {
        String s = "userId: " + userId + " userName: " + userName + " isMale: " + isMale;
        return  s;
    }
}
