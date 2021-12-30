package com.roman.garden.adeasy;

import android.util.Log;

import androidx.annotation.NonNull;

final class LogUtil {

    private static String TAG = "AdSDK";

    public static void e(@NonNull final String msg){
        Log.e(TAG, msg);
    }

    public static void i(@NonNull final String msg){
        Log.i(TAG, msg);
    }

    public static void d(@NonNull final String msg){
        Log.d(TAG, msg);
    }

    public static void w(@NonNull final String msg){
        Log.w(TAG, msg);
    }

    public static void v(@NonNull final String msg){
        Log.v(TAG, msg);
    }


}
