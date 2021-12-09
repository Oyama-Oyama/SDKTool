package com.roman.garden.adeasy;

public class PlatformConfig {

    private String _platform;

    private String _appId = "";

    private AdItem _bannerId;
    private AdItem _interstitialId;
    private AdItem _interstitialVideoId;
    private AdItem _videoId;
    private AdItem _nativeId;

    public PlatformConfig createAdmob(){
        _platform = AdInfo.GROUP_ADMOB;
        return this;
    }

    public PlatformConfig createUnity(String unityAppId){
        _platform = AdInfo.GROUP_UNITY;
        _appId = unityAppId;
        return this;
    }

    public PlatformConfig createVungle(String vungleAppId){
        _platform = AdInfo.GROUP_VUNGLE;
        _appId = vungleAppId;
        return this;
    }

    public PlatformConfig addParameter(final String _parameter, final String _type, final int widget){
        if (_type == AdInfo.TYPE_BANNER){
            _bannerId = AdItem.build(_platform, _parameter, widget);
        } else if(_type == AdInfo.TYPE_INTERSTITIAL){
            _interstitialId = AdItem.build(_platform, _parameter, widget);
        } else if(_type == AdInfo.TYPE_VIDEO){
            _videoId = AdItem.build(_platform, _parameter, widget);
        } else if(_type == AdInfo.TYPE_NATIVE){
            _nativeId = AdItem.build(_platform, _parameter, widget);
        }
        return this;
    }

    public String getPlatform() {
        return _platform;
    }

    public String getAppId() {
        return _appId;
    }

    public AdItem getBannerId() {
        return _bannerId;
    }

    public AdItem getInterstitialId() {
        return _interstitialId;
    }

    public AdItem getInterstitialVideoId() {
        return _interstitialVideoId;
    }

    public AdItem getVideoId() {
        return _videoId;
    }

    public AdItem getNativeId() {
        return _nativeId;
    }
}
