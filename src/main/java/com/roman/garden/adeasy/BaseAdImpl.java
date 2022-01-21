package com.roman.garden.adeasy;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

abstract class BaseAdImpl implements Observer<PlatformConfig> {

    protected boolean isTestMode = false;
    protected String testDeviceId;
    private MutableLiveData<PlatformConfig> _platformConfig;

    public BaseAdImpl() {
        _platformConfig = new MutableLiveData<>();
        _platformConfig.observeForever(this);
    }

    public void destroy() {
        _platformConfig.removeObserver(this);
    }

    public MutableLiveData<PlatformConfig> getPlatformConfig() {
        return _platformConfig;
    }

    public void setPlatformConfig(PlatformConfig _config) {
        _platformConfig.postValue(_config);
    }

    public void setPlatformConfig(PlatformConfig _config, boolean testMode) {
        this.isTestMode = testMode;
        _platformConfig.postValue(_config);
    }

    public void setPlatformConfig(PlatformConfig _config, boolean testMode, @Nullable String testDeviceId) {
        this.isTestMode = testMode;
        this.testDeviceId = testDeviceId;
        _platformConfig.postValue(_config);
    }


    @Override
    public void onChanged(PlatformConfig platformConfig) {
        initAdPlatform();
    }

    /**
     * 初始化广告平台
     */
    protected abstract void initAdPlatform();

    /**
     * 重试初始化广告平台
     *
     * @param platformConfig
     */
    protected void retryInitAdPlatform() {
        UIHandler.getInstance().removeCallbacks(retryRunnable);
        UIHandler.getInstance().postDelayed(retryRunnable, Const.PLATFORM_RETRY_INIT_DURATION);
    }

    private Runnable retryRunnable = new Runnable() {
        @Override
        public void run() {
            initAdPlatform();
        }
    };

    /**
     * 广告平台应用Id
     *
     * @return
     */
    protected String getAppId() {
        return _platformConfig.getValue().getAppId();
    }

    /**
     * 获取banner id 及优先级
     *
     * @return
     */
    protected AdItem getBannerId() {
        return _platformConfig.getValue().getBannerId();
    }

    /**
     * 获取插屏 id 及优先级
     *
     * @return
     */
    protected AdItem getInterstitialId() {
        return _platformConfig.getValue().getInterstitialId();
    }

    /**
     * 获取native id 及优先级
     *
     * @return
     */
    protected AdItem getNativeId() {
        return _platformConfig.getValue().getNativeId();
    }

    /**
     * 获取激励 id 及优先级
     *
     * @return
     */
    protected AdItem getVideoId() {
        return _platformConfig.getValue().getVideoId();
    }

    /**
     * 开屏广告ID
     *
     * @return
     */
    protected AdItem getOpenScreenId() {
        return _platformConfig.getValue().getOpenScreenId();
    }

    /**
     * 检查广告id是否不为空
     *
     * @param id
     * @return
     */
    protected boolean validAdItem(@Nullable AdItem item) {
        return item != null && !StringUtil.isEmpty(item.getAdId());
    }

    /**
     * 检查广告平台是否正确初始化
     *
     * @return
     */
    protected abstract boolean validPlatform();

    /**
     * 加载banner
     */
    protected abstract void loadBanner();

    protected void setupBanner(@NonNull AdItem item) {
        AdImpl.getInstance().addBanner(item);
    }

    protected abstract View getBannerView();

    private Runnable bannerReloadRunnable = new Runnable() {
        @Override
        public void run() {
            loadBanner();
        }
    };

    protected void reloadBanner() {
        UIHandler.getInstance().removeCallbacks(bannerReloadRunnable);
        UIHandler.getInstance().postDelayed(bannerReloadRunnable, Const.AD_RELOAD_DURATION);
    }

    /**
     * 加载插屏
     */
    protected abstract void loadInterstitial();

    protected abstract void showInterstitial();

    protected void setupInterstitial(@NonNull AdItem item) {
        AdImpl.getInstance().addInterstitial(item);
    }

    private Runnable interstitialReloadRunnable = new Runnable() {
        @Override
        public void run() {
            loadInterstitial();
        }
    };

    protected void reloadInterstitial() {
        UIHandler.getInstance().removeCallbacks(interstitialReloadRunnable);
        UIHandler.getInstance().postDelayed(interstitialReloadRunnable, Const.AD_RELOAD_DURATION);
    }

    /**
     * 加载native
     */
    protected abstract void loadNative();

    protected abstract View getNativeView();

    protected void setupNative(@NonNull AdItem item) {
        AdImpl.getInstance().addNative(item);
    }

    private Runnable nativeReloadRunnable = new Runnable() {
        @Override
        public void run() {
            loadNative();
        }
    };

    protected void reloadNative() {
        UIHandler.getInstance().removeCallbacks(nativeReloadRunnable);
        UIHandler.getInstance().postDelayed(nativeReloadRunnable, Const.AD_RELOAD_DURATION);
    }

    /**
     * 加载视频
     */
    protected abstract void loadVideo();

    protected abstract void showVideo(@Nullable IVideoResultCallback callback);

    protected void setupVideo(@NonNull AdItem item) {
        AdImpl.getInstance().addVideo(item);
    }

    private Runnable videoReloadRunnable = new Runnable() {
        @Override
        public void run() {
            loadVideo();
        }
    };

    protected void reloadVideo() {
        UIHandler.getInstance().removeCallbacks(videoReloadRunnable);
        UIHandler.getInstance().postDelayed(videoReloadRunnable, Const.AD_RELOAD_DURATION);
    }

    /**
     * 开屏广告，暂时只有ADMOB
     */
    protected void loadOpenScreen() {
    }

    public void showOpenScreen() {
    }

    protected void logEvent(@NonNull AdItem item, @NonNull String adType) {
        LogUtil.i(item.getAdGroup() + " >> " + adType + " >> loaded");
    }

    protected void logEvent(@NonNull AdItem item, @NonNull String adType, @Nullable String event) {
        LogUtil.i(item.getAdGroup() + " >> " + adType + " >> " + event);
    }

    protected void logError(@NonNull AdItem item, @NonNull String adType, @Nullable String error) {
        LogUtil.e(item.getAdGroup() + " >> " + adType + " >> load error:" + error);
    }

}
