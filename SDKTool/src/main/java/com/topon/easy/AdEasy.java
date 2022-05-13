package com.topon.easy;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.lifecycle.LifecycleObserver;

import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.ATAdStatusInfo;
import com.anythink.core.api.ATGDPRAuthCallback;
import com.anythink.core.api.ATSDK;
import com.anythink.core.api.AdError;
import com.anythink.core.api.AreaCode;
import com.anythink.core.api.DeviceInfoCallback;
import com.anythink.core.api.NetTrafficeCallback;
import com.anythink.interstitial.api.ATInterstitial;
import com.anythink.interstitial.api.ATInterstitialAutoAd;
import com.anythink.interstitial.api.ATInterstitialAutoEventListener;
import com.anythink.interstitial.api.ATInterstitialAutoLoadListener;
import com.anythink.interstitial.api.ATInterstitialListener;
import com.anythink.nativead.api.ATNative;
import com.anythink.nativead.api.ATNativeNetworkListener;
import com.anythink.nativead.api.NativeAd;
import com.anythink.rewardvideo.api.ATRewardVideoAutoAd;
import com.anythink.rewardvideo.api.ATRewardVideoAutoEventListener;
import com.anythink.rewardvideo.api.ATRewardVideoAutoLoadListener;
import com.support.easy.CacheUtil;
import com.support.easy.StringUtil;

import java.lang.ref.WeakReference;

public class AdEasy implements LifecycleObserver {

    private static final String TAG_GDPR = "__gdpr__";
    private static final Object TAG_BANNER_VIEW = "__banner__";
    private static Application _application = null;

    private static Config _config = null;

    private static WeakReference<Activity> _activity = null;


    public static void init(Application application, String TopOnAppID, String TopOnAppKey, IConfigBuilderCallback callback) {
        init(application, TopOnAppID, TopOnAppKey, callback, false);
    }

    public static void init(Application application, String TopOnAppID, String TopOnAppKey, IConfigBuilderCallback callback, boolean testMode) {
        _application = application;
        ATSDK.init(application, TopOnAppID, TopOnAppKey);
        CacheUtil.init(application);
        ATSDK.setAdLogoVisible(testMode);
        ATSDK.setNetworkLogDebug(testMode);
        ATSDK.setSDKArea(AreaCode.GLOBAL);
        if (testMode) {
            ATSDK.testModeDeviceInfo(application, new DeviceInfoCallback() {
                @Override
                public void deviceInfo(String s) {
                    L.D(s);
                }
            });
        }
        integrationChecking(application);

        _config = callback.buildConfig();
        // TODO: 2022/5/12 待检测使用Application是否可以替代Activity
        initInterstitial();
        initRewardedVideo();
    }

    public static void onCreate(Activity activity) {


    }

    public static void onStart(Activity activity) {
        _activity = new WeakReference<>(activity);
    }

    public static void onStop(Activity activity) {

    }

    public static void destroy() {
        if (_admobAppOpenManager != null)
            _admobAppOpenManager.destroy();
        _admobAppOpenManager = null;
    }

    public static void integrationChecking(Application application) {
        ATSDK.integrationChecking(application);
    }

    public static void initGDPR(Context context) {
        if (!CacheUtil.getValue(TAG_GDPR, false)) {
            isGDPR(context, new NetTrafficeCallback() {
                @Override
                public void onResultCallback(boolean b) {
                    if (b) {
                        showGDPRAuth(context, new ATGDPRAuthCallback() {
                            @Override
                            public void onAuthResult(int i) {
                                setGDPRUploadDataLevel(context, i);
                                CacheUtil.putValue(TAG_GDPR, true);
                            }

                            @Override
                            public void onPageLoadFail() {

                            }
                        });
                    }
                }

                @Override
                public void onErrorCallback(String s) {

                }
            });
        }
    }

    public static void isGDPR(Context context, NetTrafficeCallback callback) {
        ATSDK.checkIsEuTraffic(context, callback);
    }

    public static int getGDPRDataLevel(Context context) {
        return ATSDK.getGDPRDataLevel(context);
    }

    public static void setGDPRUploadDataLevel(Context context, int level) {
        ATSDK.setGDPRUploadDataLevel(context, level);
    }

