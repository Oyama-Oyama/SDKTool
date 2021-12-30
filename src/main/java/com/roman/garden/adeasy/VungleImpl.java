package com.roman.garden.adeasy;

import android.view.View;

import androidx.annotation.Nullable;

import com.vungle.warren.AdConfig;
import com.vungle.warren.BannerAdConfig;
import com.vungle.warren.Banners;
import com.vungle.warren.InitCallback;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.VungleNativeAd;
import com.vungle.warren.error.VungleException;

final class VungleImpl extends BaseAdImpl {

    private boolean _videoRewardResult = false;
    private VungleNativeAd vungleNativeAd = null;

    @Override
    protected void initAdPlatform() {
        Vungle.init(getAppId(), AdEasyImpl.of().getApplication(), new InitCallback() {
            @Override
            public void onSuccess() {
                LogUtil.e("Vungle SDK init success");
                loadBanner();
                loadInterstitial();
                loadVideo();
                loadNative();
            }

            @Override
            public void onError(VungleException exception) {
                LogUtil.e("Vungle SDK init error-->" + exception.toString());
                retryInitAdPlatform();
            }

            @Override
            public void onAutoCacheAdAvailable(String placementId) {

            }
        });
    }

    @Override
    protected boolean validPlatform() {
        return Vungle.isInitialized();
    }

    @Override
    protected void loadBanner() {
        if (validPlatform()) {
            AdItem item = getBannerId();
            if (!validAdItem(item)) return;
            Banners.loadBanner(item.getAdId(),
                    new BannerAdConfig(AdConfig.AdSize.BANNER),
                    new LoadAdCallback() {
                        @Override
                        public void onAdLoad(String placementId) {
                            logEvent(item, AdInfo.TYPE_BANNER);
                            setupBanner(item);
                        }

                        @Override
                        public void onError(String placementId, VungleException exception) {
                            logError(item, AdInfo.TYPE_BANNER, exception.getMessage());
                            reloadBanner();
                        }
                    });
        }
    }

    @Override
    protected View getBannerView() {
        if (validAdItem(getBannerId()) && Vungle.canPlayAd(getBannerId().getAdId())) {
            return Banners.getBanner(getBannerId().getAdId(), new BannerAdConfig(AdConfig.AdSize.BANNER), null);
        }
        return null;
    }

    @Override
    protected void loadInterstitial() {
        if (validPlatform()) {
            AdItem item = getInterstitialId();
            if (!validAdItem(item)) return;
            Vungle.loadAd(item.getAdId(), new LoadAdCallback() {
                @Override
                public void onAdLoad(String placementId) {
                    logEvent(item, AdInfo.TYPE_INTERSTITIAL);
                    setupInterstitial(item);
                }

                @Override
                public void onError(String placementId, VungleException exception) {
                    logError(item, AdInfo.TYPE_INTERSTITIAL, exception.getMessage());
                    reloadInterstitial();
                }
            });
        }
    }

    @Override
    protected void showInterstitial() {
        if (AdEasyImpl.of().getActivity() != null) {
            AdItem item = getInterstitialId();
            if (validAdItem(item) && Vungle.canPlayAd(item.getAdId())) {
                Vungle.playAd(item.getAdId(), null, new PlayAdCallback() {
                    @Override
                    public void creativeId(String creativeId) {

                    }

                    @Override
                    public void onAdStart(String placementId) {

                    }

                    @Override
                    public void onAdEnd(String placementId, boolean completed, boolean isCTAClicked) {

                    }

                    @Override
                    public void onAdEnd(String placementId) {
                        reloadInterstitial();
                    }

                    @Override
                    public void onAdClick(String placementId) {

                    }

                    @Override
                    public void onAdRewarded(String placementId) {

                    }

                    @Override
                    public void onAdLeftApplication(String placementId) {

                    }

                    @Override
                    public void onError(String placementId, VungleException exception) {

                    }

                    @Override
                    public void onAdViewed(String placementId) {

                    }
                });
            }
        }
    }

    @Override
    protected void loadNative() {
        if (validPlatform()) {
            AdItem item = getNativeId();
            if (!validAdItem(item)) return;
            Vungle.loadAd(item.getAdId(), new LoadAdCallback() {
                @Override
                public void onAdLoad(String placementId) {
                    logEvent(item, AdInfo.TYPE_NATIVE);
                    setupNative(item);
                }

                @Override
                public void onError(String placementId, VungleException exception) {
                    logError(item, AdInfo.TYPE_NATIVE, exception.getMessage());
                    reloadNative();
                }
            });
        }
    }

    @Override
    protected View getNativeView() {
        AdItem item = getNativeId();
        if (validAdItem(item) && Vungle.canPlayAd(item.getAdId())) {
            if (vungleNativeAd != null) {
                vungleNativeAd.setAdVisibility(false);
                vungleNativeAd.finishDisplayingAd();
            }
            AdConfig config = new AdConfig();
            config.setAdSize(AdConfig.AdSize.VUNGLE_MREC);
            vungleNativeAd = Vungle.getNativeAd(item.getAdId(),
                    String.valueOf(System.currentTimeMillis()),
                    config, null);
            if (vungleNativeAd != null) {
                View view = vungleNativeAd.renderNativeView();
                vungleNativeAd.setAdVisibility(true);
                return view;
            }
        }
        return null;
    }

    @Override
    protected void loadVideo() {
        if (validPlatform()) {
            AdItem item = getVideoId();
            if (!validAdItem(item)) return;
            Vungle.loadAd(item.getAdId(), new LoadAdCallback() {
                @Override
                public void onAdLoad(String placementId) {
                    logEvent(item, AdInfo.TYPE_VIDEO);
                    setupVideo(item);
                }

                @Override
                public void onError(String placementId, VungleException exception) {
                    logError(item, AdInfo.TYPE_VIDEO, exception.getMessage());
                }
            });
        }
    }

    @Override
    protected void showVideo(@Nullable @org.jetbrains.annotations.Nullable IVideoResultCallback callback) {
        _videoRewardResult = false;
        AdItem item = getVideoId();
        if (validAdItem(item) && Vungle.canPlayAd(item.getAdId())) {
            Vungle.playAd(item.getAdId(), null, new PlayAdCallback() {
                @Override
                public void creativeId(String creativeId) {

                }

                @Override
                public void onAdStart(String placementId) {

                }

                @Override
                public void onAdEnd(String placementId, boolean completed, boolean isCTAClicked) {

                }

                @Override
                public void onAdEnd(String placementId) {
                    if (callback != null) callback.onResult(_videoRewardResult);
                    reloadVideo();
                }

                @Override
                public void onAdClick(String placementId) {

                }

                @Override
                public void onAdRewarded(String placementId) {
                    _videoRewardResult = true;
                }

                @Override
                public void onAdLeftApplication(String placementId) {

                }

                @Override
                public void onError(String placementId, VungleException exception) {

                }

                @Override
                public void onAdViewed(String placementId) {

                }
            });
        } else if (callback != null) callback.onResult(false);
    }


}
