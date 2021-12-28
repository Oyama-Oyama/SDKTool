package com.roman.garden.adeasy;

import android.view.View;

import androidx.annotation.Nullable;

//import com.bytedance.sdk.openadsdk.AdSlot;
//import com.bytedance.sdk.openadsdk.TTAdConfig;
//import com.bytedance.sdk.openadsdk.TTAdManager;
//import com.bytedance.sdk.openadsdk.TTAdNative;
//import com.bytedance.sdk.openadsdk.TTAdSdk;
//import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
//import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
//import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
//
//import java.util.List;

/**
 * 需要填写公司信息， 暂时无法使用
 */
public class PangleImpl extends BaseAdImpl {

//    private TTRewardVideoAd _ttRewardVideoAd = null;
//    private TTFullScreenVideoAd _ttFullScreenVideoAd = null;
//    private View _banner = null;
//    private View _native = null;
//    private TTAdNative ttAdNative = null;

//    private TTAdNative getTTAdNative() {
//        return null;
//        if (ttAdNative == null)
//            ttAdNative = TTAdSdk.getAdManager().createAdNative(AdEasyImpl.of().getApplication());
//        return ttAdNative;
//    }

    @Override
    protected void initAdPlatform() {
//        TTAdConfig config = new TTAdConfig.Builder()
//                .appId(getAppId())
//                .allowShowNotify(true)
//                .allowShowPageWhenScreenLock(false)
//                .supportMultiProcess(false)
//                .coppa(0)
//                .debug(isTestMode)
//                .build();
//        TTAdSdk.init(AdEasyImpl.of().getApplication(), config, new TTAdSdk.InitCallback() {
//            @Override
//            public void success() {
//                loadBanner();
//                loadInterstitial();
//                loadVideo();
//                loadNative();
//            }
//
//            @Override
//            public void fail(int i, String s) {
//                LogUtil.e("Pangle SDK init error code:" + i + "  mesg:" + s);
//                retryInitAdPlatform();
//            }
//        });
    }

    @Override
    protected boolean validPlatform() {
        return false;//TTAdSdk.isInitSuccess();
    }

    @Override
    protected void loadBanner() {
//        if (validPlatform()) {
//            AdItem item = getBannerId();
//            if (!validAdItem(item)) return;
//            AdSlot adSlot = new AdSlot.Builder()
//                    .setAdId(item.getAdId())
//                    .setExpressViewAcceptedSize(320.0f, 50.0f)
//                    .setIsAutoPlay(true)
//                    .build();
//            getTTAdNative().loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
//                @Override
//                public void onError(int i, String s) {
//                    logError(item, AdInfo.TYPE_BANNER, s);
//                }
//
//                @Override
//                public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
//                    if (list == null) {
//                        logError(item, AdInfo.TYPE_BANNER, "result is null");
//                        reloadBanner();
//                        return;
//                    }
//                    if (list.size() == 0) {
//                        logError(item, AdInfo.TYPE_BANNER, "result size is 0");
//                        reloadBanner();
//                        return;
//                    }
//                    list.get(0).setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
//                        @Override
//                        public void onAdClicked(View view, int i) {
//
//                        }
//
//                        @Override
//                        public void onAdShow(View view, int i) {
//
//                        }
//
//                        @Override
//                        public void onRenderFail(View view, String s, int i) {
//                            logError(item, AdInfo.TYPE_BANNER, "render fail");
//                            reloadBanner();
//                        }
//
//                        @Override
//                        public void onRenderSuccess(View view, float v, float v1) {
//                            logEvent(item, AdInfo.TYPE_BANNER, "render success");
//                            _banner = view;
//                            setupBanner(item);
//                        }
//                    });
//                    list.get(0).render();
//                }
//            });
//        }
    }

    @Override
    protected void reloadBanner() {
//        _banner = null;
        super.reloadBanner();
    }

    @Override
    protected View getBannerView() {
        return null;//_banner;
    }

    @Override
    protected void loadInterstitial() {
//        if (validPlatform()) {
//            AdItem item = getInterstitialId();
//            if (!validAdItem(item)) return;
//            AdSlot adSlot = new AdSlot.Builder()
//                    .setAdId(item.getAdId())
//                    .setIsAutoPlay(true)
//                    .build();
//            getTTAdNative().loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
//                @Override
//                public void onError(int i, String s) {
//                    logError(item, AdInfo.TYPE_INTERSTITIAL, s);
//                    reloadInterstitial();
//                }
//
//                @Override
//                public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ttFullScreenVideoAd) {
//                    logEvent(item, AdInfo.TYPE_INTERSTITIAL);
//                    _ttFullScreenVideoAd = ttFullScreenVideoAd;
//                    setupInterstitial(item);
//                }
//
//                @Override
//                public void onFullScreenVideoCached() {
//
//                }
//            });
//
//        }
    }

