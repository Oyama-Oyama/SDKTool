package com.roman.garden.adeasy;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;
import com.unity3d.services.ads.UnityAdsImplementation;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.BannerViewCache;
import com.unity3d.services.banners.UnityBannerSize;

final class UnityImpl implements AdImpl {

    private AdLoadListener _bannerListener = null;
    private AdLoadListener _interstitialListener = null;
    private AdLoadListener _videoListener = null;

    private PlatformConfig _platformConfig = null;

    private BannerView _bannerView = null;

    public UnityImpl(PlatformConfig _config){
        this._platformConfig = _config;
    }

    private boolean assetUnity(){
        if (UnityAds.isInitialized()){
            if (UnityAds.isSupported()){
                return true;
            } else {
                LogUtil.e("UnityAds SDK is not supported");
            }
        } else {
            LogUtil.e("UnityAds SDK has not initialized");
        }
        return false;
    }

    private boolean assetPlatformConfig(){
        if (_platformConfig != null)
            return true;
        LogUtil.e("UnityAds platform has not seted");
        return false;
    }

    @Override
    public void setupBannerListener(@NonNull AdLoadListener _listener) {
        this._bannerListener = _listener;
    }

    @Override
    public void loadBanner(Activity _activity) {
        if (assetUnity() && assetPlatformConfig()){
            try {
                BannerView __bannerView = new BannerView(_activity, _platformConfig.getBannerId().getAdId(), new UnityBannerSize(320, 50));
                __bannerView.setListener(new BannerView.IListener() {
                    @Override
                    public void onBannerLoaded(BannerView bannerView) {
                        _bannerView = bannerView;
                        if (_bannerListener != null)
                            _bannerListener.onAdLoaded(AdInfo.GROUP_UNITY, AdInfo.TYPE_BANNER);
                    }

                    @Override
                    public void onBannerClick(BannerView bannerView) {
                        if (_bannerListener != null)
                            _bannerListener.onAdClicked(AdInfo.GROUP_UNITY, AdInfo.TYPE_BANNER);
                    }

                    @Override
                    public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo bannerErrorInfo) {
                        if (_bannerListener != null)
                            _bannerListener.onAdLoadFailed(AdInfo.GROUP_UNITY, AdInfo.TYPE_BANNER, bannerErrorInfo.toString());
                    }

                    @Override
                    public void onBannerLeftApplication(BannerView bannerView) {
                        if (_bannerListener != null)
                            _bannerListener.onAdClicked(AdInfo.GROUP_UNITY, AdInfo.TYPE_BANNER);
                    }
                });
                __bannerView.load();
            } catch (Exception e){
                if (_bannerListener != null)
                    _bannerListener.onAdLoadFailed(AdInfo.GROUP_UNITY, AdInfo.TYPE_BANNER, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isBannerOk() {
        if (assetUnity() && assetPlatformConfig()){
            return _bannerView != null;
        }
        return false;
    }

    @Override
    public View getBannerView() {
        if (assetUnity() && assetPlatformConfig()){
            return _bannerView;
        }
        return null;
    }

    @Override
    public void destroyBannerView(@Nullable View view) {
        try{
            if (_bannerView != null) {
                if(_bannerView.getParent() != null){
                    ((ViewGroup)_bannerView.getParent()).removeView(_bannerView);
                }
                _bannerView.destroy();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            _bannerView = null;
        }
    }

    @Override
    public void setupInterstitialListener(@NonNull AdLoadListener _listener) {
        this._interstitialListener = _listener;
    }

    @Override
    public void loadInterstitial(Context _context) {
        if (assetUnity() && assetPlatformConfig()){
            UnityAds.load(_platformConfig.getInterstitialId().getAdId(), new IUnityAdsLoadListener() {
                @Override
                public void onUnityAdsAdLoaded(String s) {
                    if (_interstitialListener != null)
                        _interstitialListener.onAdLoaded(AdInfo.GROUP_UNITY, AdInfo.TYPE_INTERSTITIAL);
                }

                @Override
                public void onUnityAdsFailedToLoad(String s, UnityAds.UnityAdsLoadError unityAdsLoadError, String s1) {
                    if (_interstitialListener != null)
                        _interstitialListener.onAdLoadFailed(AdInfo.GROUP_UNITY, AdInfo.TYPE_INTERSTITIAL, unityAdsLoadError.toString());
                }
            });
        }
    }

    @Override
    public boolean isInterstitialOk() {
        if (assetUnity() && assetPlatformConfig()){
            return UnityAdsImplementation.isReady(_platformConfig.getInterstitialId().getAdId());
        }
        return false;
    }

    @Override
    public void showInterstitial(Activity _activity) {
        if (assetUnity() && assetPlatformConfig()) {
            UnityAds.show(_activity, _platformConfig.getInterstitialId().getAdId(), new IUnityAdsShowListener() {
                @Override
                public void onUnityAdsShowFailure(String s, UnityAds.UnityAdsShowError unityAdsShowError, String s1) {
                    if (_interstitialListener != null)
                        _interstitialListener.onAdShowFailed(AdInfo.GROUP_UNITY, AdInfo.TYPE_INTERSTITIAL, unityAdsShowError.toString());
                }

                @Override
                public void onUnityAdsShowStart(String s) {
                    if (_interstitialListener != null)
                        _interstitialListener.onAdOpened(AdInfo.GROUP_UNITY, AdInfo.TYPE_INTERSTITIAL);
                }

                @Override
                public void onUnityAdsShowClick(String s) {
                    if (_interstitialListener != null)
                        _interstitialListener.onAdClicked(AdInfo.GROUP_UNITY, AdInfo.TYPE_INTERSTITIAL);
                }

                @Override
                public void onUnityAdsShowComplete(String s, UnityAds.UnityAdsShowCompletionState unityAdsShowCompletionState) {
                    if (_interstitialListener != null)
                        _interstitialListener.onAdClosed(AdInfo.GROUP_UNITY, AdInfo.TYPE_INTERSTITIAL);
                }
            });
        }
    }

    @Override
    public void setupVideoListener(@NonNull AdLoadListener _listener) {
        this._videoListener = _listener;
    }

    @Override
    public void loadVideo(Context _context) {
        if (assetUnity() && assetPlatformConfig()){
            UnityAds.load(_platformConfig.getVideoId().getAdId(), new IUnityAdsLoadListener() {
                @Override
                public void onUnityAdsAdLoaded(String s) {
                    if (_videoListener != null)
                        _videoListener.onAdLoaded(AdInfo.GROUP_UNITY, AdInfo.TYPE_VIDEO);
                }

                @Override
                public void onUnityAdsFailedToLoad(String s, UnityAds.UnityAdsLoadError unityAdsLoadError, String s1) {
                    if (_videoListener != null)
                        _videoListener.onAdLoadFailed(AdInfo.GROUP_UNITY, AdInfo.TYPE_VIDEO, unityAdsLoadError.toString());
                }
            });
        }
    }

    @Override
    public boolean isVideoOk() {
        if (assetUnity() && assetPlatformConfig()) {
            return UnityAdsImplementation.isReady(_platformConfig.getVideoId().getAdId());
        }
        return false;
    }

    @Override
    public void showVideo(Activity _activity, RewardVideoResultListener listener) {
        if (assetUnity() && assetPlatformConfig()) {
            UnityAds.show(_activity, _platformConfig.getVideoId().getAdId(), new IUnityAdsShowListener() {
                @Override
                public void onUnityAdsShowFailure(String s, UnityAds.UnityAdsShowError unityAdsShowError, String s1) {
                    if (_videoListener != null)
                        _videoListener.onAdShowFailed(AdInfo.GROUP_UNITY, AdInfo.TYPE_VIDEO, unityAdsShowError.toString());
                }

                @Override
                public void onUnityAdsShowStart(String s) {
                    if(_videoListener != null)
                        _videoListener.onAdOpened(AdInfo.GROUP_UNITY, AdInfo.TYPE_VIDEO);
                }

                @Override
                public void onUnityAdsShowClick(String s) {
                    if (_videoListener != null)
                        _videoListener.onAdClicked(AdInfo.GROUP_UNITY, AdInfo.TYPE_VIDEO);
                }

                @Override
                public void onUnityAdsShowComplete(String s, UnityAds.UnityAdsShowCompletionState unityAdsShowCompletionState) {
                    if (_videoListener != null)
                        _videoListener.onAdClicked(AdInfo.GROUP_UNITY, AdInfo.TYPE_VIDEO);
                    if (unityAdsShowCompletionState == UnityAds.UnityAdsShowCompletionState.COMPLETED){
                        if (listener != null)   listener.onRewardVideoResult(true);
                    } else {
                        if (listener != null)   listener.onRewardVideoResult(false);
                    }
                }
            });
        } else {
            if (listener != null)
                listener.onRewardVideoResult(false);
        }
    }

    @Override
    public void destroy() {
        _bannerListener = null;
        _interstitialListener = null;
        _videoListener = null;
        destroyBannerView(null);
    }

}
