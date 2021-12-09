package com.roman.garden.adeasy;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface AdImpl {

    void setupBannerListener(@NonNull AdLoadListener _listener);

    void loadBanner(Activity _activity);

    boolean isBannerOk();

    View getBannerView();

    void destroyBannerView(@Nullable View view);

    void setupInterstitialListener(@NonNull AdLoadListener _listener);

    void loadInterstitial(Context _context);

    boolean isInterstitialOk();

    void showInterstitial(Activity _activity);

    void setupVideoListener(@NonNull AdLoadListener _listener);

    void loadVideo(Context _context);

    boolean isVideoOk();

    void setupNativeListener(@NonNull AdLoadListener _listener);

    void showVideo(Activity _activity, RewardVideoResultListener listener);

    void loadNative(Context context);

    boolean isNativeOk();

    View getNativeView();


    void destroy();

}
