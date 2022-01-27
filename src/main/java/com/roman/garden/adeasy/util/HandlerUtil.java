package com.roman.garden.adeasy.util;

import android.os.Handler;
import android.os.Looper;


public class HandlerUtil extends Handler {

    private HandlerUtil() {
        super(Looper.getMainLooper());
    }

    private static HandlerUtil _instance = null;

    public static HandlerUtil getInstance() {
        if (_instance == null) {
            synchronized (HandlerUtil.class) {
                if (_instance == null) {
                    _instance = new HandlerUtil();
                }
            }
        }
        return _instance;
    }

}
