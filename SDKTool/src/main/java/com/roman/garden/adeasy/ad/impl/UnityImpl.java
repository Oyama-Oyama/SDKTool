package com.roman.garden.adeasy.ad.impl;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.roman.garden.adeasy.AdEasyImpl;
import com.roman.garden.adeasy.IAdListener;
import com.roman.garden.adeasy.ad.AdImpl;
import com.roman.garden.adeasy.ad.AdInfo;
import com.roman.garden.adeasy.ad.AdItem;
import com.roman.garden.adeasy.ad.IRewardedResultCallback;
import com.roman.garden.adeasy.util.StringUtil;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;


public class UnityImpl extends BaseAdImpl {

    private AdImpl _interstitial;
    private AdImpl _banner;
    private AdImpl _video;

    private Observer observer = new Observer<AdImpl>() {
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
            }
        }
    };

    @Override
    void bindLiveData() {
        if (getBanner() != null && getBanner().size() > 0 && _banner == null) {
            _banner = new AdImpl(AdInfo.GROUP_UNITY, AdInfo.TYPE_BANNER);
            _banner.observeForever(observer);
        }
        if (getInterstitial() != null && getInterstitial().size() > 0 && _interstitial == null) {
            _interstitial = new AdImpl(AdInfo.GROUP_UNITY, AdInfo.TYPE_INTERSTITIAL);
            _interstitial.observeForever(observer);
        }
        if (getVideo() != null && getVideo().size() > 0 && _video == null) {
            _video = new AdImpl(AdInfo.GROUP_UNITY, AdInfo.TYPE_BANNER);
            _video.observeForever(observer);
        }
    }

    @Override
    void initPlatform() {
        if (!UnityAds.isSupported())
            return;
        boolean testMode = !StringUtil.isEmpty(getTestDeviceId());
        UnityAds.initialize(AdEasyImpl.getInstance().getApplication(),
                getAppId(),
                testMode,
                new IUnityAdsInitializationListener() {
                    @Override
                    public void onInitializationComplete() {
                        if (_banner != null) _banner.reset();
                        if (_interstitial != null) _interstitial.reset();
                        if (_video != null) _video.reset();
                    }

                    @Override
                    public void onInitializationFailed(UnityAds.UnityAdsInitializationError unityAdsInitializationError, String s) {
                        retryInitPlatform();
                    }
                });
    }

    @Override
    public void destroy() {
        super.destroy();
        if (_banner != null)
            _banner.removeObserver(observer);
        _banner = null;
        if (_interstitial != null)
            _interstitial.removeObserver(observer);
        _interstitial = null;
        if (_video != null)
            _video.removeObserver(observer);
        _video = null;
    }

    private boolean validPlatform() {
        return UnityAds.isInitialized();
    }

    @Override
    void loadBanner(@NonNull AdItem adItem) {
        if (validPlatform() && adItem.valid()) {
            BannerView _bannerView = new BannerView(AdEasyImpl.getInstance().getActivity(),
                    adItem.getId(),
                    new UnityBannerSize(320, 50));
            _bannerView.setListener(new BannerView.IListener() {
                @Override
                public void onBannerLoaded(BannerView bannerView) {
                    if (_banner != null) {
                        _banner.setBannerView(bannerView);
                        _banner.autoUpdate();
                    }
                }

                @Override
                public void onBannerClick(BannerView bannerView) {
                }

                @Override
                public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo bannerErrorInfo) {
                    if (_banner != null)
                        _banner.autoUpdate();
                }

                @Override
                public void onBannerLeftApplication(BannerView bannerView) {
                }
            });
            _bannerView.load();
        }
    }

    @Override
    public View getBannerView() {
        BannerView bannerView = null;
        if (hasBanner()) {
            bannerView = _banner.bannerView;
            _banner.setBannerView(null);
            _banner.reset();
        }
        return bannerView;
    }

    @Override
    public boolean hasBanner() {
        return _banner != null && _banner.bannerView != null;
    }

    @Override
    void loadInterstitial(@NonNull AdItem adItem) {
        if (validPlatform() && adItem.valid()) {
            UnityAds.load(adItem.getId(), new IUnityAdsLoadListener() {
                @Override
                public void onUnityAdsAdLoaded(String s) {
                    if (_interstitial != null)
                        _interstitial.setUnityInterstitial(s);
                }

                @Override
                public void onUnityAdsFailedToLoad(String s, UnityAds.UnityAdsLoadError unityAdsLoadError, String s1) {
                    if (_interstitial != null)
                        _interstitial.autoUpdate();
                }
            });
        }
    }

    @Override
    public void showInterstitial(@Nullable IAdListener listener) {
        if (hasInterstitial() && AdEasyImpl.getInstance().getActivity() != null) {
            UnityAds.show(AdEasyImpl.getInstance().getActivity(),
                    _interstitial.unityInterstitial,
                    new IUnityAdsShowListener() {
                        @Override
                        public void onUnityAdsShowFailure(String s, UnityAds.UnityAdsShowError unityAdsShowError, String s1) {
                            if (_interstitial != null) {
                                _interstitial.setUnityInterstitial(null);
                                _interstitial.reset();
                            }
                            if (listener != null)
                                listener.onAdShowError();
                        }

                        @Override
                        public void onUnityAdsShowStart(String s) {
                            if (_interstitial != null) {
                                _interstitial.setUnityInterstitial(null);
                                _interstitial.reset();
                            }
                            if (listener != null)
                                listener.onAdShowStart();
                        }

                        @Override
                        public void onUnityAdsShowClick(String s) {

                        }

                        @Override
                        public void onUnityAdsShowComplete(String s, UnityAds.UnityAdsShowCompletionState unityAdsShowCompletionState) {
                            if (listener != null)
                                listener.onAdCompleted();
                        }
                    });
        }
    }

    @Override
    public boolean hasInterstitial() {
        return _interstitial != null && _interstitial.unityInterstitial != null;
    }

    @Override
    void loadVideo(@NonNull AdItem adItem) {
        if (validPlatform() && adItem != null && adItem.valid()) {
            UnityAds.load(adItem.getId(), new IUnityAdsLoadListener() {
                @Override
                public void onUnityAdsAdLoaded(String s) {
                    if (_video != null) _video.setUnityRewarded(s);
                }

                @Override
                public void onUnityAdsFailedToLoad(String s, UnityAds.UnityAdsLoadError unityAdsLoadError, String s1) {
                    if (_video != null) _video.autoUpdate();
                }
            });
        }
    }

    @Override
    public void showVideo(IRewardedResultCallback callback) {
        if (hasVideo() && AdEasyImpl.getInstance().getActivity() != null) {
            UnityAds.show(AdEasyImpl.getInstance().getActivity(),
                    _video.unityRewarded,
                    new IUnityAdsShowListener() {
                        @Override
                        public void onUnityAdsShowFailure(String s, UnityAds.UnityAdsShowError unityAdsShowError, String s1) {
                            if (_video != null) {
                                _video.setUnityRewarded(null);
                                _video.reset();
                            }
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
                            if (_video != null) {
                                _video.setUnityRewarded(null);
                                _video.reset();
                            }
                        }
                    });
        }
    }

    @Override
    public boolean hasVideo() {
        return _video != null && _video.unityRewarded != null;
    }

    @Override
    public int getWeights(String adType) {
        if (adType == AdInfo.TYPE_BANNER) {
            if (hasBanner())
                return _banner.adItem.getWeights();
        } else if (adType == AdInfo.TYPE_INTERSTITIAL) {
            if (hasInterstitial())
                return _interstitial.adItem.getWeights();
        } else if (adType == AdInfo.TYPE_VIDEO) {
            if (hasVideo())
                return _video.adItem.getWeights();
        }
        return 0;
    }

    @Override
    public AdImpl getBannerLive() {
        return _banner;
    }

    //unsupported blow methods
    @Override
    void loadNative(@NonNull AdItem adItem) {

    }

    @Override
    public View getNativeView(int templateLayoutId) {
        return null;
    }

    @Override
    public boolean hasNative() {
        return false;
    }


    @Override
    public void loadAppOpen() {

    }

    @Override
    public void showAppOpen() {

    }

    @Override
    public void registerAppOpenListener(Observer<Boolean> listener) {

    }

    @Override
    public void unregisterAppOpenListener(Observer<Boolean> listener) {

    }

    @Override
    public AdImpl getNativeLive() {
        return null;
    }

    //end unsupported

}
