package com.roman.garden.adeasy;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerView;
import com.vungle.warren.InitCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.VungleBanner;
import com.vungle.warren.error.VungleException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdEasy {

    private static AdEasy _instance = null;

    private boolean _initialized = false;
    private WeakReference<Activity> _activityImpl = null;
    private AdEasyActivityImpl _adEasyActivityImpl = null;
    private List<PlatformConfig> _platformConfigs = new ArrayList<>();

    private List<AdItem> _bannerIds = new ArrayList<>();
    private List<AdItem> _interstitialIds = new ArrayList<>();
    private List<AdItem> _videoIds = new ArrayList<>();
    private List<AdItem> _nativeIds = new ArrayList<>();

    private AdmobImpl _admobImpl = null;
    private UnityImpl _unityImpl = null;
    private VungleImpl _vungleImpl = null;

    private BannerAdLoadListener _bannerAdLoadListener = null;
    private InterstitialAdLoadListener _interstitialAdLoadListener = null;
    private VideoAdLoadListener _videoAdLoadListener = null;
    private NativeAdLoadListener _nativeAdLoadListener = null;

    private static final long AD_LOADER_DELAY = 10 * 1000;
    private static final long AD_LOADER_START_DELAY = 2 * 1000;

    public static AdEasy of() {
       if (_instance == null){
           synchronized (AdEasy.class){
               if (_instance == null)
                   _instance = new AdEasy();
           }
       }
        return _instance;
    };

    public static void init(@NonNull Application _application, @NonNull ADEasyApplicationImp _applicationImpl){
        init(_application, null, _applicationImpl);
    }

    public static void init(@NonNull Application _application, @Nullable String testDeviceId, @NonNull ADEasyApplicationImp _applicationImpl) {
        UIHandler.getInstance().removeCallbacksAndMessages(null);
        PlatformConfig _admob = _applicationImpl.createPlatformConfig(AdInfo.GROUP_ADMOB);
        if (_admob != null) {
            MobileAds.initialize(_application, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                    LogUtil.e("Admob SDK init result-->" + initializationStatus.toString());
                    of().initAdmobPlatform(_admob);
                }
            });
            if (!AdUtil.isEmpty(testDeviceId)){
                MobileAds.setRequestConfiguration(
                        new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList(testDeviceId))
                                .build());
            }
            of()._platformConfigs.add(_admob);
        }
        PlatformConfig _unity = _applicationImpl.createPlatformConfig(AdInfo.GROUP_UNITY);
        if (_unity != null) {
            UnityAds.initialize(_application, _unity.getAppId(), testDeviceId != null, true, new IUnityAdsInitializationListener() {
                @Override
                public void onInitializationComplete() {
                    LogUtil.e("Unity SDK init completed");
                    of().initUnityPlatform(_unity);
                }

                @Override
                public void onInitializationFailed(UnityAds.UnityAdsInitializationError unityAdsInitializationError, String s) {
                    LogUtil.e("Unity SDK init error-->" + unityAdsInitializationError.toString());
                }
            });
            of()._platformConfigs.add(_applicationImpl.createPlatformConfig(AdInfo.GROUP_UNITY));
        }
        PlatformConfig _vungle = _applicationImpl.createPlatformConfig(AdInfo.GROUP_VUNGLE);
        if (_vungle != null) {
            Vungle.init(_vungle.getAppId(), _application, new InitCallback() {
                @Override
                public void onSuccess() {
                    LogUtil.e("Vungle SDK init success");
                    of().initVunglePlatform(_vungle);
                }

                @Override
                public void onError(VungleException exception) {
                    LogUtil.e("Vungle SDK init error-->" + exception.toString());
                }

                @Override
                public void onAutoCacheAdAvailable(String placementId) {
                    LogUtil.e("Vungle SDK init has cached ad: onAutoCacheAdAvailable-->" + placementId);
                }
            });
            of()._platformConfigs.add(_vungle);
        }
        of().sortPlatformData();

    }

    public void onCreate(@NonNull Activity activity, @NonNull AdEasyActivityImpl _impl){
        _activityImpl = new WeakReference<>(activity);
        _adEasyActivityImpl = _impl;

        _initialized = true;
    }

    public void onResume(@NonNull Activity activity, @NonNull AdEasyActivityImpl _impl){
//        _activityImpl = new WeakReference<>(activity);
//        _adEasyActivityImpl = _impl;
//        _initialized = true;
        try {
            if (_admobImpl != null) {
                if (_admobImpl.getBannerView() != null)
                    ((AdView) _admobImpl.getBannerView()).resume();
            }
        } catch (Exception e){
            LogUtil.e("resume admob banner view error-->" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onPause(){
        try {
            if (_admobImpl != null) {
                if (_admobImpl.getBannerView() != null)
                    ((AdView) _admobImpl.getBannerView()).pause();
            }
        } catch (Exception e){
            LogUtil.e("pause admob banner view error-->" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onDestroy(){
        _initialized = false;
        UIHandler.getInstance().removeCallbacksAndMessages(null);

        _bannerAdLoadListener = null;
        _interstitialAdLoadListener = null;
        _videoAdLoadListener = null;

        if (_admobImpl != null)
            _admobImpl.destroy();
        if (_unityImpl != null)
            _unityImpl.destroy();
        if (_vungleImpl != null)
            _vungleImpl.destroy();
        _admobImpl = null;
        _unityImpl = null;
        _vungleImpl = null;

        _adEasyActivityImpl = null;
        _platformConfigs.clear();
        _bannerIds.clear();
        _interstitialIds.clear();
        _videoIds.clear();
        _nativeIds.clear();
    }

    private boolean isInitialized(){
        if (!_initialized){
            LogUtil.e("AdEasy not Initialized");
        }
        return _initialized;
    }

    private void sortPlatformData(){
        for (PlatformConfig pc : _platformConfigs){
            if (!AdUtil.isAdIdEmpty(pc.getBannerId()))
                _bannerIds.add(pc.getBannerId());
            if (!AdUtil.isAdIdEmpty(pc.getInterstitialId()))
                _interstitialIds.add(pc.getInterstitialId());
            if (!AdUtil.isAdIdEmpty(pc.getVideoId()))
                _videoIds.add(pc.getVideoId());
            if (!AdUtil.isAdIdEmpty(pc.getNativeId()))
                _nativeIds.add(pc.getVideoId());
        }
        Collections.sort(_bannerIds, new Comparator<AdItem>() {
            @Override
            public int compare(AdItem o1, AdItem o2) {
                return o1.getAdWidget() - o2.getAdWidget();
            }
        });
        Collections.sort(_interstitialIds, new Comparator<AdItem>() {
            @Override
            public int compare(AdItem o1, AdItem o2) {
                return o1.getAdWidget() - o2.getAdWidget();
            }
        });
        Collections.sort(_videoIds, new Comparator<AdItem>() {
            @Override
            public int compare(AdItem o1, AdItem o2) {
                return o1.getAdWidget() - o2.getAdWidget();
            }
        });
        Collections.sort(_nativeIds, new Comparator<AdItem>() {
            @Override
            public int compare(AdItem o1, AdItem o2) {
                return o1.getAdWidget() - o2.getAdWidget();
            }
        });
    }

    public BannerAdLoadListener getBannerAdLoadListener() {
        if (_bannerAdLoadListener == null)
            _bannerAdLoadListener = new BannerAdLoadListener();
        return _bannerAdLoadListener;
    }

    public InterstitialAdLoadListener getInterstitialAdLoadListener() {
        if (_interstitialAdLoadListener == null)
            _interstitialAdLoadListener = new InterstitialAdLoadListener();
        return _interstitialAdLoadListener;
    }

    public VideoAdLoadListener getVideoAdLoadListener() {
        if (_videoAdLoadListener == null)
            _videoAdLoadListener = new VideoAdLoadListener();
        return _videoAdLoadListener;
    }

    public NativeAdLoadListener getNativeAdLoadListener() {
        if (_nativeAdLoadListener == null)
            _nativeAdLoadListener = new NativeAdLoadListener();
        return _nativeAdLoadListener;
    }

    private void startLoadAds(String _adGroup){
        startAdLoaderRunnable(_adGroup, AdInfo.TYPE_BANNER, 0l);
        startAdLoaderRunnable(_adGroup, AdInfo.TYPE_INTERSTITIAL, 0l);
        startAdLoaderRunnable(_adGroup, AdInfo.TYPE_VIDEO, 0l);
        startAdLoaderRunnable(_adGroup, AdInfo.TYPE_NATIVE, 0l);
    }

    private synchronized void startAdLoaderRunnable(String _adGroup, String _adType, long _delay){
        LogUtil.e("start " + _adGroup + "  loader, adType->" + _adType);
        if (_adGroup == AdInfo.GROUP_ADMOB){
            if (_initialized) {
                runAdmobLoader(_adType, _delay);
            } else {
                LogUtil.e("error start " + _adGroup + "  loader, adType->" + _adType + ", do you have initialized !!!");
                UIHandler.getInstance().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startAdLoaderRunnable(_adGroup, _adType, _delay);
                    }
                }, AD_LOADER_START_DELAY);
            }
        } else if(_adGroup == AdInfo.GROUP_UNITY){
            if (_initialized) {
                runUnityLoader(_adType, _delay);
            } else {
                LogUtil.e("error start " + _adGroup + "  loader, adType->" + _adType + ", do you have initialized !!!");
                UIHandler.getInstance().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startAdLoaderRunnable(_adGroup, _adType, _delay);
                    }
                }, AD_LOADER_START_DELAY);
            }
        } else if(_adGroup == AdInfo.GROUP_VUNGLE){
            if (_initialized) {
                runVungleLoader(_adType, _delay);
            } else {
                LogUtil.e("error start " + _adGroup + "  loader, adType->" + _adType + ", do you have initialized !!!");
                UIHandler.getInstance().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startAdLoaderRunnable(_adGroup, _adType, _delay);
                    }
                }, AD_LOADER_START_DELAY);
            }
        }
    }

    private void initAdmobPlatform(PlatformConfig _platform){
        if (_admobImpl == null) {
            _admobImpl = new AdmobImpl(_platform);
            _admobImpl.setupBannerListener(getBannerAdLoadListener());
            _admobImpl.setupInterstitialListener(getInterstitialAdLoadListener());
            _admobImpl.setupVideoListener(getVideoAdLoadListener());
            _admobImpl.setupNativeListener(getNativeAdLoadListener());
        }
        startLoadAds(AdInfo.GROUP_ADMOB);
    }

    private void initVunglePlatform(PlatformConfig _platform){
        if (_vungleImpl == null) {
            _vungleImpl = new VungleImpl(_platform);
            _vungleImpl.setupBannerListener(getBannerAdLoadListener());
            _vungleImpl.setupInterstitialListener(getInterstitialAdLoadListener());
            _vungleImpl.setupVideoListener(getVideoAdLoadListener());
            _vungleImpl.setupNativeListener(getNativeAdLoadListener());
        }
        startLoadAds(AdInfo.GROUP_VUNGLE);
    }

    private void initUnityPlatform(PlatformConfig _platform){
        if (_unityImpl == null) {
            _unityImpl = new UnityImpl(_platform);
            _unityImpl.setupBannerListener(getBannerAdLoadListener());
            _unityImpl.setupInterstitialListener(getInterstitialAdLoadListener());
            _unityImpl.setupVideoListener(getVideoAdLoadListener());
            _unityImpl.setupNativeListener(getNativeAdLoadListener());
        }
        startLoadAds(AdInfo.GROUP_UNITY);
    }

    private void runAdmobLoader(String _adType, long _delay){
        if(_adType == AdInfo.TYPE_BANNER){
            UIHandler.getInstance().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(_admobImpl != null && !_admobImpl.isBannerOk() && _activityImpl != null && _activityImpl.get() != null){
                        _admobImpl.loadBanner(_activityImpl.get());
                    }
                }
            }, _delay);
        } else if(_adType == AdInfo.TYPE_INTERSTITIAL){
            UIHandler.getInstance().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(_admobImpl != null && !_admobImpl.isInterstitialOk() && _activityImpl != null && _activityImpl.get() != null){
                        _admobImpl.loadInterstitial(_activityImpl.get());
                    }
                }
            }, _delay);
        } else if(_adType == AdInfo.TYPE_VIDEO){
            UIHandler.getInstance().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(_admobImpl != null && !_admobImpl.isVideoOk() && _activityImpl != null && _activityImpl.get() != null){
                        _admobImpl.loadVideo(_activityImpl.get());
                    }
                }
            }, _delay);
        } else if (_adType == AdInfo.TYPE_NATIVE){
            UIHandler.getInstance().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(_admobImpl != null && !_admobImpl.isVideoOk() && _activityImpl != null && _activityImpl.get() != null){
                        _admobImpl.loadNative(_activityImpl.get());
                    }
                }
            }, _delay);
        }
    }

    private void runUnityLoader(String _adType, long _delay){
        if(_adType == AdInfo.TYPE_BANNER){
            UIHandler.getInstance().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (_unityImpl != null && !_unityImpl.isBannerOk() && _activityImpl != null && _activityImpl.get() != null)
                        _unityImpl.loadBanner(_activityImpl.get());
                }
            }, _delay);
        } else if(_adType == AdInfo.TYPE_INTERSTITIAL){
            UIHandler.getInstance().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (_unityImpl != null && !_unityImpl.isInterstitialOk())
                        _unityImpl.loadInterstitial(null);
                }
            }, _delay);
        } else if(_adType == AdInfo.TYPE_VIDEO){
            UIHandler.getInstance().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (_unityImpl != null && !_unityImpl.isVideoOk())
                        _unityImpl.loadVideo(null);
                }
            }, _delay);
        } else if(_adType == AdInfo.TYPE_NATIVE){
            // TODO: 2021/12/9  nothing
        }
    }

    private void runVungleLoader(String _adType, long _delay){
        if(_adType == AdInfo.TYPE_BANNER){
            UIHandler.getInstance().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (_vungleImpl != null && !_vungleImpl.isBannerOk())
                        _vungleImpl.loadBanner(null);
                }
            }, _delay);
        } else if(_adType == AdInfo.TYPE_INTERSTITIAL){
            UIHandler.getInstance().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (_vungleImpl != null && !_vungleImpl.isInterstitialOk())
                        _vungleImpl.loadInterstitial(null);
                }
            }, _delay);
        } else if(_adType == AdInfo.TYPE_VIDEO){
            UIHandler.getInstance().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (_vungleImpl != null && !_vungleImpl.isVideoOk())
                        _vungleImpl.loadVideo(null);
                }
            }, _delay);
        } else if(_adType == AdInfo.TYPE_NATIVE){
            UIHandler.getInstance().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (_vungleImpl != null && !_vungleImpl.isVideoOk())
                        _vungleImpl.loadNative(null);
                }
            }, _delay);
        }
    }

    private class BannerAdLoadListener implements AdLoadListener{

        @Override
        public void onAdLoaded(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_LOADED, _adGroup, _adType));
        }

        @Override
        public void onAdLoadFailed(String _adGroup, String _adType, String _error) {
            LogUtil.e(AdInfo.makeExtraMsg(AdInfo.EVENT_AD_LOAD_FAILED, _adGroup, _adType, "error", _error));
            startAdLoaderRunnable(_adGroup, _adType, AD_LOADER_DELAY);
        }

        @Override
        public void onAdClosed(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_CLOSED, _adGroup, _adType));
            startAdLoaderRunnable(_adGroup, _adType, AD_LOADER_DELAY);
        }

        @Override
        public void onAdOpened(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_OPENED, _adGroup, _adType));
        }

        @Override
        public void onAdImpression(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_IMPRESSION, _adGroup, _adType));
        }

        @Override
        public void onAdClicked(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_CLICKED, _adGroup, _adType));
        }

        @Override
        public void onAdShowFailed(String _adGroup, String _adType, String error) {
            LogUtil.e(AdInfo.makeExtraMsg(AdInfo.EVENT_AD_SHOW_FAILED, _adGroup, _adType, "error", error));
            startAdLoaderRunnable(_adGroup, _adType, AD_LOADER_DELAY);
        }
    };

    private class InterstitialAdLoadListener implements AdLoadListener{

        @Override
        public void onAdLoaded(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_LOADED, _adGroup, _adType));
        }

        @Override
        public void onAdLoadFailed(String _adGroup, String _adType, String _error) {
            LogUtil.e(AdInfo.makeExtraMsg(AdInfo.EVENT_AD_LOAD_FAILED, _adGroup, _adType, "error", _error));
            startAdLoaderRunnable(_adGroup, _adType, AD_LOADER_DELAY);
        }

        @Override
        public void onAdClosed(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_CLOSED, _adGroup, _adType));
            startAdLoaderRunnable(_adGroup, _adType, AD_LOADER_DELAY);
        }

        @Override
        public void onAdOpened(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_OPENED, _adGroup, _adType));
        }

        @Override
        public void onAdImpression(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_IMPRESSION, _adGroup, _adType));
        }

        @Override
        public void onAdClicked(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_CLICKED, _adGroup, _adType));
        }

        @Override
        public void onAdShowFailed(String _adGroup, String _adType, String error) {
            LogUtil.e(AdInfo.makeExtraMsg(AdInfo.EVENT_AD_SHOW_FAILED, _adGroup, _adType, "error", error));
            startAdLoaderRunnable(_adGroup, _adType, AD_LOADER_DELAY);
        }
    }

    private class VideoAdLoadListener implements AdLoadListener{

        @Override
        public void onAdLoaded(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_LOADED, _adGroup, _adType));
        }

        @Override
        public void onAdLoadFailed(String _adGroup, String _adType, String _error) {
            LogUtil.e(AdInfo.makeExtraMsg(AdInfo.EVENT_AD_LOAD_FAILED, _adGroup, _adType, "error", _error));
            startAdLoaderRunnable(_adGroup, _adType, AD_LOADER_DELAY);
        }

        @Override
        public void onAdClosed(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_CLOSED, _adGroup, _adType));
            startAdLoaderRunnable(_adGroup, _adType, AD_LOADER_DELAY);
        }

        @Override
        public void onAdOpened(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_OPENED, _adGroup, _adType));
        }

        @Override
        public void onAdImpression(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_IMPRESSION, _adGroup, _adType));
        }

        @Override
        public void onAdClicked(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_CLICKED, _adGroup, _adType));
        }

        @Override
        public void onAdShowFailed(String _adGroup, String _adType, String error) {
            LogUtil.e(AdInfo.makeExtraMsg(AdInfo.EVENT_AD_SHOW_FAILED, _adGroup, _adType, "error", error));
            startAdLoaderRunnable(_adGroup, _adType, AD_LOADER_DELAY);
        }
    }

    private class NativeAdLoadListener implements AdLoadListener{

        @Override
        public void onAdLoaded(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_LOADED, _adGroup, _adType));
        }

        @Override
        public void onAdLoadFailed(String _adGroup, String _adType, String _error) {
            LogUtil.e(AdInfo.makeExtraMsg(AdInfo.EVENT_AD_LOAD_FAILED, _adGroup, _adType, "error", _error));
            startAdLoaderRunnable(_adGroup, _adType, AD_LOADER_DELAY);
        }

        @Override
        public void onAdClosed(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_CLOSED, _adGroup, _adType));
        }

        @Override
        public void onAdOpened(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_OPENED, _adGroup, _adType));
        }

        @Override
        public void onAdImpression(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_IMPRESSION, _adGroup, _adType));
        }

        @Override
        public void onAdClicked(String _adGroup, String _adType) {
            LogUtil.e(AdInfo.makeMsg(AdInfo.EVENT_AD_CLICKED, _adGroup, _adType));
        }

        @Override
        public void onAdShowFailed(String _adGroup, String _adType, String error) {
            LogUtil.e(AdInfo.makeExtraMsg(AdInfo.EVENT_AD_SHOW_FAILED, _adGroup, _adType, "error", error));
        }
    };

    public boolean hasBanner(){
        if (!isInitialized())
            return false;
        if (_bannerIds.size() <= 0)
            return false;
        for(AdItem _item : _bannerIds){
            if (_item.getAdGroup() == AdInfo.GROUP_ADMOB){
                if (_admobImpl != null && _admobImpl.isBannerOk())
                    return true;
            } else if(_item.getAdGroup() == AdInfo.GROUP_UNITY){
                if (_unityImpl != null && _unityImpl.isBannerOk())
                    return true;
            } else if(_item.getAdGroup() == AdInfo.GROUP_VUNGLE){
                if (_vungleImpl != null && _vungleImpl.isBannerOk())
                    return true;
            }
        }
        return false;
    }

    private View getBannerView(){
        if (_bannerIds.size() <= 0)
            return null;
        for(AdItem _item : _bannerIds){
            if (_item.getAdGroup() == AdInfo.GROUP_ADMOB){
                if (_admobImpl != null && _admobImpl.isBannerOk()) {
                    return _admobImpl.getBannerView();
                }
            } else if(_item.getAdGroup() == AdInfo.GROUP_UNITY){
                if (_unityImpl != null && _unityImpl.isBannerOk()) {
                    return _unityImpl.getBannerView();
                }
            } else if(_item.getAdGroup() == AdInfo.GROUP_VUNGLE){
                if (_vungleImpl != null && _vungleImpl.isBannerOk()) {
                    return _vungleImpl.getBannerView();
                }
            }
        }
        return null;
    }

    public void showBanner(){
        if (!isInitialized())
            return;
        if(_adEasyActivityImpl != null){
            if (_adEasyActivityImpl.getBannerContainer() != null){
                View _bannerView = getBannerView();
                if (_bannerView != null){
                    if (_bannerView.getParent() != null){
                        ((ViewGroup)_bannerView.getParent()).removeView(_bannerView);
                    }
                    _adEasyActivityImpl.getBannerContainer().addView(_bannerView);
                }
            }
        }
    }

    public void hideBanner(){
        if (!isInitialized())
            return;
        if(_adEasyActivityImpl != null) {
            if (_adEasyActivityImpl.getBannerContainer() != null) {
                View view = ((ViewGroup)_adEasyActivityImpl.getBannerContainer()).getChildAt(0);
                if (view != null) {
                    if (view instanceof AdView) {
                        if (_admobImpl != null)
                            _admobImpl.destroyBannerView(view);
                        startAdLoaderRunnable(AdInfo.GROUP_ADMOB, AdInfo.TYPE_BANNER, AD_LOADER_DELAY);
                    } else if (view instanceof VungleBanner) {
                        if (_admobImpl != null)
                            _admobImpl.destroyBannerView(view);
                        startAdLoaderRunnable(AdInfo.GROUP_UNITY, AdInfo.TYPE_BANNER, AD_LOADER_DELAY);
                    } else if (view instanceof BannerView) {
                        if (_admobImpl != null)
                            _admobImpl.destroyBannerView(view);
                        startAdLoaderRunnable(AdInfo.GROUP_UNITY, AdInfo.TYPE_BANNER, AD_LOADER_DELAY);
                    } else {
//                        try{
//                            if (view != null) {
//                                if(view.getParent() != null){
//                                    ((ViewGroup)view.getParent()).removeView(view);
//                                }
//                            }
//                        } catch (Exception e){
//                            e.printStackTrace();
//                        } finally {
//
//                        }
                    }
                }
            }
        }
    }

    public boolean hasInterstitial(){
        if (!isInitialized())
            return false;
        if (_interstitialIds.size() <= 0)
            return false;
        for(AdItem _item : _interstitialIds){
            if (_item.getAdGroup() == AdInfo.GROUP_ADMOB){
                if (_admobImpl != null && _admobImpl.isInterstitialOk())
                    return true;
            } else if(_item.getAdGroup() == AdInfo.GROUP_UNITY){
                if (_unityImpl != null && _unityImpl.isInterstitialOk())
                    return true;
            } else if(_item.getAdGroup() == AdInfo.GROUP_VUNGLE){
                if (_vungleImpl != null && _vungleImpl.isInterstitialOk())
                    return true;
            }
        }
        return false;
    }

    public void showInterstitial(){
        if (!isInitialized())
            return;
        if (_interstitialIds.size() <= 0)
            return;
        for(AdItem _item : _interstitialIds){
            if (_item.getAdGroup() == AdInfo.GROUP_ADMOB){
                if (_admobImpl != null && _admobImpl.isInterstitialOk() && _activityImpl != null && _activityImpl.get() != null) {
                    _admobImpl.showInterstitial(_activityImpl.get());
                    return;
                }
            } else if(_item.getAdGroup() == AdInfo.GROUP_UNITY){
                if (_unityImpl != null && _unityImpl.isInterstitialOk() && _activityImpl != null && _activityImpl.get() != null) {
                    _unityImpl.showInterstitial(_activityImpl.get());
                    return;
                }
            } else if(_item.getAdGroup() == AdInfo.GROUP_VUNGLE){
                if (_vungleImpl != null && _vungleImpl.isInterstitialOk()) {
                    _vungleImpl.showInterstitial(null);
                    return;
                }
            }
        }
    }

    public boolean hasVideo(){
        if (!isInitialized())
            return false;
        if (_videoIds.size() <= 0)
            return false;
        for(AdItem _item : _videoIds){
            if (_item.getAdGroup() == AdInfo.GROUP_ADMOB){
                if (_admobImpl != null && _admobImpl.isVideoOk())
                    return true;
            } else if(_item.getAdGroup() == AdInfo.GROUP_UNITY){
                if (_unityImpl != null && _unityImpl.isVideoOk())
                    return true;
            } else if(_item.getAdGroup() == AdInfo.GROUP_VUNGLE){
                if (_vungleImpl != null && _vungleImpl.isVideoOk())
                    return true;
            }
        }
        return false;
    }

    public void showVideo(RewardVideoResultListener listener){
        LogUtil.e("showVideoAd");
        if (!isInitialized())
            return;
        if (_videoIds.size() <= 0)
            return;
        for(AdItem _item : _videoIds){
            if (_item.getAdGroup() == AdInfo.GROUP_ADMOB){
                if (_admobImpl != null && _admobImpl.isInterstitialOk() && _activityImpl != null && _activityImpl.get() != null) {
                    _admobImpl.showVideo(_activityImpl.get(), listener);
                    return;
                }
            } else if(_item.getAdGroup() == AdInfo.GROUP_UNITY){
                if (_unityImpl != null && _unityImpl.isInterstitialOk() && _activityImpl != null && _activityImpl.get() != null) {
                    _unityImpl.showVideo(_activityImpl.get(), listener);
                    return;
                }
            } else if(_item.getAdGroup() == AdInfo.GROUP_VUNGLE){
                if (_vungleImpl != null && _vungleImpl.isInterstitialOk()) {
                    _vungleImpl.showVideo(null, listener);
                    return;
                }
            }
        }
    }

    public boolean hasNativeAd(){
        if (!isInitialized())
            return false;
        if (_nativeIds.size() <= 0)
            return false;
        for (AdItem _item : _nativeIds){
            if (_item.getAdGroup() == AdInfo.GROUP_ADMOB){
                if (_admobImpl != null && _admobImpl.isNativeOk())
                    return true;
            } else if(_item.getAdGroup() == AdInfo.GROUP_UNITY){
                if (_unityImpl != null && _unityImpl.isNativeOk())
                    return true;
            } else if(_item.getAdGroup() == AdInfo.GROUP_VUNGLE){
                if (_vungleImpl != null && _vungleImpl.isNativeOk())
                    return true;
            }
        }
        return false;
    }

    public View getNativeAdView(){
        if (!isInitialized())
            return null;
        if (_nativeIds.size() <= 0)
            return null;
        for (AdItem _item : _nativeIds){
            if (_item.getAdGroup() == AdInfo.GROUP_ADMOB){
                if (_admobImpl != null && _admobImpl.isNativeOk())
                    startAdLoaderRunnable(AdInfo.GROUP_ADMOB, AdInfo.TYPE_NATIVE, AD_LOADER_DELAY * 3);
                    return _admobImpl.getNativeView();
            } else if(_item.getAdGroup() == AdInfo.GROUP_UNITY){
                if (_unityImpl != null && _unityImpl.isNativeOk())
                    startAdLoaderRunnable(AdInfo.GROUP_UNITY, AdInfo.TYPE_NATIVE, AD_LOADER_DELAY * 3);
                    return _unityImpl.getNativeView();
            } else if(_item.getAdGroup() == AdInfo.GROUP_VUNGLE){
                if (_vungleImpl != null && _vungleImpl.isNativeOk())
                    startAdLoaderRunnable(AdInfo.GROUP_VUNGLE, AdInfo.TYPE_NATIVE, AD_LOADER_DELAY * 3);
                    return _vungleImpl.getNativeView();
            }
        }
        return null;
    }

}
