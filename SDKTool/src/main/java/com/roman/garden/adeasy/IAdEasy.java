package com.roman.garden.adeasy;

import android.app.Activity;
import android.app.Application;
import android.view.View;

import androidx.annotation.GravityInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.roman.garden.adeasy.ad.IRewardedResultCallback;

interface IAdEasy {

    void init(@NonNull Application application, IAdEasyApplicationImpl impl);

    void init(@NonNull Application application, IAdEasyApplicationImpl impl, String testDeviceId);

    void onCreate(@NonNull Activity activity);

    void onResume(@NonNull Activity activity);

    void onPause(@NonNull Activity activity);

    @SuppressWarnings("only transfer on APP exit")
    void onDestroy();

    boolean hasBanner();

    View getBannerView();

    void showBanner();

    void showBanner(@GravityInt int position);

    void cancelBanner();

    boolean hasNative();

    View getNativeView();

    View getNativeView(@LayoutRes int templateLayoutId);

    boolean hasInterstitial();

    void showInterstitial();

    boolean hasVideo();

    void showVideo(@Nullable IRewardedResultCallback callback);

    void loadAppOpen();

    void showAppOpen();

    void registerAppOpenListener(Observer<Boolean> listener);

    void unregisterAppOpenListener(Observer<Boolean> listener);

    void rate();


}
