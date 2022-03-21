package com.roman.garden.adeasy.ad.impl;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

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
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.roman.garden.adeasy.AdEasyImpl;
import com.roman.garden.adeasy.IAdListener;
import com.roman.garden.adeasy.ad.AdImpl;
import com.roman.garden.adeasy.ad.AdInfo;
import com.roman.garden.adeasy.ad.AdItem;
import com.roman.garden.adeasy.ad.AdmobAppOpenManager;
import com.roman.garden.adeasy.ad.IRewardedResultCallback;
import com.roman.garden.adeasy.ad.template.NativeTemplateStyle;
import com.roman.garden.adeasy.ad.template.TemplateView;
import com.roman.garden.adeasy.util.StringUtil;

import java.util.Arrays;


public class AdmobImpl extends BaseAdImpl {

    private AdImpl _bannerImpl;
    private AdImpl _interstitialImpl;
    private AdImpl _videoImpl;
    private AdImpl _nativeImpl;

    private AdmobAppOpenManager admobAppOpenManager = null;

    private Observer _observer = new Observer<AdImpl>() {
        @Override
        public void onChanged(AdImpl impl) {
            if (impl == null) return;
            if (StringUtil.isEmpty(impl._type)) return;
            if (impl._type == AdInfo.TYPE_BANNER &&
                    getBanner() != null &&
                    getBanner().size() > 0) {
                if (impl._banner == null) {
                    impl.reset(getBanner());
                    loadBanner(impl.adItem);
                }
            } else if (impl._type == AdInfo.TYPE_INTERSTITIAL &&
                    getInterstitial() != null &&
                    getInterstitial().size() > 0) {
                if (impl._interstitial == null) {
                    impl.reset(getInterstitial());
                    loadInterstitial(impl.adItem);
                }
            } else if (impl._type == AdInfo.TYPE_VIDEO &&
                    getVideo() != null &&
                    getVideo().size() > 0) {
                if (impl._rewarded == null) {
                    impl.reset(getVideo());
                    loadVideo(impl.adItem);
                }
            } else if (impl._type == AdInfo.TYPE_NATIVE &&
                    getNative() != null &&
                    getNative().size() > 0) {
                if (impl.nativeAd == null) {
                    impl.reset(getNative());
                    loadNative(impl.adItem);
                }
            }
        }
    };

    @Override
    void bindLiveData() {
        if (_bannerImpl == null && getBanner() != null && getBanner().size() > 0) {
            _bannerImpl = new AdImpl(AdInfo.GROUP_ADMOB, AdInfo.TYPE_BANNER);
            _bannerImpl.observeForever(_observer);
        }
        if (_interstitialImpl == null && getInterstitial() != null && getInterstitial().size() > 0) {
            _interstitialImpl = new AdImpl(AdInfo.GROUP_ADMOB, AdInfo.TYPE_INTERSTITIAL);
            _interstitialImpl.observeForever(_observer);
        }
        if (_videoImpl == null && getVideo() != null && getVideo().size() > 0) {
            _videoImpl = new AdImpl(AdInfo.GROUP_ADMOB, AdInfo.TYPE_VIDEO);
            _videoImpl.observeForever(_observer);
        }
        if (_nativeImpl == null && getNative() != null && getNative().size() > 0) {
            _nativeImpl = new AdImpl(AdInfo.GROUP_ADMOB, AdInfo.TYPE_NATIVE);
            _nativeImpl.observeForever(_observer);
        }
    }

