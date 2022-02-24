package com.roman.garden.adeasy.ad.impl;

import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.roman.garden.adeasy.ad.AdImpl;
import com.roman.garden.adeasy.ad.AdItem;
import com.roman.garden.adeasy.ad.IRewardedResultCallback;
import com.roman.garden.adeasy.ad.Platform;
import com.roman.garden.adeasy.util.HandlerUtil;

import java.util.List;

public abstract class BaseAdImpl {

    protected String testDeviceId;
    protected Platform _platform;

    private static final int retryInitPlatformDuration = 5 * 1000;

    public BaseAdImpl() {
    }

    public void setup(Platform _platform, String testDeviceId) {
        this._platform = _platform;
        this.testDeviceId = testDeviceId;
        bindLiveData();
        initPlatform();
    }

    public void destroy() {

    }

    protected String getTestDeviceId() {
        return testDeviceId;
    }

    abstract void bindLiveData();

    abstract void initPlatform();

    abstract void loadBanner(@NonNull AdItem adItem);

    public abstract View getBannerView();

    public abstract boolean hasBanner();

    abstract void loadInterstitial(@NonNull AdItem adItem);

    public abstract void showInterstitial();

    public abstract boolean hasInterstitial();

    abstract void loadVideo(@NonNull AdItem adItem);

    public abstract void showVideo(IRewardedResultCallback callback);

    public abstract boolean hasVideo();

    abstract void loadNative(@NonNull AdItem adItem);

    public abstract View getNativeView(@LayoutRes int templateLayoutId);

    public abstract boolean hasNative();

    public abstract int getWeights(String adType);

    public abstract void loadAppOpen();

    public abstract void showAppOpen();

    public abstract void registerAppOpenListener(Observer<Boolean> listener);
    public abstract void unregisterAppOpenListener(Observer<Boolean> listener);

    public abstract AdImpl getBannerLive();

    public abstract AdImpl getNativeLive();

    protected void retryInitPlatform() {
        HandlerUtil.getInstance().removeCallbacks(retryInitPlatformTask);
        HandlerUtil.getInstance().postDelayed(retryInitPlatformTask, retryInitPlatformDuration);
    }

    private Runnable retryInitPlatformTask = new Runnable() {
        @Override
        public void run() {
            initPlatform();
        }
    };

    protected String getAppId() {
        return _platform.getAppId();
    }

    protected List<AdItem> getBanner() {
        return _platform.getBanner();
    }

    protected List<AdItem> getInterstitial() {
        return _platform.getInterstitial();
    }

    protected List<AdItem> getVideo() {
        return _platform.getVideo();
    }

    protected List<AdItem> getNative() {
        return _platform.getNative();
    }

    protected AdItem getAppOpen() {
        return _platform.getAppOpen();
    }

}
