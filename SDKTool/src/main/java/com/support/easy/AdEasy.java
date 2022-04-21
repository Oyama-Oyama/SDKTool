package com.support.easy;


import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.Native;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.appodeal.ads.native_ad.views.NativeAdViewAppWall;
import com.appodeal.ads.native_ad.views.NativeAdViewContentStream;
import com.appodeal.ads.native_ad.views.NativeAdViewNewsFeed;

import java.lang.ref.WeakReference;
import java.util.List;

public class AdEasy {

    public static int AD_TYPE_BANNER = Appodeal.BANNER;
    public static int AD_TYPE_INTERSTITIAL = Appodeal.INTERSTITIAL;
    public static int AD_TYPE_REWARDED_VIDEO = Appodeal.REWARDED_VIDEO;
    public static int AD_TYPE_NATIVE = Appodeal.NATIVE;

    public static int BANNER_POSITION_LEFT = Appodeal.BANNER_LEFT;
    public static int BANNER_POSITION_TOP = Appodeal.BANNER_TOP;
    public static int BANNER_POSITION_RIGHT = Appodeal.BANNER_RIGHT;
    public static int BANNER_POSITION_BOTTOM = Appodeal.BANNER_BOTTOM;

    private static WeakReference<Activity> _activity = null;

    private static WeakReference<IRewardedVideoCallback> _rewardedVideoCallbacks = null;
    private static WeakReference<IInterstitialCallback> _interstitialCallbacks = null;

    public static void init(@NonNull Activity activity, @NonNull String appKey) {
        init(activity, appKey, AD_TYPE_BANNER | AD_TYPE_NATIVE | AD_TYPE_INTERSTITIAL | AD_TYPE_REWARDED_VIDEO);
    }

    public static void init(@NonNull Activity activity, @NonNull String appKey, @NonNull int adTypes) {
        init(activity, appKey, adTypes, false);
    }

    public static void init(@NonNull Activity activity, @NonNull String appKey, boolean testing) {
        init(activity, appKey, AD_TYPE_BANNER | AD_TYPE_NATIVE | AD_TYPE_INTERSTITIAL | AD_TYPE_REWARDED_VIDEO, testing);
    }

    public static void init(@NonNull Activity activity, @NonNull String appKey, @NonNull int adTypes, boolean testing) {
        _activity = new WeakReference<>(activity);
        Appodeal.initialize(activity, appKey, adTypes);
        setNativeAdType(Native.NativeAdType.Auto);
        setTesting(testing);
        Appodeal.setRewardedVideoCallbacks(new RewardedVideoCallbacks() {
            @Override
            public void onRewardedVideoLoaded(boolean isPrecache) {

            }

            @Override
            public void onRewardedVideoFailedToLoad() {

            }

            @Override
            public void onRewardedVideoShown() {
                if (_rewardedVideoCallbacks != null && _rewardedVideoCallbacks.get() != null)
                    _rewardedVideoCallbacks.get().onRewardedVideoShown();
            }

            @Override
            public void onRewardedVideoShowFailed() {
                if (_rewardedVideoCallbacks != null && _rewardedVideoCallbacks.get() != null)
                    _rewardedVideoCallbacks.get().onRewardedVideoShowFailed();
            }

            @Override
            public void onRewardedVideoFinished(double amount, @Nullable String name) {
                if (_rewardedVideoCallbacks != null && _rewardedVideoCallbacks.get() != null)
                    _rewardedVideoCallbacks.get().onRewarded(true);
            }

            @Override
            public void onRewardedVideoClosed(boolean finished) {
                if (_rewardedVideoCallbacks != null && _rewardedVideoCallbacks.get() != null)
                    _rewardedVideoCallbacks.get().onRewardedVideoClosed();
            }

            @Override
            public void onRewardedVideoExpired() {

            }

            @Override
            public void onRewardedVideoClicked() {
                if (_rewardedVideoCallbacks != null && _rewardedVideoCallbacks.get() != null)
                    _rewardedVideoCallbacks.get().onRewardedVideoClicked();
            }
        });
        Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
            @Override
            public void onInterstitialLoaded(boolean isPrecache) {

            }

            @Override
            public void onInterstitialFailedToLoad() {

            }

            @Override
            public void onInterstitialShown() {
                if (_interstitialCallbacks != null && _interstitialCallbacks.get() != null)
                    _interstitialCallbacks.get().onInterstitialShown();
            }

            @Override
            public void onInterstitialShowFailed() {
                if (_interstitialCallbacks != null && _interstitialCallbacks.get() != null)
                    _interstitialCallbacks.get().onInterstitialShowFailed();
            }

            @Override
            public void onInterstitialClicked() {
                if (_interstitialCallbacks != null && _interstitialCallbacks.get() != null)
                    _interstitialCallbacks.get().onInterstitialClicked();
            }

            @Override
            public void onInterstitialClosed() {
                if (_interstitialCallbacks != null && _interstitialCallbacks.get() != null)
                    _interstitialCallbacks.get().onInterstitialClosed();
            }

            @Override
            public void onInterstitialExpired() {

            }
        });
    }

    public static void onStart(Activity activity) {
        _activity = new WeakReference<>(activity);
    }

    public static void onResume(Activity activity) {
        _activity = new WeakReference<>(activity);
    }

    public static void setTesting(boolean testing) {
        Appodeal.setTesting(testing);
    }

    public static void resetAutoLoadStatus(int adType, boolean autoLoad) {
        Appodeal.setAutoCache(adType, autoLoad);
    }

    public static boolean isAutoCacheEnabled(int adType) {
        return Appodeal.isAutoCacheEnabled(adType);
    }

    public static boolean isPrecache(int adType) {
        return Appodeal.isPrecache(adType);
    }

    public static int getAvailableNativeAdsCount() {
        return Appodeal.getAvailableNativeAdsCount();
    }

    public static boolean isInitialized(int adType) {
        return Appodeal.isInitialized(adType);
    }

    public static boolean isLoaded(int adType) {
        return Appodeal.isLoaded(adType);
    }

    public static boolean canShow(int adType) {
        return Appodeal.canShow(adType);
    }

    public static void cache(@NonNull Activity activity, int adType) {
        cache(activity, adType, 1);
    }

    public static void cache(@NonNull Activity activity, int adType, int count) {
        Appodeal.cache(activity, adType, count);
    }

    /**
     * 获取广告的预测有效每千次展示费用
     *
     * @param adType
     * @return
     */
    public static Double getPredictedEcpm(int adType) {
        return Appodeal.getPredictedEcpm(adType);
    }

    /**
     * 视频是否静默播放
     *
     * @param mute
     */
    public static void muteVideosIfCallsMuted(boolean mute) {
        Appodeal.muteVideosIfCallsMuted(mute);
    }

    /**
     * 获取指定数量的native广告
     *
     * @param count
     * @return
     */
    public static List<NativeAd> getNativeAds(int count) {
        return Appodeal.getNativeAds(count);
    }

    public static void setNativeAdType(Native.NativeAdType nativeAdType) {
        Appodeal.setNativeAdType(nativeAdType);
    }

    public static Native.NativeAdType getNativeAdType() {
        return Appodeal.getNativeAdType();
    }

