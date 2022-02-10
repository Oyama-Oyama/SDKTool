package com.roman.garden.sdk;

import android.app.Application;

import androidx.annotation.NonNull;

import com.roman.garden.adeasy.AdEasyImpl;
import com.roman.garden.adeasy.IAdEasyApplicationImpl;
import com.roman.garden.adeasy.ad.AdInfo;
import com.roman.garden.adeasy.ad.Platform;

public class App extends Application implements IAdEasyApplicationImpl {


    @Override
    public void onCreate() {
        super.onCreate();
        AdEasyImpl.getInstance().init(this, this, "DD9282199306F5659ADC5CD3E4DF970D");
    }

    @Override
    public Platform getPlatform(@NonNull String adGroup) {
        switch (adGroup){
            case AdInfo.GROUP_ADMOB:
                return new Platform.Builder()
                        .buildAdmob()
                        .addParameter("ca-app-pub-3940256099942544/3419835294", AdInfo.TYPE_OPEN_SCREEN, 10)
                        .addParameter("ca-app-pub-3940256099942544/6300978111", AdInfo.TYPE_BANNER, 10)
                        .addParameter("ca-app-pub-3940256099942544/1033173712", AdInfo.TYPE_INTERSTITIAL, 8, AdInfo.PRICE_ALL)
                        .addParameter("ca-app-pub-3940256099942544/8691691433", AdInfo.TYPE_INTERSTITIAL,  8, AdInfo.PRICE_MEDIUM)
                        .addParameter("ca-app-pub-3940256099942544/5224354917", AdInfo.TYPE_VIDEO, 10)
                        .addParameter("ca-app-pub-3940256099942544/1044960115", AdInfo.TYPE_NATIVE, 10, AdInfo.PRICE_MEDIUM)
                        .addParameter("ca-app-pub-3940256099942544/2247696110", AdInfo.TYPE_NATIVE, 10, AdInfo.PRICE_MEDIUM)
                        .build();
        }
        return null;
    }


}