    public static void resetGDPR(Context context) {
        CacheUtil.putValue(TAG_GDPR, false);
        initGDPR(context);
    }

    public static void showGDPRAuth(Context context) {
        showGDPRAuth(context, null);
    }

    public static void showGDPRAuth(Context context, ATGDPRAuthCallback callback) {
        ATSDK.showGdprAuth(context, callback);
    }

    //Interstitial
    public static ATInterstitial loadInterstitial(Activity activity, String placementId) {
        return loadInterstitial(activity, placementId, null);
    }

    public static ATInterstitial loadInterstitial(Activity activity, String placementId, ATInterstitialListener listener) {
        ATInterstitial atInterstitial = new ATInterstitial(activity, placementId);
        atInterstitial.setAdListener(listener);
        atInterstitial.load(activity);
        return atInterstitial;
    }

    public static void showLoadedInterstitial(Activity activity, ATInterstitial atInterstitial) {
        if (atInterstitial != null && atInterstitial.isAdReady())
            atInterstitial.show(activity);
    }

    private static void initInterstitial() {
        if (_config != null) {
            if (!StringUtil.isEmpty(_config.AD_INTERSTITIAL_PLACEMENT)) {
                ATInterstitialAutoAd.init(_application, new String[]{_config.AD_INTERSTITIAL_PLACEMENT}, new ATInterstitialAutoLoadListener() {
                    @Override
                    public void onInterstitialAutoLoaded(String s) {
                        L.i("InterstitialAutoLoaded");
                    }

                    @Override
                    public void onInterstitialAutoLoadFail(String s, AdError adError) {
                        L.i("InterstitialAutoLoadFail --> " + adError.getFullErrorInfo());
                    }
                });
            }
        }
    }

    public static boolean hasInterstitial() {
        if (_config != null)
            if (!StringUtil.isEmpty(_config.AD_INTERSTITIAL_PLACEMENT))
                return ATInterstitialAutoAd.isAdReady(_config.AD_INTERSTITIAL_PLACEMENT);
        return false;
    }

    public static void showInterstitial(Activity activity) {
        showInterstitial(activity, null);
    }

    public static void showInterstitial(Activity activity, ATInterstitialAutoEventListener listener) {
        if (hasInterstitial())
            ATInterstitialAutoAd.show(activity, _config.AD_INTERSTITIAL_PLACEMENT, listener);
    }

    //banner
    public static void showBanner(Activity activity) {
        showBanner(activity, Gravity.BOTTOM | Gravity.CENTER);
    }

