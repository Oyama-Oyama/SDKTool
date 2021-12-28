package com.roman.garden.adeasy;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

public final class PlatformConfig {

    private String _platform;

    private String _appId = "";

    private AdItem _bannerId;
    private AdItem _interstitialId;
    private AdItem _videoId;
    private AdItem _nativeId;


    public static class Builder {
        PlatformConfig config;

        public Builder() {
            config = new PlatformConfig();
        }

        public Builder createWidthAdmob() {
            config._platform = AdInfo.GROUP_ADMOB;
            return this;
        }

        public Builder createWidthUnity(@NonNull String appId) {
            config._appId = appId;
            config._platform = AdInfo.GROUP_UNITY;
            return this;
        }

        public Builder createWidthPangle(@NonNull String appId) {
            config._appId = appId;
            config._platform = AdInfo.GROUP_PANGLE;
            return this;
        }

        public Builder createWidthVungle(@NonNull String appId) {
            config._appId = appId;
            config._platform = AdInfo.GROUP_VUNGLE;
            return this;
        }

        public Builder createWidthAdcolony(@NonNull String appId) {
            config._appId = appId;
            config._platform = AdInfo.GROUP_ADCOLONY;
            return this;
        }

        public Builder addParameter(@NonNull final String _parameter, @NonNull final String _type, @IntRange(from = 0, to = 100) final int widget) {
            if (_type == AdInfo.TYPE_BANNER) {
                config._bannerId = AdItem.build(config._platform, _parameter, widget);
            } else if (_type == AdInfo.TYPE_INTERSTITIAL) {
                config._interstitialId = AdItem.build(config._platform, _parameter, widget);
            } else if (_type == AdInfo.TYPE_VIDEO) {
                config._videoId = AdItem.build(config._platform, _parameter, widget);
            } else if (_type == AdInfo.TYPE_NATIVE) {
                config._nativeId = AdItem.build(config._platform, _parameter, widget);
            }
            return this;
        }

        public PlatformConfig build() {
            return config;
        }

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

    public AdItem getVideoId() {
        return _videoId;
    }

    public AdItem getNativeId() {
        return _nativeId;
    }
}
