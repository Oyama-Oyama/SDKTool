package com.topon.easy;

import android.util.Log;

public class L {

    public static final String TAG = "TopOn";

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void D(String msg) {
        Log.d(TAG, msg);
    }

    public static void w(String msg) {
        Log.w(TAG, msg);
    }

    public static void E(String msg) {
        Log.e(TAG, msg);
    }

}
