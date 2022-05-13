package com.topon.easy;

public class Config {

    public String AD_SPLASH_PLACEMENT = null;
    public String AD_INTERSTITIAL_PLACEMENT = null;
    public String AD_NATIVE_PLACEMENT = null;
    public String AD_BANNER_PLACEMENT = null;
    public String AD_REWARDED_VIDEO_PLACEMENT = null;

    public String AD_APP_OPEN_PLACEMENT_ID = null;

    public Config setSplashPlacement(String AD_SPLASH_PLACEMENT) {
        this.AD_SPLASH_PLACEMENT = AD_SPLASH_PLACEMENT;
        return this;
    }

    public Config setInterstitialPlacement(String AD_INTERSTITIAL_PLACEMENT) {
        this.AD_INTERSTITIAL_PLACEMENT = AD_INTERSTITIAL_PLACEMENT;
        return this;
    }

    public Config setNativePlacement(String AD_NATIVE_PLACEMENT) {
        this.AD_NATIVE_PLACEMENT = AD_NATIVE_PLACEMENT;
        return this;
    }

    public Config setBannerPlacement(String AD_BANNER_PLACEMENT) {
        this.AD_BANNER_PLACEMENT = AD_BANNER_PLACEMENT;
        return this;
    }

    public Config setRewardedVideoPlacement(String AD_REWARDED_VIDEO_PLACEMENT) {
        this.AD_REWARDED_VIDEO_PLACEMENT = AD_REWARDED_VIDEO_PLACEMENT;
        return this;
    }

    public void setAppOpenPlacement(String AD_APP_OPEN_PLACEMENT_ID) {
        this.AD_APP_OPEN_PLACEMENT_ID = AD_APP_OPEN_PLACEMENT_ID;
    }
}
