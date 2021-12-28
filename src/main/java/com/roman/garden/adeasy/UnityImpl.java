package com.roman.garden.adeasy;

import android.view.View;

import androidx.annotation.Nullable;

import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.ads.UnityAdsImplementation;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

final class UnityImpl extends BaseAdImpl {

    private BannerView _banner;

    @Override
    protected void initAdPlatform() {
        UnityAds.initialize(AdEasyImpl.of().getApplication(),
                getAppId(),
                isTestMode,
                new IUnityAdsInitializationListener() {
                    @Override
                    public void onInitializationComplete() {
                        loadBanner();
                        loadInterstitial();
                        loadVideo();
                        loadNative();
                    }

                    @Override
                    public void onInitializationFailed(UnityAds.UnityAdsInitializationError unityAdsInitializationError, String s) {
                        LogUtil.e("init unity error " + unityAdsInitializationError.toString());
                        retryInitAdPlatform();
                    }
                });
    }

    @Override
    protected boolean validPlatform() {
        return UnityAds.isInitialized() && UnityAds.isSupported();
    }

    @Override
    protected void loadBanner() {
        if (validPlatform()) {
            AdItem item = getBannerId();
            if (!validAdItem(item)) return;
            if (AdEasyImpl.of().getActivity() == null) {
                logError(item, AdInfo.TYPE_BANNER, " activity is null");
                reloadBanner();
                return;
            }
            BannerView _bannerView = new BannerView(AdEasyImpl.of().getActivity(),
                    item.getAdId(),
                    new UnityBannerSize(320, 50));
            _bannerView.setListener(new BannerView.IListener() {
                @Override
                public void onBannerLoaded(BannerView bannerView) {
                    logEvent(item, AdInfo.TYPE_BANNER);
                    _banner = bannerView;
                    setupBanner(item);
                }

                @Override
                public void onBannerClick(BannerView bannerView) {
                    logEvent(item, AdInfo.TYPE_BANNER, "clicked");
                }

                @Override
                public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo bannerErrorInfo) {
                    logError(item, AdInfo.TYPE_BANNER, bannerErrorInfo.errorMessage);
                    reloadBanner();
                }

                @Override
                public void onBannerLeftApplication(BannerView bannerView) {

                }
            });
            _bannerView.load();
        }
    }

    @Override
    protected void reloadBanner() {
        _banner = null;
        super.reloadBanner();
    }

    @Override
    protected View getBannerView() {
        return _banner;
    }

    @Override
    protected void loadInterstitial() {
        if (validPlatform()) {
            AdItem item = getInterstitialId();
            if (!validAdItem(item)) return;
            UnityAds.load(item.getAdId(), new IUnityAdsLoadListener() {
                @Override
                public void onUnityAdsAdLoaded(String s) {
                    logEvent(item, AdInfo.TYPE_INTERSTITIAL);
                    setupInterstitial(item);
                }

                @Override
                public void onUnityAdsFailedToLoad(String s, UnityAds.UnityAdsLoadError unityAdsLoadError, String s1) {
                    logError(item, AdInfo.TYPE_INTERSTITIAL, unityAdsLoadError.toString());
                    reloadInterstitial();
                }
            });
        }
    }

    @Override
    protected void showInterstitial() {
        AdItem item = getInterstitialId();
        if (AdEasyImpl.of().getActivity() != null &&
                validAdItem(item) &&
                UnityAdsImplementation.isReady(item.getAdId())) {
            UnityAds.show(AdEasyImpl.of().getActivity(), item.getAdId(), new IUnityAdsShowListener() {
                @Override
                public void onUnityAdsShowFailure(String s, UnityAds.UnityAdsShowError unityAdsShowError, String s1) {
                    reloadInterstitial();
                }

                @Override
                public void onUnityAdsShowStart(String s) {

                }

                @Override
                public void onUnityAdsShowClick(String s) {

                }

                @Override
                public void onUnityAdsShowComplete(String s, UnityAds.UnityAdsShowCompletionState unityAdsShowCompletionState) {
                    reloadInterstitial();
                }
            });
        }
    }

    @Override
    protected void loadNative() {

    }

    @Override
    protected View getNativeView() {
        return null;
    }

    @Override
    protected void loadVideo() {
        if (validPlatform()) {
            AdItem item = getVideoId();
            if (!validAdItem(item)) return;
            UnityAds.load(item.getAdId(), new IUnityAdsLoadListener() {
                @Override
                public void onUnityAdsAdLoaded(String s) {
                    logEvent(item, AdInfo.TYPE_VIDEO);
                    setupVideo(item);
                }

                @Override
                public void onUnityAdsFailedToLoad(String s, UnityAds.UnityAdsLoadError unityAdsLoadError, String s1) {
                    logError(item, AdInfo.TYPE_VIDEO, unityAdsLoadError.toString());
                    reloadVideo();
                }
            });
        }
    }

    @Override
    protected void showVideo(@Nullable @org.jetbrains.annotations.Nullable IVideoResultCallback callback) {
        AdItem item = getVideoId();
        if (AdEasyImpl.of().getActivity() != null &&
                validAdItem(item) &&
                UnityAdsImplementation.isReady(item.getAdId())) {
            UnityAds.show(AdEasyImpl.of().getActivity(), item.getAdId(), new IUnityAdsShowListener() {
                @Override
                public void onUnityAdsShowFailure(String s, UnityAds.UnityAdsShowError unityAdsShowError, String s1) {
                    reloadVideo();
                    if (callback != null) callback.onResult(false);
                }

                @Override
                public void onUnityAdsShowStart(String s) {

                }

                @Override
                public void onUnityAdsShowClick(String s) {

                }

                @Override
                public void onUnityAdsShowComplete(String s, UnityAds.UnityAdsShowCompletionState unityAdsShowCompletionState) {
                    if (unityAdsShowCompletionState == UnityAds.UnityAdsShowCompletionState.COMPLETED) {
                        if (callback != null) callback.onResult(true);
                    } else {
                        if (callback != null) callback.onResult(false);
                    }
                    reloadVideo();
                }
            });
        } else if (callback != null) callback.onResult(false);
    }


}
