package com.roman.garden.adeasy.ad;

import androidx.lifecycle.LiveData;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.unity3d.services.banners.BannerView;

import java.util.List;

public class AdImpl extends LiveData<AdImpl> {


    //admob
    public AdView _banner;

    public InterstitialAd _interstitial;

    public RewardedAd _rewarded;

    public NativeAd nativeAd;

    //unityads
    public BannerView bannerView;

    public String unityInterstitial;

    public String unityRewarded;

    //common
    public String adGroup;

    public AdItem adItem;

    public String _type;

    public AdImpl(String adGroup, String adType) {
        this.adGroup = adGroup;
        this._type = adType;
    }


    // start admob
    public void setBanner(AdView _banner) {
        this._banner = _banner;
    }

    public void setInterstitial(InterstitialAd _interstitial) {
        this._interstitial = _interstitial;
    }

    public void setRewarded(RewardedAd _rewarded) {
        this._rewarded = _rewarded;
    }

    public void setNativeAd(NativeAd nativeAd) {
        this.nativeAd = nativeAd;
    }

    //end admob

    //start UnityAds
    public void setBannerView(BannerView bannerView) {
        this.bannerView = bannerView;
    }

    public void setUnityInterstitial(String unityInterstitial) {
        this.unityInterstitial = unityInterstitial;
    }

    public void setUnityRewarded(String unityRewarded) {
        this.unityRewarded = unityRewarded;
    }

    //end UnityAds



    public void reset() {
        adItem = null;
        postValue(this);
    }

    public void reset(List<AdItem> src) {
        if (adItem == null) {
            adItem = src.get(0);
        } else {
            int index = src.indexOf(adItem);
            ++index;
            if (index >= src.size())
                index = 0;
            adItem = src.get(index);
        }
    }

    public void autoUpdate() {
        postValue(this);
    }

}
