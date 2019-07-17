package com.example.tiangou.bindertest.binderPool;

import android.os.RemoteException;

import com.example.tiangou.bindertest.ICompute;

public class ComputeImpl extends ICompute.Stub {

    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }

    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
