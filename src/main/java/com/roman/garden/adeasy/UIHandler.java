package com.roman.garden.adeasy;

import android.os.Handler;
import android.os.Looper;

public final class UIHandler extends Handler {

    private UIHandler(){
        super(Looper.getMainLooper());
    }

    private static UIHandler _instance = null;

    public static UIHandler getInstance(){
        if (_instance == null){
            synchronized (UIHandler.class){
                if (_instance == null){
                    _instance = new UIHandler();
                }
            }
        }
        return _instance;
    }

}
