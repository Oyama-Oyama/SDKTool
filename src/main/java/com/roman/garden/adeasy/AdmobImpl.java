package com.roman.garden.adeasy;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.roman.garden.adeasy.template.NativeTemplateStyle;
import com.roman.garden.adeasy.template.TemplateView;

import org.jetbrains.annotations.NotNull;

final class AdmobImpl extends BaseAdImpl {

    private AdView _banner;
    private InterstitialAd _interstitial;
    private RewardedAd _rewarded;
    private TemplateView _native;

    @Override
    protected void initAdPlatform() {
        MobileAds.initialize(AdEasyImpl.of().getApplication(),
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(@NonNull @NotNull InitializationStatus initializationStatus) {
                        loadBanner();
                        loadInterstitial();
                        loadVideo();
                        loadNative();
                    }
                });
    }

    @Override
    protected boolean validPlatform() {
        return true;
    }

    @Override
    protected void loadBanner() {
        AdItem item = getBannerId();
        if (!validAdItem(item)) return;
        AdView view = new AdView(AdEasyImpl.of().getApplication());
        view.setAdSize(AdSize.BANNER);
        view.setAdUnitId(item.getAdId());
        AdRequest _request = new AdRequest.Builder().build();
        view.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                logError(item, AdInfo.TYPE_BANNER, loadAdError.toString());
                reloadBanner();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                logEvent(item, AdInfo.TYPE_BANNER);
                _banner = view;
                setupBanner(item);
            }
        });
        view.loadAd(_request);
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
        AdItem item = getInterstitialId();
        if (!validAdItem(item)) return;
        AdRequest _request = new AdRequest.Builder().build();
        InterstitialAd.load(AdEasyImpl.of().getApplication(),
                item.getAdId(),
                _request,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        logEvent(item, AdInfo.TYPE_INTERSTITIAL);
                        _interstitial = interstitialAd;
                        setupInterstitial(item);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        logError(item, AdInfo.TYPE_INTERSTITIAL, loadAdError.toString());
                        reloadInterstitial();
                    }
                });
    }

    @Override
    protected void reloadInterstitial() {
        _interstitial = null;
        super.reloadInterstitial();
    }

    @Override
    protected void showInterstitial() {
        if (AdEasyImpl.of().getActivity() != null && _interstitial != null) {
            _interstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull @NotNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    reloadInterstitial();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    reloadInterstitial();
                }
            });
            _interstitial.show(AdEasyImpl.of().getActivity());
        }
    }

    @Override
    protected void loadNative() {
        AdItem item = getNativeId();
        if (!validAdItem(item)) return;
        AdLoader _adLoader = new AdLoader.Builder(AdEasyImpl.of().getApplication(),
                item.getAdId())
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull @NotNull NativeAd nativeAd) {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(new ColorDrawable(Color.WHITE)).build();
                        _native = (TemplateView) View.inflate(AdEasyImpl.of().getApplication(), R.layout.template_medium, null);
                        _native.setStyles(styles);
                        _native.setNativeAd(nativeAd);
                        logEvent(item, AdInfo.TYPE_NATIVE);

                        setupNative(item);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        logError(item, AdInfo.TYPE_NATIVE, loadAdError.toString());
                        reloadNative();
                    }
                })
                .build();
        _adLoader.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void reloadNative() {
        _native = null;
        super.reloadNative();
    }

    @Override
    protected View getNativeView() {
        return _native;
    }

    @Override
    protected void loadVideo() {
        AdItem item = getVideoId();
        if (!validAdItem(item)) return;
        AdRequest _request = new AdRequest.Builder().build();
        RewardedAd.load(AdEasyImpl.of().getApplication(),
                item.getAdId(), _request, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        super.onAdLoaded(rewardedAd);
                        logEvent(item, AdInfo.TYPE_VIDEO);
                        _rewarded = rewardedAd;
                        setupVideo(item);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        logError(item, AdInfo.TYPE_VIDEO, loadAdError.toString());
                        reloadVideo();
                    }
                });
    }

    @Override
    protected void reloadVideo() {
        _rewarded = null;
        super.reloadVideo();
    }

    @Override
    protected void showVideo(@Nullable @org.jetbrains.annotations.Nullable IVideoResultCallback callback) {
        if (AdEasyImpl.of().getActivity() != null && _rewarded != null) {
            _rewarded.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull @NotNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    if (callback != null) callback.onResult(false);
                    reloadVideo();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    if (callback != null) callback.onResult(false);
                    reloadVideo();
                }
            });
        }
        _rewarded.show(AdEasyImpl.of().getActivity(), new OnUserEarnedRewardListener() {
            @Override
            public void onUserEarnedReward(@NonNull @NotNull RewardItem rewardItem) {
                if (callback != null) callback.onResult(true);
                reloadVideo();
            }
        });
    }


}