    @Override
    void initPlatform() {
        if (!StringUtil.isEmpty(getTestDeviceId())) {
            RequestConfiguration configuration = new RequestConfiguration.Builder()
                    .setTestDeviceIds(Arrays.asList(getTestDeviceId()))
                    .build();
            MobileAds.setRequestConfiguration(configuration);
        }
        MobileAds.initialize(AdEasyImpl.getInstance().getApplication(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                if (_bannerImpl != null) _bannerImpl.reset();
                if (_interstitialImpl != null) _interstitialImpl.reset();
                if (_videoImpl != null) _videoImpl.reset();
                if (_nativeImpl != null) _nativeImpl.reset();
                loadAppOpen();
            }
        });
    }

    @Override
    public void destroy() {
        super.destroy();
        if (_bannerImpl != null)
            _bannerImpl.removeObserver(_observer);
        _bannerImpl = null;
        if (_interstitialImpl != null)
            _interstitialImpl.removeObserver(_observer);
        _interstitialImpl = null;
        if (_videoImpl != null)
            _videoImpl.removeObserver(_observer);
        _videoImpl = null;
        if (_nativeImpl != null)
            _nativeImpl.removeObserver(_observer);
        _nativeImpl = null;
    }

    @Override
    void loadBanner(@NonNull AdItem adItem) {
        if (AdEasyImpl.getInstance().getApplication() == null || !adItem.valid())
            return;
        AdView adView = new AdView(AdEasyImpl.getInstance().getApplication());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(adItem.getId());
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (_bannerImpl != null) {
                    _bannerImpl.setBanner(adView);
                    _bannerImpl.autoUpdate();
                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                if (_bannerImpl != null) _bannerImpl.autoUpdate();
            }
        });
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);
    }

    @Override
    public View getBannerView() {
        AdView view = null;
        if (hasBanner()) {
            view = _bannerImpl._banner;
            _bannerImpl._banner = null;
            _bannerImpl.reset();
        }
        return view;
    }

    @Override
    public boolean hasBanner() {
        return _bannerImpl != null && _bannerImpl._banner != null;
    }

    @Override
    void loadInterstitial(@NonNull AdItem adItem) {
        if (AdEasyImpl.getInstance().getApplication() == null || !adItem.valid())
            return;
        AdRequest request = new AdRequest.Builder().build();
        InterstitialAd.load(AdEasyImpl.getInstance().getApplication(),
                adItem.getId(),
                request,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        if (_interstitialImpl != null)
                            _interstitialImpl.setInterstitial(interstitialAd);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        if (_interstitialImpl != null) _interstitialImpl.autoUpdate();

                    }
                });
    }

    @Override
    public void showInterstitial(@Nullable IAdListener listener) {
        if (hasInterstitial() && AdEasyImpl.getInstance().getActivity() != null) {
            _interstitialImpl._interstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    if (_interstitialImpl != null) {
                        _interstitialImpl._interstitial = null;
                        _interstitialImpl.reset();
                    }
                    if (listener != null)
                        listener.onAdShowError();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    if (_interstitialImpl != null) {
                        _interstitialImpl._interstitial = null;
                        _interstitialImpl.reset();
                    }
                    if (listener != null)
                        listener.onAdShowStart();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    if (listener != null)
                        listener.onAdCompleted();
                }
            });
            _interstitialImpl._interstitial.show(AdEasyImpl.getInstance().getActivity());
        }
    }

    @Override
    public boolean hasInterstitial() {
        return _interstitialImpl != null && _interstitialImpl._interstitial != null;
    }

    @Override
    void loadVideo(@NonNull AdItem adItem) {
        if (AdEasyImpl.getInstance().getApplication() == null || !adItem.valid())
            return;
        AdRequest request = new AdRequest.Builder().build();
        RewardedAd.load(AdEasyImpl.getInstance().getApplication(),
                adItem.getId(),
                request,
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        super.onAdLoaded(rewardedAd);
                        if (_videoImpl != null) _videoImpl.setRewarded(rewardedAd);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        if (_videoImpl != null) _videoImpl.autoUpdate();
                    }
                });
    }

    @Override
    public void showVideo(IRewardedResultCallback callback) {
        if (hasVideo() && AdEasyImpl.getInstance().getActivity() != null) {
            _videoImpl._rewarded.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    if (_videoImpl != null) {
                        _videoImpl._rewarded = null;
                        _videoImpl.reset();
                    }
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    if (_videoImpl != null) {
                        _videoImpl._rewarded = null;
                        _videoImpl.reset();
                    }
                }
            });
            _videoImpl._rewarded.show(AdEasyImpl.getInstance().getActivity(), new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    if (rewardItem != null && rewardItem.getAmount() > 0) {
                        if (callback != null) callback.onResult(true);
                    } else {
                        if (callback != null) callback.onResult(false);
                    }
                }
            });
        }
    }

    @Override
    public boolean hasVideo() {
        return _videoImpl != null && _videoImpl._rewarded != null;
    }

    @Override
    void loadNative(@NonNull AdItem adItem) {
        if (AdEasyImpl.getInstance().getApplication() == null || !adItem.valid())
            return;
        AdLoader _adLoader = new AdLoader.Builder(AdEasyImpl.getInstance().getApplication(),
                adItem.getId())
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        if (_nativeImpl != null) {
                            _nativeImpl.setNativeAd(nativeAd);
                            _nativeImpl.autoUpdate();
                        }
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        if (_nativeImpl != null) _nativeImpl.autoUpdate();
                    }
                })
                .build();
        _adLoader.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public View getNativeView(int templateLayoutId) {
        if (hasNative() && AdEasyImpl.getInstance().getApplication() != null) {
            NativeTemplateStyle styles = new
                    NativeTemplateStyle.Builder().withMainBackgroundColor(new ColorDrawable(Color.WHITE)).build();
            TemplateView _native = (TemplateView) View.inflate(AdEasyImpl.getInstance().getApplication(), templateLayoutId, null);
            _native.setStyles(styles);
            _native.setNativeAd(_nativeImpl.nativeAd);

            _nativeImpl.nativeAd = null;
            _nativeImpl.reset();

            return _native;
        }
        return null;
    }

    @Override
    public boolean hasNative() {
        return _nativeImpl != null && _nativeImpl.nativeAd != null;
    }

    @Override
    public int getWeights(String adType) {
        if (adType == AdInfo.TYPE_BANNER) {
            if (hasBanner())
                return _bannerImpl.adItem.getWeights();
        } else if (adType == AdInfo.TYPE_INTERSTITIAL) {
            if (hasInterstitial())
                return _interstitialImpl.adItem.getWeights();
        } else if (adType == AdInfo.TYPE_VIDEO) {
            if (hasVideo())
                return _videoImpl.adItem.getWeights();
        } else if (adType == AdInfo.TYPE_NATIVE) {
            if (hasNative())
                return _nativeImpl.adItem.getWeights();
        }
        return 0;
    }

    @Override
    public void loadAppOpen() {
        AdItem adItem = getAppOpen();
        if (AdEasyImpl.getInstance().getApplication() == null || !adItem.valid())
            return;
        if (admobAppOpenManager == null)
            admobAppOpenManager = new AdmobAppOpenManager(adItem.getId());
        admobAppOpenManager.loadAd(AdEasyImpl.getInstance().getApplication());
    }

    @Override
    public void showAppOpen() {
        if (admobAppOpenManager != null && AdEasyImpl.getInstance().getActivity() != null)
            admobAppOpenManager.showAdIfAvailable(AdEasyImpl.getInstance().getActivity());
    }

    @Override
    public void registerAppOpenListener(Observer<Boolean> listener) {
        if (admobAppOpenManager != null)
            admobAppOpenManager.registerAppOpenListener(listener);
    }

    @Override
    public void unregisterAppOpenListener(Observer<Boolean> listener) {
        if (admobAppOpenManager != null)
            admobAppOpenManager.unregisterAppOpenListener(listener);
    }

    @Override
    public AdImpl getBannerLive() {
        return _bannerImpl;
    }

    @Override
    public AdImpl getNativeLive() {
        return _nativeImpl;
    }


}