    @Override
    protected void showInterstitial() {
//        if (AdEasyImpl.of().getActivity() == null || _ttFullScreenVideoAd == null) return;
//        _ttFullScreenVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {
//            @Override
//            public void onAdShow() {
//
//            }
//
//            @Override
//            public void onAdVideoBarClick() {
//
//            }
//
//            @Override
//            public void onAdClose() {
//                _ttFullScreenVideoAd = null;
//                reloadInterstitial();
//            }
//
//            @Override
//            public void onVideoComplete() {
//
//            }
//
//            @Override
//            public void onSkippedVideo() {
//
//            }
//        });
//        _ttFullScreenVideoAd.showFullScreenVideoAd(AdEasyImpl.of().getActivity());
    }

    @Override
    protected void loadNative() {
//        if (validPlatform()) {
//            AdItem item = getNativeId();
//            if (!validAdItem(item)) return;
//            AdSlot adSlot = new AdSlot.Builder()
//                    .setAdId(item.getAdId())
//                    .setIsAutoPlay(true)
//                    .build();
//            getTTAdNative().loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
//                @Override
//                public void onError(int i, String s) {
//                    logError(item, AdInfo.TYPE_NATIVE, s);
//                    reloadNative();
//                }
//
//                @Override
//                public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
//                    if (list == null) {
//                        logError(item, AdInfo.TYPE_NATIVE, "result is null");
//                        reloadNative();
//                        return;
//                    }
//                    if (list.size() == 0) {
//                        logError(item, AdInfo.TYPE_NATIVE, "result size is 0");
//                        reloadNative();
//                        return;
//                    }
//                    list.get(0).setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
//                        @Override
//                        public void onAdClicked(View view, int i) {
//
//                        }
//
//                        @Override
//                        public void onAdShow(View view, int i) {
//
//                        }
//
//                        @Override
//                        public void onRenderFail(View view, String s, int i) {
//                            logError(item, AdInfo.TYPE_NATIVE, "render fail");
//                            reloadNative();
//                        }
//
//                        @Override
//                        public void onRenderSuccess(View view, float v, float v1) {
//                            logEvent(item, AdInfo.TYPE_NATIVE);
//                            _native = view;
//                            setupNative(item);
//                        }
//                    });
//                    list.get(0).render();
//                }
//            });
//        }
    }

    @Override
    protected void reloadNative() {
//        _native = null;
        super.reloadNative();
    }

    @Override
    protected View getNativeView() {
        return null;//_native;
    }

    @Override
    protected void loadVideo() {
//        if (validPlatform()) {
//            AdItem item = getVideoId();
//            if (!validAdItem(item)) return;
//            AdSlot adSlot = new AdSlot.Builder()
//                    .setAdId(item.getAdId())
//                    .setIsAutoPlay(true)
//                    .build();
//            getTTAdNative().loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
//                @Override
//                public void onError(int i, String s) {
//                    logError(item, AdInfo.TYPE_VIDEO, s);
//                    reloadVideo();
//                }
//
//                @Override
//                public void onRewardVideoAdLoad(TTRewardVideoAd ttRewardVideoAd) {
//                    logEvent(item, AdInfo.TYPE_VIDEO);
//                    _ttRewardVideoAd = ttRewardVideoAd;
//                    setupVideo(item);
//                }
//
//                @Override
//                public void onRewardVideoCached() {
//
//                }
//            });
//        }
    }

    @Override
    protected void showVideo(@Nullable @org.jetbrains.annotations.Nullable IVideoResultCallback callback) {
//        if (AdEasyImpl.of().getActivity() == null || _ttRewardVideoAd == null) {
//            if (callback != null) callback.onResult(false);
//            return;
//        }
//        _ttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {
//            @Override
//            public void onAdShow() {
//
//            }
//
//            @Override
//            public void onAdVideoBarClick() {
//
//            }
//
//            @Override
//            public void onAdClose() {
//                _ttRewardVideoAd = null;
//                reloadVideo();
//            }
//
//            @Override
//            public void onVideoComplete() {
//
//            }
//
//            @Override
//            public void onVideoError() {
//
//            }
//
//            @Override
//            public void onRewardVerify(boolean b, int i, String s) {
//                if (callback != null) callback.onResult(b);
//            }
//
//            @Override
//            public void onSkippedVideo() {
//
//            }
//        });
//        _ttRewardVideoAd.showRewardVideoAd(AdEasyImpl.of().getActivity());
    }


}