    public static void showBanner(Activity activity, int position) {
        if (_config == null)
            return;
        if (activity != null) {
            activity.getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    FrameLayout root = activity.findViewById(android.R.id.content);
                    CustomBannerView bannerView = new CustomBannerView(activity);
                    bannerView.setTag(TAG_BANNER_VIEW);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
                    params.gravity = position;
                    root.addView(bannerView, params);
                }
            });
        }
    }

    public static void hideBanner() {
        try {
            if (_activity != null && _activity.get() != null) {
                FrameLayout root = _activity.get().findViewById(android.R.id.content);
                if (root != null) {
                    View view = root.findViewWithTag(TAG_BANNER_VIEW);
                    if (view != null)
                        root.removeView(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getBannerPlacementId() {
        if (_config != null)
            return _config.AD_BANNER_PLACEMENT;
        return null;
    }

    //Rewarded video
    public static void initRewardedVideo() {
        if (_config != null) {
            if (!StringUtil.isEmpty(_config.AD_REWARDED_VIDEO_PLACEMENT)) {
                ATRewardVideoAutoAd.init(_application, new String[]{_config.AD_REWARDED_VIDEO_PLACEMENT}, new ATRewardVideoAutoLoadListener() {
                    @Override
                    public void onRewardVideoAutoLoaded(String s) {
                        L.i("RewardVideoAutoLoaded");
                    }

                    @Override
                    public void onRewardVideoAutoLoadFail(String s, AdError adError) {
                        L.i("RewardVideoAutoLoadFail --> " + adError.getFullErrorInfo());
                    }
                });
            }
        }
    }

    public static boolean hasRewardedVideo() {
        if (_config != null)
            if (!StringUtil.isEmpty(_config.AD_REWARDED_VIDEO_PLACEMENT))
                return ATRewardVideoAutoAd.isAdReady(_config.AD_REWARDED_VIDEO_PLACEMENT);
        return false;
    }

    public static void showRewardedVideo(Activity activity, IRewardedVideoCallback listener) {
        if (hasRewardedVideo())
            ATRewardVideoAutoAd.show(activity, _config.AD_REWARDED_VIDEO_PLACEMENT, new ATRewardVideoAutoEventListener() {
                @Override
                public void onRewardedVideoAdPlayStart(ATAdInfo atAdInfo) {
                    L.i("onRewardedVideoAdPlayStart");
                }

                @Override
                public void onRewardedVideoAdPlayEnd(ATAdInfo atAdInfo) {
                    L.i("onRewardedVideoAdPlayEnd");
                }

                @Override
                public void onRewardedVideoAdPlayFailed(AdError adError, ATAdInfo atAdInfo) {
                    L.i("onRewardedVideoAdPlayFailed --> " + adError.getFullErrorInfo());
                    if (listener != null)
                        listener.onRewarded(false);
                }

                @Override
                public void onRewardedVideoAdClosed(ATAdInfo atAdInfo) {
                    L.i("onRewardedVideoAdClosed");
                }

                @Override
                public void onRewardedVideoAdPlayClicked(ATAdInfo atAdInfo) {
                    L.i("onRewardedVideoAdPlayClicked");
                }

                @Override
                public void onReward(ATAdInfo atAdInfo) {
                    L.i("onRewardedVideoAdPlayEnd");
                    if (listener != null)
                        listener.onRewarded(true);
                }
            });
    }

    //Native ad
    public static ATNative _atNative = null;

    public static void initNativeAd() {
        if (_config != null) {
            if (!StringUtil.isEmpty(_config.AD_REWARDED_VIDEO_PLACEMENT)) {
                _atNative = new ATNative(_application, _config.AD_NATIVE_PLACEMENT, new ATNativeNetworkListener() {
                    @Override
                    public void onNativeAdLoaded() {
                        L.i("onNativeAdLoaded");
                    }

                    @Override
                    public void onNativeAdLoadFail(AdError adError) {
                        // TODO: 2022/5/12 注意：禁止在此回调中执行广告的加载方法进行重试，否则会引起很多无用请求且可能会导致应用卡顿
                        L.i("onNativeAdLoadFail --> " + adError.getFullErrorInfo());
                    }
                });
            }
        }
    }

    public static void loadNative() {
        if (_atNative == null)
            initNativeAd();
        if (_atNative != null) {
            ATAdStatusInfo atAdStatusInfo = _atNative.checkAdStatus();
            if (atAdStatusInfo.isLoading() || atAdStatusInfo.isReady()) {

            } else {
                _atNative.makeAdRequest();
            }
        }
    }

    public static NativeAd getNativeAd() {
        if (_atNative != null) {
            ATAdStatusInfo atAdStatusInfo = _atNative.checkAdStatus();
            if (atAdStatusInfo.isReady()) {
                return _atNative.getNativeAd();
            }
        } else {
            loadNative();
        }
        return null;
    }

    public static View getNativeAdView(Context context) {
        return new CustomNativeAdView(context);
    }

    //AppOpen
    private static AdmobAppOpenManager _admobAppOpenManager = null;

    public static void initAdmobAppOpenManager() {
        if (_config != null) {
            if (!StringUtil.isEmpty(_config.AD_APP_OPEN_PLACEMENT_ID)) {
                _admobAppOpenManager = new AdmobAppOpenManager(_config.AD_APP_OPEN_PLACEMENT_ID);
            }
        }
    }

    public static void showAppOpenAd() {
        if (_admobAppOpenManager != null)
            if (_activity != null && _activity.get() != null)
                _admobAppOpenManager.showAdIfAvailable(_activity.get());
    }

    public static void loadAppOpenAd() {
        if (_admobAppOpenManager != null)
            _admobAppOpenManager.loadAd(_application);
    }

}
