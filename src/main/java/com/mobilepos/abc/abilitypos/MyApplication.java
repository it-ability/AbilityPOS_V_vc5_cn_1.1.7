package com.mobilepos.abc.abilitypos;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.sun.jna.Pointer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import me.myatminsoe.mdetect.MDetect;

public class MyApplication extends Application {
    private static MyApplication mInstance;
    private static String mDeviceAddress="";
    private static OutputStream mmOutputStream;
    private static BluetoothSocket mmBluetoothSocket;


    public static MyApplication getmInstance() {
        return mInstance;
    }

    public static void setmInstance(MyApplication mInstance) {
        MyApplication.mInstance = mInstance;
    }



    public static BluetoothDevice getmBluetoothDeviceAddress() {
        return mBluetoothDeviceAddress;
    }

    public static void setmBluetoothDeviceAddress(BluetoothDevice mBluetoothDeviceAddress) {
        MyApplication.mBluetoothDeviceAddress = mBluetoothDeviceAddress;
    }

    private static BluetoothDevice mBluetoothDeviceAddress;

    public static String getmDeviceAddress() {
        return mDeviceAddress;
    }

    public static void setmDeviceAddress(String mDeviceAddress) {
        MyApplication.mDeviceAddress = mDeviceAddress;
    }



    public static BluetoothSocket getMmBluetoothSocket() {
        return mmBluetoothSocket;
    }

    public static void setMmBluetoothSocket(BluetoothSocket mmBluetoothSocket) {
        MyApplication.mmBluetoothSocket = mmBluetoothSocket;
    }

    public static OutputStream getMmOutputStream() {
        return mmOutputStream;
    }

    public static void setMmOutputStream(OutputStream mmOutputStream) {
        MyApplication.mmOutputStream = mmOutputStream;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        MDetect.INSTANCE.init(this);
    }




    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


}