//    public static void setNativeCallbacks(NativeCallbacks callbacks) {
//        Appodeal.setNativeCallbacks(callbacks);
//    }

    private static NativeAd getNativeAd() {
        List<NativeAd> nativeAds = getNativeAds(1);
        if (nativeAds != null && nativeAds.size() == 1)
            return nativeAds.get(0);
        return null;
    }

//    public static View buildNativeView(@NonNull Context context) {
//        NativeAd nativeAd = getNativeAd();
//        if (nativeAd == null)
//            return null;
//        NativeAdView nativeAdView = new NativeAdView(context);
//        // nativeAd.
//
//        return nativeAdView;
//    }

    public static View buildNativeViewAppWall(@NonNull Context context) {
        NativeAd nativeAd = getNativeAd();
        if (nativeAd == null)
            return null;
        NativeAdViewAppWall nativeAdView = new NativeAdViewAppWall(context);
        nativeAdView.setNativeAd(nativeAd);
        return nativeAdView;
    }

    public static View buildNativeViewNewsFeed(@NonNull Context context) {
        NativeAd nativeAd = getNativeAd();
        if (nativeAd == null)
            return null;
        NativeAdViewNewsFeed nativeAdView = new NativeAdViewNewsFeed(context);
        nativeAdView.setNativeAd(nativeAd);
        return nativeAdView;
    }

    public static View buildNativeViewContentStream(@NonNull Context context) {
        NativeAd nativeAd = getNativeAd();
        if (nativeAd == null)
            return null;
        NativeAdViewContentStream nativeAdView = new NativeAdViewContentStream(context);
        nativeAdView.setNativeAd(nativeAd);
        return nativeAdView;
    }

    /**
     * 必需的原生媒体资产类型
     *
     * @param mediaAssetType
     */
    public static void setRequiredNativeMediaAssetType(Native.MediaAssetType mediaAssetType) {
        Appodeal.setRequiredNativeMediaAssetType(mediaAssetType);
    }


    public static View getBannerView(@NonNull Context context) {
        return Appodeal.getBannerView(context);
    }

    public static void setBannerAnimation(boolean ani) {
        Appodeal.setBannerAnimation(ani);
    }

