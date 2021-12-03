package com.roman.garden.adeasy;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

final class AdmobImpl implements AdImpl{

    private AdView _bannerAdView = null;
    private volatile boolean _bannerStatus = false;

    private InterstitialAd _interstitialAd = null;

    private RewardedAd _rewardAd = null;

    private AdLoadListener _bannerListener = null;
    private AdLoadListener _interstitialListener = null;
    private AdLoadListener _videoListener = null;

    PlatformConfig _platformConfig;

    public AdmobImpl(PlatformConfig _config){
        this._platformConfig = _config;
    }

    @Override
    public void setupBannerListener(@NonNull AdLoadListener _listener) {
        this._bannerListener = _listener;
    }

    private boolean assetAdmob(){
        //·暂不处理
        return false;
    }

    private boolean assetPlatformConfig(){
        if (_platformConfig != null)
            return true;
        LogUtil.e("Admob platform has not seted");
        return false;
    }

    @Override
    public void loadBanner(Activity _activity) {
        if (assetPlatformConfig()) {
            try {
                _bannerAdView = new AdView(_activity);
                _bannerAdView.setAdSize(AdSize.BANNER);
                _bannerAdView.setAdUnitId(_platformConfig.getBannerId().getAdId());
                AdRequest _request = new AdRequest.Builder().build();
                _bannerAdView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        _bannerStatus = true;
                        if (_bannerListener != null)
                            _bannerListener.onAdLoaded(AdInfo.GROUP_ADMOB, AdInfo.TYPE_BANNER);
                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                        if (_bannerListener != null)
                            _bannerListener.onAdClicked(AdInfo.GROUP_ADMOB, AdInfo.TYPE_BANNER);
                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        _bannerStatus = false;
                        _bannerAdView = null;
                        if (_bannerListener != null)
                            _bannerListener.onAdClosed(AdInfo.GROUP_ADMOB, AdInfo.TYPE_BANNER);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        _bannerStatus = false;
                        if (_bannerListener != null)
                            _bannerListener.onAdLoadFailed(AdInfo.GROUP_ADMOB, AdInfo.TYPE_BANNER, loadAdError.toString());
                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                        _bannerStatus = false;
                        _bannerAdView = null;
                        if (_bannerListener != null)
                            _bannerListener.onAdOpened(AdInfo.GROUP_ADMOB, AdInfo.TYPE_BANNER);
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                        if (_bannerListener != null)
                            _bannerListener.onAdImpression(AdInfo.GROUP_ADMOB, AdInfo.TYPE_BANNER);
                    }

                });
                _bannerAdView.loadAd(_request);
            } catch (Exception e) {
                _bannerAdView = null;
                _bannerStatus = false;
                if (_bannerListener != null)
                    _bannerListener.onAdLoadFailed(AdInfo.GROUP_ADMOB, AdInfo.TYPE_BANNER, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isBannerOk() {
        return _bannerStatus;
    }

    @Override
    public View getBannerView() {
        return _bannerAdView;
    }

    @Override
    public void destroyBannerView(@Nullable View view) {
        _bannerStatus = false;
        destroyBanner();
    }

    @Override
    public void setupInterstitialListener(@NonNull AdLoadListener _listener) {
        this._interstitialListener = _listener;
    }

    @Override
    public void loadInterstitial(Context _context) {
        if (assetPlatformConfig()) {
            try {
                AdRequest _request = new AdRequest.Builder().build();
                InterstitialAd.load(_context, _platformConfig.getInterstitialId().getAdId(), _request, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        _interstitialAd = interstitialAd;
                        if (_interstitialListener != null)
                            _interstitialListener.onAdLoaded(AdInfo.GROUP_ADMOB, AdInfo.TYPE_INTERSTITIAL);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        _interstitialAd = null;
                        if (_interstitialListener != null)
                            _interstitialListener.onAdLoadFailed(AdInfo.GROUP_ADMOB, AdInfo.TYPE_INTERSTITIAL, loadAdError.toString());
                    }
                });
            } catch (Exception e) {
                _interstitialAd = null;
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isInterstitialOk() {
        if (_interstitialAd != null)
            return true;
        return false;
    }

    @Override
    public void showInterstitial(Activity _activity) {
        if (_interstitialAd != null){
            _interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    _interstitialAd = null;
                    if (_interstitialListener != null)
                        _interstitialListener.onAdShowFailed(AdInfo.GROUP_ADMOB, AdInfo.TYPE_INTERSTITIAL, adError.toString());
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    if (_interstitialListener != null)
                        _interstitialListener.onAdOpened(AdInfo.GROUP_ADMOB, AdInfo.TYPE_INTERSTITIAL);
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    _interstitialAd = null;
                    if (_interstitialListener != null)
                        _interstitialListener.onAdClosed(AdInfo.GROUP_ADMOB, AdInfo.TYPE_INTERSTITIAL);
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                    if (_interstitialListener != null)
                        _interstitialListener.onAdImpression(AdInfo.GROUP_ADMOB, AdInfo.TYPE_INTERSTITIAL);
                }

                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                    if (_interstitialListener != null)
                        _interstitialListener.onAdClicked(AdInfo.GROUP_ADMOB, AdInfo.TYPE_INTERSTITIAL);
                }
            });
            _interstitialAd.show(_activity);
        }
    }

    @Override
    public void setupVideoListener(@NonNull AdLoadListener _listener) {
        this._videoListener = _listener;
    }

    @Override
    public void loadVideo(Context _context) {
        if (assetPlatformConfig()) {
            try {
                AdRequest _request = new AdRequest.Builder().build();
                RewardedAd.load(_context, _platformConfig.getVideoId().getAdId(), _request, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        super.onAdLoaded(rewardedAd);
                        _rewardAd = rewardedAd;
                        if (_videoListener != null)
                            _videoListener.onAdLoaded(AdInfo.GROUP_ADMOB, AdInfo.TYPE_VIDEO);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        _rewardAd = null;
                        if (_videoListener != null)
                            _videoListener.onAdLoadFailed(AdInfo.GROUP_ADMOB, AdInfo.TYPE_VIDEO, loadAdError.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isVideoOk() {
        if (_rewardAd != null)
            return true;
        return false;
    }

    @Override
    public void showVideo(Activity _activity, RewardVideoResultListener listener) {
        if (_rewardAd == null){
            if (listener != null)
                listener.onRewardVideoResult(false);
            return;
        }
        _rewardAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                _rewardAd = null;
                if (_videoListener != null)
                    _videoListener.onAdShowFailed(AdInfo.GROUP_ADMOB, AdInfo.TYPE_VIDEO, adError.toString());
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                if (_videoListener != null)
                    _videoListener.onAdOpened(AdInfo.GROUP_ADMOB, AdInfo.TYPE_VIDEO);
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                _rewardAd = null;
                if (_videoListener != null)
                    _videoListener.onAdOpened(AdInfo.GROUP_ADMOB, AdInfo.TYPE_VIDEO);
                if (listener != null)
                    listener.onRewardVideoResult(false);
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                if (_videoListener != null)
                    _videoListener.onAdOpened(AdInfo.GROUP_ADMOB, AdInfo.TYPE_VIDEO);
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                if (_videoListener != null)
                    _videoListener.onAdOpened(AdInfo.GROUP_ADMOB, AdInfo.TYPE_VIDEO);
            }
        });
        _rewardAd.show(_activity, new OnUserEarnedRewardListener() {
            @Override
            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                if (listener != null)
                    listener.onRewardVideoResult(true);
            }
        });
    }

    @Override
    public void destroy() {
        _bannerListener = null;
        _interstitialListener = null;
        _videoListener = null;
        destroyBanner();
        _interstitialAd = null;
        _rewardAd = null;
    }

    private void destroyBanner(){
        try{
            if (_bannerAdView != null) {
                if(_bannerAdView.getParent() != null){
                    ((ViewGroup)_bannerAdView.getParent()).removeView(_bannerAdView);
                }
                _bannerAdView.destroy();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            _bannerAdView = null;
        }
    }

}
