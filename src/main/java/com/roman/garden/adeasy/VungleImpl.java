package com.roman.garden.adeasy;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vungle.warren.AdConfig;
import com.vungle.warren.BannerAdConfig;
import com.vungle.warren.Banners;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.VungleBanner;
import com.vungle.warren.error.VungleException;

final class VungleImpl implements AdImpl {

    private AdLoadListener _bannerListener = null;
    private AdLoadListener _interstitialListener = null;
    private AdLoadListener _videoListener = null;

    private PlatformConfig _platformConfig;

    private volatile boolean _isVideoReward = false;

    public VungleImpl(PlatformConfig _config){
        this._platformConfig = _config;
    }

    private boolean assetVungleInit(){
        if (Vungle.isInitialized())
            return true;
        LogUtil.e("Vungle not initialized");
        return false;
    }

    private boolean assetPlatformConfig(){
        if (_platformConfig != null)
            return true;
        LogUtil.e("Vungle platform had not seted");
        return false;
    }

    @Override
    public void setupBannerListener(@NonNull AdLoadListener _listener) {
        this._bannerListener = _listener;
    }

    @Override
    public void loadBanner(Activity _activity) {
        if (assetVungleInit() && assetPlatformConfig()){
                Banners.loadBanner(_platformConfig.getBannerId().getAdId(), new BannerAdConfig(AdConfig.AdSize.BANNER), new LoadAdCallback() {
                    @Override
                    public void onAdLoad(String placementId) {
                        if(_bannerListener != null)
                            _bannerListener.onAdLoaded(AdInfo.GROUP_VUNGLE, AdInfo.TYPE_BANNER);
                    }

                    @Override
                    public void onError(String placementId, VungleException exception) {
                        if (_bannerListener != null)
                            _bannerListener.onAdLoadFailed(AdInfo.GROUP_VUNGLE, AdInfo.TYPE_BANNER, exception.toString());
                    }
                });
        }
    }

    @Override
    public boolean isBannerOk() {
        if (assetVungleInit() && assetPlatformConfig())
            return Vungle.canPlayAd(_platformConfig.getBannerId().getAdId());
        return false;
    }

    @Override
    public View getBannerView() {
        if (assetVungleInit() && assetPlatformConfig()){
            return Banners.getBanner(_platformConfig.getBannerId().getAdId(), new BannerAdConfig(AdConfig.AdSize.BANNER), new PlayAdCallback() {
                @Override
                public void creativeId(String creativeId) {
                    //当调用playAd方法时，立刻触发。该回调会先于onAdStart回调触发。开发者可以通过该回调获知Vungle即将播放的广告素材ID。
                }

                @Override
                public void onAdStart(String placementId) {
                    //当SDK有可播放的广告且即将播放广告时触发
                }

                @Override
                public void onAdEnd(String placementId, boolean completed, boolean isCTAClicked) {
                    //当广告关闭，且将返回用用时触发
                }

                @Override
                public void onAdEnd(String placementId) {
                    //当广告关闭
                }

                @Override
                public void onAdClick(String placementId) {
                    //当用户点击广告视频或广告的下载按钮时触发
                    if (_bannerListener != null)
                        _bannerListener.onAdClicked(AdInfo.GROUP_VUNGLE, AdInfo.TYPE_BANNER);
                }

                @Override
                public void onAdRewarded(String placementId) {
                    //当用户观看完80%的奖励视频广告后用户时触发。该回调只对奖励广告位起作用。
                }

                @Override
                public void onAdLeftApplication(String placementId) {
                    //当点击下载按钮离开应用时触发
                    if (_bannerListener != null)
                        _bannerListener.onAdImpression(AdInfo.GROUP_VUNGLE, AdInfo.TYPE_BANNER);
                }

                @Override
                public void onError(String placementId, VungleException exception) {
                    //当初始化失败时触发，此时请查看错误信息getLocalizedMessage以便获知getExceptionCode 。
                    if (_bannerListener != null)
                        _bannerListener.onAdShowFailed(AdInfo.GROUP_VUNGLE, AdInfo.TYPE_BANNER, exception.toString());
                }

                @Override
                public void onAdViewed(String placementId) {
                    //当SDK将广告渲染时触发
                    if (_bannerListener != null)
                        _bannerListener.onAdOpened(AdInfo.GROUP_VUNGLE, AdInfo.TYPE_BANNER);
                }
            });
        }
        return null;
    }

