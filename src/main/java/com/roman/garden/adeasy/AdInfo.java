package com.roman.garden.adeasy;

public final class AdInfo {

    public static final String GROUP_ADMOB = "admob";
    public static final String GROUP_UNITY = "unity";
    public static final String GROUP_VUNGLE = "vungle";
    public static final String GROUP_PANGLE = "pangle";

    public static final String TYPE_BANNER = "banner";
    public static final String TYPE_INTERSTITIAL = "interstitial";
    public static final String TYPE_VIDEO = "video";
    public static final String TYPE_NATIVE = "video";

    public static final String EVENT_AD_LOADED = "adAdLoaded";
    public static final String EVENT_AD_LOAD_FAILED = "onAdLoadFailed";
    public static final String EVENT_AD_CLOSED = "onAdClosed";
    public static final String EVENT_AD_OPENED = "onAdOpened";
    public static final String EVENT_AD_IMPRESSION = "onAdImpression";
    public static final String EVENT_AD_CLICKED = "onAdClicked";
    public static final String EVENT_AD_SHOW_FAILED = "onAdShowFailed";

    public static String makeMsg(String event, String group, String adType){
        return "{" + event + ":" + group + ", " + adType + "}";
    }

    public static String makeExtraMsg(String event, String group, String adType, String extraTitle, String extra){
        return "{" + event + ":" + group + ", " + adType + ", " + extraTitle + ":" + extra + "}";
    }

    public static PlatformConfig createAdmob(){
        return new PlatformConfig().createAdmob();
    }

    public static PlatformConfig createUnity(final String unityAppId){
        return new PlatformConfig().createUnity(unityAppId);
    }

    public static PlatformConfig createVungle(final String vungleAppId){
        return new PlatformConfig().createVungle(vungleAppId);
    }



}