//    public static void setBannerCallbacks(BannerCallbacks callbacks) {
//        Appodeal.setBannerCallbacks(callbacks);
//    }

    public static void setBannerViewId(int id) {
        Appodeal.setBannerViewId(id);
    }

    public static void setSmartBanners(boolean smart) {
        Appodeal.setSmartBanners(smart);
    }

    public static boolean isSmartBannersEnabled() {
        return Appodeal.isSmartBannersEnabled();
    }

    public static void setBannerRotation(int id, int rotation) {
        Appodeal.setBannerRotation(id, rotation);
    }

    public static boolean canShowBanner() {
        return isLoaded(AD_TYPE_BANNER) && canShow(AD_TYPE_BANNER);
    }

    public static void showBanner(@NonNull Activity activity) {
        showBanner(activity, -1);
    }

    public static void hideBanner(@NonNull Activity activity) {
        hideBanner(activity, -1);
    }

    public static void showBanner(@NonNull Activity activity, int position) {
        if (position == BANNER_POSITION_LEFT)
            Appodeal.show(activity, Appodeal.BANNER);
        else if (position == BANNER_POSITION_TOP)
            Appodeal.show(activity, Appodeal.BANNER_TOP);
        else if (position == BANNER_POSITION_RIGHT)
            Appodeal.show(activity, Appodeal.BANNER_RIGHT);
        else if (position == BANNER_POSITION_BOTTOM)
            Appodeal.show(activity, Appodeal.BANNER_BOTTOM);
        else
            Appodeal.show(activity, Appodeal.BANNER);
    }

    public static void hideBanner(@NonNull Activity activity, int position) {
        if (position == BANNER_POSITION_LEFT)
            Appodeal.hide(activity, Appodeal.BANNER);
        else if (position == BANNER_POSITION_TOP)
            Appodeal.hide(activity, Appodeal.BANNER_TOP);
        else if (position == BANNER_POSITION_RIGHT)
            Appodeal.hide(activity, Appodeal.BANNER_RIGHT);
        else if (position == BANNER_POSITION_BOTTOM)
            Appodeal.hide(activity, Appodeal.BANNER_BOTTOM);
        else
            Appodeal.hide(activity, Appodeal.BANNER);
    }

    public static boolean canShowInterstitial() {
        return isLoaded(AD_TYPE_INTERSTITIAL) && canShow(AD_TYPE_INTERSTITIAL);
    }

    public static boolean showInterstitial(@NonNull Activity activity) {
        return showInterstitial(activity, null);
    }

    public static boolean showInterstitial(@NonNull Activity activity, @Nullable IInterstitialCallback callback) {
        if (callback == null) {
            if (_interstitialCallbacks != null)
                _interstitialCallbacks.clear();
            _interstitialCallbacks = null;
        } else {
            _interstitialCallbacks = new WeakReference<>(callback);
        }
        return Appodeal.show(activity, AD_TYPE_INTERSTITIAL);
    }

//    public static void setInterstitialCallbacks(InterstitialCallbacks callbacks) {
//        Appodeal.setInterstitialCallbacks(callbacks);
//    }


    public static boolean canShowRewardedVideo() {
        return isLoaded(AD_TYPE_REWARDED_VIDEO) && canShow(AD_TYPE_REWARDED_VIDEO);
    }

    public static boolean showRewardedVideo(@NonNull Activity activity) {
        return showRewardedVideo(activity, null);
    }

    public static boolean showRewardedVideo(@NonNull Activity activity, @Nullable IRewardedVideoCallback callback) {
        if (callback == null) {
            if (_rewardedVideoCallbacks != null)
                _rewardedVideoCallbacks.clear();
            _rewardedVideoCallbacks = null;
        } else {
            _rewardedVideoCallbacks = new WeakReference<>(callback);
        }
        return Appodeal.show(activity, AD_TYPE_REWARDED_VIDEO);
    }

//    public static void setRewardedVideoCallbacks(RewardedVideoCallbacks callbacks) {
//        Appodeal.setRewardedVideoCallbacks(callbacks);
//    }

    //开屏广告
    private static AdmobAppOpenManager _admobAppOpenManager = null;
    private static String ADMOB_APP_OPEN_ID = null;

    public static void setAdmobAppOpenId(String id) {
        ADMOB_APP_OPEN_ID = id;
    }

    public static void loadAppOpenAd() {
        if (_admobAppOpenManager == null)
            _admobAppOpenManager = new AdmobAppOpenManager(ADMOB_APP_OPEN_ID);
        if (_activity != null && _activity.get() != null)
            _admobAppOpenManager.loadAd(_activity.get().getApplicationContext());
    }

    public static void showAppOpenAd() {
        if (_admobAppOpenManager != null && _activity != null && _activity.get() != null)
            _admobAppOpenManager.showAdIfAvailable(_activity.get());
    }


}