    @Override
    public void destroyBannerView(@Nullable View view) {
        try{
            if (view != null) {
                if(view.getParent() != null){
                    ((ViewGroup)view.getParent()).removeView(view);
                }
                ((VungleBanner)view).destroyAd();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            view = null;
        }
    }

    @Override
    public void setupInterstitialListener(@NonNull AdLoadListener _listener) {
        this._interstitialListener = _listener;
    }

    @Override
    public void loadInterstitial(Context _context) {
        if (assetVungleInit() && assetPlatformConfig()){
            Vungle.loadAd(_platformConfig.getInterstitialId().getAdId(), new LoadAdCallback() {
                @Override
                public void onAdLoad(String placementId) {
                    if (_interstitialListener != null)
                        _interstitialListener.onAdLoaded(AdInfo.GROUP_VUNGLE, AdInfo.TYPE_INTERSTITIAL);
                }

                @Override
                public void onError(String placementId, VungleException exception) {
                    if (_interstitialListener != null)
                        _interstitialListener.onAdLoadFailed(AdInfo.GROUP_VUNGLE, AdInfo.TYPE_INTERSTITIAL, exception.toString());
                }
            });
        }
    }

    @Override
    public boolean isInterstitialOk() {
        if (assetVungleInit() && assetPlatformConfig())
            return Vungle.canPlayAd(_platformConfig.getInterstitialId().getAdId());
        return false;
    }

    @Override
    public void showInterstitial(Activity _activity) {
        if (assetVungleInit() && assetPlatformConfig()){
            Vungle.playAd(_platformConfig.getInterstitialId().getAdId(), null, new PlayAdCallback() {
                @Override
                public void creativeId(String creativeId) {
                    //当调用playAd方法时，立刻触发。该回调会先于onAdStart回调触发。开发者可以通过该回调获知Vungle即将播放的广告素材ID。
                }

                @Override
                public void onAdStart(String placementId) {
                    //当SDK有可播放的广告且即将播放广告时触发
                }

                @Override
                public void onAdEnd(String placementId, boolean completed, boolean isCTAClicked) {
                    //当广告关闭，且将返回用用时触发
                }

                @Override
                public void onAdEnd(String placementId) {
                    //当广告关闭
                }

                @Override
                public void onAdClick(String placementId) {
                    //当用户点击广告视频或广告的下载按钮时触发
                    if (_interstitialListener != null)
                        _interstitialListener.onAdClicked(AdInfo.GROUP_VUNGLE, AdInfo.TYPE_INTERSTITIAL);
                }

                @Override
                public void onAdRewarded(String placementId) {
                    //当用户观看完80%的奖励视频广告后用户时触发。该回调只对奖励广告位起作用。
                }

                @Override
                public void onAdLeftApplication(String placementId) {
                    //当点击下载按钮离开应用时触发
                    if (_interstitialListener != null)
                        _interstitialListener.onAdImpression(AdInfo.GROUP_VUNGLE, AdInfo.TYPE_INTERSTITIAL);
                }

                @Override
                public void onError(String placementId, VungleException exception) {
                    //当初始化失败时触发，此时请查看错误信息getLocalizedMessage以便获知getExceptionCode 。
                    if (_interstitialListener != null)
                        _interstitialListener.onAdShowFailed(AdInfo.GROUP_VUNGLE, AdInfo.TYPE_INTERSTITIAL, exception.toString());
                }

                @Override
                public void onAdViewed(String placementId) {
                    //当SDK将广告渲染时触发
                    if (_interstitialListener != null)
                        _interstitialListener.onAdOpened(AdInfo.GROUP_VUNGLE, AdInfo.TYPE_INTERSTITIAL);
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
        if (assetVungleInit() && assetPlatformConfig()){
            Vungle.loadAd(_platformConfig.getVideoId().getAdId(), new LoadAdCallback() {
                @Override
                public void onAdLoad(String placementId) {
                    if (_videoListener != null)
                        _videoListener.onAdLoaded(AdInfo.GROUP_VUNGLE, AdInfo.TYPE_VIDEO);
                }

                @Override
                public void onError(String placementId, VungleException exception) {
                    if (_videoListener != null)
                        _videoListener.onAdLoadFailed(AdInfo.GROUP_VUNGLE, AdInfo.TYPE_VIDEO, exception.toString());
                }
            });
        }
    }

    @Override
    public boolean isVideoOk() {
        if (assetVungleInit() && assetPlatformConfig())
            return Vungle.canPlayAd(_platformConfig.getVideoId().getAdId());
        return false;
    }

    @Override
    public void showVideo(Activity _activity, RewardVideoResultListener listener) {
        _isVideoReward = false;
        if (assetVungleInit() && assetPlatformConfig()){
            Vungle.playAd(_platformConfig.getVideoId().getAdId(), null, new PlayAdCallback() {
                @Override
                public void creativeId(String creativeId) {
                    //当调用playAd方法时，立刻触发。该回调会先于onAdStart回调触发。开发者可以通过该回调获知Vungle即将播放的广告素材ID。
                }

                @Override
                public void onAdStart(String placementId) {
                    //当SDK有可播放的广告且即将播放广告时触发
                }

                @Override
                public void onAdEnd(String placementId, boolean completed, boolean isCTAClicked) {
                    //当广告关闭，且将返回用用时触发

                }

                @Override
                public void onAdEnd(String placementId) {
                    //当广告关闭
                    if (listener != null)
                        listener.onRewardVideoResult(_isVideoReward);
                }

                @Override
                public void onAdClick(String placementId) {
                    //当用户点击广告视频或广告的下载按钮时触发
                    if (_interstitialListener != null)
                        _interstitialListener.onAdClicked(AdInfo.GROUP_VUNGLE, AdInfo.TYPE_INTERSTITIAL);
                }

                @Override
                public void onAdRewarded(String placementId) {
                    //当用户观看完80%的奖励视频广告后用户时触发。该回调只对奖励广告位起作用。
                    _isVideoReward = true;
                }

                @Override
                public void onAdLeftApplication(String placementId) {
                    //当点击下载按钮离开应用时触发
                    if (_interstitialListener != null)
                        _interstitialListener.onAdImpression(AdInfo.GROUP_VUNGLE, AdInfo.TYPE_INTERSTITIAL);
                }

                @Override
                public void onError(String placementId, VungleException exception) {
                    //当初始化失败时触发，此时请查看错误信息getLocalizedMessage以便获知getExceptionCode 。
                    if (_interstitialListener != null)
                        _interstitialListener.onAdShowFailed(AdInfo.GROUP_VUNGLE, AdInfo.TYPE_INTERSTITIAL, exception.toString());
                }

                @Override
                public void onAdViewed(String placementId) {
                    //当SDK将广告渲染时触发
                    if (_interstitialListener != null)
                        _interstitialListener.onAdOpened(AdInfo.GROUP_VUNGLE, AdInfo.TYPE_INTERSTITIAL);
                }
            });
        } else {
            if (listener != null)   listener.onRewardVideoResult(false);
        }
    }

    @Override
    public void destroy() {
        _bannerListener = null;
        _interstitialListener = null;
        _videoListener = null;
    }


}
