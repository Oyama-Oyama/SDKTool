//package com.roman.garden.adeasy;
//
//import android.app.Activity;
//import android.view.View;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.ironsource.mediationsdk.IronSource;
//import com.ironsource.mediationsdk.logger.IronSourceError;
//import com.ironsource.mediationsdk.model.Placement;
//import com.ironsource.mediationsdk.sdk.InterstitialListener;
//import com.ironsource.mediationsdk.sdk.OfferwallListener;
//import com.ironsource.mediationsdk.sdk.RewardedVideoListener;
//
//public class IronSourceImpl extends BaseAdImpl {
//
//    @Override
//    protected void initAdPlatform() {
//        IronSource.setAdaptersDebug(isTestMode);
//        IronSource.setRewardedVideoListener(new RewardedVideoListener() {
//            @Override
//            public void onRewardedVideoAdOpened() {
//
//            }
//
//            @Override
//            public void onRewardedVideoAdClosed() {
//
//            }
//
//            @Override
//            public void onRewardedVideoAvailabilityChanged(boolean b) {
//
//            }
//
//            @Override
//            public void onRewardedVideoAdStarted() {
//
//            }
//
//            @Override
//            public void onRewardedVideoAdEnded() {
//
//            }
//
//            @Override
//            public void onRewardedVideoAdRewarded(Placement placement) {
//
//            }
//
//            @Override
//            public void onRewardedVideoAdShowFailed(IronSourceError ironSourceError) {
//
//            }
//
//            @Override
//            public void onRewardedVideoAdClicked(Placement placement) {
//
//            }
//        });
//
//        IronSource.setInterstitialListener(new InterstitialListener() {
//            @Override
//            public void onInterstitialAdReady() {
//
//            }
//
//            @Override
//            public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
//
//            }
//
//            @Override
//            public void onInterstitialAdOpened() {
//
//            }
//
//            @Override
//            public void onInterstitialAdClosed() {
//
//            }
//
//            @Override
//            public void onInterstitialAdShowSucceeded() {
//
//            }
//
//            @Override
//            public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
//
//            }
//
//            @Override
//            public void onInterstitialAdClicked() {
//
//            }
//        });
//
//        IronSource.setOfferwallListener(new OfferwallListener() {
//            @Override
//            public void onOfferwallAvailable(boolean b) {
//
//            }
//
//            @Override
//            public void onOfferwallOpened() {
//
//            }
//
//            @Override
//            public void onOfferwallShowFailed(IronSourceError ironSourceError) {
//
//            }
//
//            @Override
//            public boolean onOfferwallAdCredited(int i, int i1, boolean b) {
//                return false;
//            }
//
//            @Override
//            public void onGetOfferwallCreditsFailed(IronSourceError ironSourceError) {
//
//            }
//
//            @Override
//            public void onOfferwallClosed() {
//
//            }
//        });
//        /**
//         *Ad Units should be in the type of IronSource.Ad_Unit.AdUnitName, example
//         */
//        if (AdEasyImpl.of().getActivity() != null) {
//            IronSource.setMetaData("do_not_sell", "true");
//            IronSource.setMetaData("is_child_directed", "false");
//            IronSource.init(AdEasyImpl.of().getActivity(),
//                    getAppId(),
//                    IronSource.AD_UNIT.OFFERWALL,
//                    IronSource.AD_UNIT.INTERSTITIAL,
//                    IronSource.AD_UNIT.REWARDED_VIDEO,
//                    IronSource.AD_UNIT.BANNER);
//        } else
//            retryInitAdPlatform();
//    }
//
//    @Override
//    protected boolean validPlatform() {
//        return false;
//    }
//
//    public void onResume(@NonNull Activity activity) {
//        IronSource.onResume(activity);
//    }
//
//    public void onPause(@NonNull Activity activity) {
//        IronSource.onPause(activity);
//    }
//
//    @Override
//    protected void loadBanner() {
//
//    }
//
//    @Override
//    protected View getBannerView() {
//        return null;
//    }
//
//    @Override
//    protected void loadInterstitial() {
//        AdItem item = getInterstitialId();
//        if (validAdItem(item))
//            IronSource.loadInterstitial();
//    }
//
//    @Override
//    protected void showInterstitial() {
//        if (IronSource.isInterstitialReady())
//            IronSource.showInterstitial();
//    }
//
//    @Override
//    protected void loadNative() {
//
//    }
//
//    @Override
//    protected View getNativeView() {
//        return null;
//    }
//
//    @Override
//    protected void loadVideo() {
//        AdItem item = getVideoId();
//        if (validAdItem(item)) IronSource.loadRewardedVideo();
//
//    }
//
//    @Override
//    protected void showVideo(@Nullable @org.jetbrains.annotations.Nullable IVideoResultCallback callback) {
//        if (IronSource.isRewardedVideoAvailable()) {
//            IronSource.showRewardedVideo();
//        }
//    }
//
//
//}
