package com.roman.garden.sdk;

import android.app.Application;

import com.topon.easy.AdEasy;
import com.topon.easy.Config;
import com.topon.easy.IConfigBuilderCallback;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AdEasy.init(this, "a627c82b32b4e0", "157af9059fd74460e2d5d89d6469056d", new IConfigBuilderCallback() {
            @Override
            public Config buildConfig() {
                return new Config().setBannerPlacement("b627daf554fb83")
                        .setNativePlacement("b627daf718add0")
                        .setInterstitialPlacement("b627daf655fb54")
                        .setRewardedVideoPlacement("b627daf7f36cd3");
            }
        }, true);
    }

}
