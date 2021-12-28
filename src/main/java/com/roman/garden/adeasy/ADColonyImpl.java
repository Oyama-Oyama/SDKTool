package com.roman.garden.adeasy;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyReward;
import com.adcolony.sdk.AdColonyRewardListener;
import com.adcolony.sdk.AdColonyZone;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class ADColonyImpl extends BaseAdImpl {

    private AdColonyInterstitial _adColonyInterstitial = null;
    private AdColonyInterstitial __adColonyInterstitial = null;
    private AdColonyAdView _adColonyAdView = null;
    private AdColonyAdView __adColonyAdView = null;

    private List<String> makeZoneIds(AdItem item, List<String> list){
        if (validAdItem(item) && !StringUtil.isEmpty(item.getAdId()))
            list.add(item.getAdId());
        return list;
    }

    @Override
    protected void initAdPlatform() {
        if (AdEasyImpl.of().getApplication() != null) {
            List<String> list = new ArrayList<>();
            AdItem banner = getBannerId();
            makeZoneIds(banner, list);
            AdItem nativeId = getNativeId();
            makeZoneIds(nativeId, list);
            AdItem inter = getInterstitialId();
            makeZoneIds(inter, list);
            AdItem video = getVideoId();
            makeZoneIds(video, list);

            boolean result = AdColony.configure(AdEasyImpl.of().getApplication(), getAppId(), list.toArray(new String[list.size()]));
            if (result){
                LogUtil.e("ADColony init success");
                loadBanner();
                loadNative();
                loadInterstitial();
                loadVideo();
            } else {
                LogUtil.e("ADColony init error");
                reloadInterstitial();
            }
        } else {
            LogUtil.e("ADColony init error");
            reloadInterstitial();
        }
    }

    @Override
    protected boolean validPlatform() {
        return false;
    }

    @Override
    protected void loadBanner() {
        AdItem item = getBannerId();
        if (validAdItem(item)){
            AdColony.requestAdView(item.getAdId(), new AdColonyAdViewListener() {
                @Override
                public void onRequestFilled(AdColonyAdView adColonyAdView) {
                    logEvent(item, AdInfo.TYPE_BANNER);
                    _adColonyAdView = adColonyAdView;
                    setupBanner(item);
                }

                @Override
                public void onRequestNotFilled(AdColonyZone zone) {
                    super.onRequestNotFilled(zone);
                    logError(item, AdInfo.TYPE_BANNER, "filled error ");
                    reloadBanner();
                }
            }, AdColonyAdSize.BANNER);
        }
    }

    @Override
    protected View getBannerView() {
        return _adColonyAdView;
    }

    @Override
    protected void loadInterstitial() {
        AdItem item = getInterstitialId();
        if (validAdItem(item)) {
            AdColony.requestInterstitial(item.getAdId(), new AdColonyInterstitialListener() {
                @Override
                public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {
                    logEvent(item, AdInfo.TYPE_INTERSTITIAL);
                    _adColonyInterstitial = adColonyInterstitial;
                    setupInterstitial(item);
                }

                @Override
                public void onRequestNotFilled(AdColonyZone zone) {
                    super.onRequestNotFilled(zone);
                    logError(item, AdInfo.TYPE_INTERSTITIAL, "filled error");
                    reloadInterstitial();
                }
            });
        }
    }

    @Override
    protected void showInterstitial() {
        if (_adColonyInterstitial != null) {
            _adColonyInterstitial.setListener(new AdColonyInterstitialListener() {
                @Override
                public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {

                }

                @Override
                public void onClosed(AdColonyInterstitial ad) {
                    super.onClosed(ad);
                    _adColonyInterstitial = null;
                    reloadInterstitial();
                }
            });
            _adColonyInterstitial.show();
        }
    }

    @Override
    protected void loadNative() {
        AdItem item = getNativeId();
        if (validAdItem(item)){
            AdColony.requestAdView(item.getAdId(), new AdColonyAdViewListener() {
                @Override
                public void onRequestFilled(AdColonyAdView adColonyAdView) {
                    logEvent(item, AdInfo.TYPE_NATIVE);
                    __adColonyAdView = adColonyAdView;
                    setupNative(item);
                }

                @Override
                public void onRequestNotFilled(AdColonyZone zone) {
                    super.onRequestNotFilled(zone);
                    logError(item, AdInfo.TYPE_NATIVE, "filled error");
                    reloadBanner();
                }
            }, AdColonyAdSize.MEDIUM_RECTANGLE);
        }
    }

    @Override
    protected View getNativeView() {
        return __adColonyAdView;
    }

    @Override
    protected void loadVideo() {
        AdItem item = getVideoId();
        if (validAdItem(item)) {
            AdColonyAdOptions options = new AdColonyAdOptions()
                    .enableConfirmationDialog(true)
                    .enableResultsDialog(true);
            AdColony.requestInterstitial(item.getAdId(), new AdColonyInterstitialListener() {
                @Override
                public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {
                    logEvent(item, AdInfo.TYPE_VIDEO);
                    __adColonyInterstitial = adColonyInterstitial;
                    setupVideo(item);
                }

                @Override
                public void onRequestNotFilled(AdColonyZone zone) {
                    super.onRequestNotFilled(zone);
                    logError(item, AdInfo.TYPE_VIDEO, "filled error");
                    reloadVideo();
                }
            }, options);
        }
    }

    @Override
    protected void showVideo(@Nullable @org.jetbrains.annotations.Nullable IVideoResultCallback callback) {
        AdItem item = getVideoId();
        if (validAdItem(item)) {
            AdColony.setRewardListener(new AdColonyRewardListener() {
                @Override
                public void onReward(@NonNull @NotNull AdColonyReward adColonyReward) {
                    if (callback != null) callback.onResult(adColonyReward.success());
                    AdColony.removeRewardListener();
                }
            });
            __adColonyInterstitial.show();
        }
    }


}
