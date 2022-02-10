package com.roman.garden.adeasy.ad;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class Platform {

    private String appId;
    private String adGroup;
    private List<AdItem> banner;
    private List<AdItem> interstitial;
    private List<AdItem> _native;
    private List<AdItem> video;
    private AdItem appOpen;

    public Platform(String appId,
                    String adGroup,
                    List<AdItem> banner,
                    List<AdItem> interstitial,
                    List<AdItem> _native,
                    List<AdItem> video,
                    AdItem appOpen) {
        this.appId = appId;
        this.adGroup = adGroup;
        this.banner = banner;
        this.interstitial = interstitial;
        this._native = _native;
        this.video = video;
        this.appOpen = appOpen;

        if (banner != null)
            Collections.sort(banner, comparator);
        if (interstitial != null)
            Collections.sort(interstitial, comparator);
        if (_native != null)
            Collections.sort(_native, comparator);
        if (video != null)
            Collections.sort(video, comparator);
    }

    private Comparator comparator = new Comparator<AdItem>() {
        @Override
        public int compare(AdItem o, AdItem t1) {
            return o.getPrice() - t1.getPrice();
        }
    };

    public String getAppId() {
        return appId;
    }

    public String getAdGroup() {
        return adGroup;
    }

    public List<AdItem> getBanner() {
        return banner;
    }

    public List<AdItem> getInterstitial() {
        return interstitial;
    }

    public List<AdItem> getNative() {
        return _native;
    }

    public List<AdItem> getVideo() {
        return video;
    }

    public AdItem getAppOpen() {
        return appOpen;
    }

    public static class Builder {

        private String appId;
        private String adGroup;
        private List<AdItem> banner;
        private List<AdItem> interstitial;
        private List<AdItem> _native;
        private List<AdItem> video;
        private AdItem appOpen;

        public Builder buildAdmob() {
            this.adGroup = AdInfo.GROUP_ADMOB;
            return this;
        }

        public Builder buildUnity(String appId) {
            this.adGroup = AdInfo.GROUP_UNITY;
            this.appId = appId;
            return this;
        }

        public Builder buildVungle(String appId) {
            this.adGroup = AdInfo.GROUP_VUNGLE;
            this.appId = appId;
            return this;
        }

        public Builder buildAdcolony(String adGroup) {
            this.adGroup = AdInfo.GROUP_ADCOLONY;
            this.adGroup = adGroup;
            return this;
        }

        public Builder addParameter(@NonNull final String _parameter,
                                    @NonNull final String _type,
                                    @IntRange(from = 0, to = 100) final int weights,
                                    int price) {
            if (_type == AdInfo.TYPE_BANNER) {
                if (banner == null) banner = new ArrayList<>();
                banner.add(new AdItem(_parameter, _type, weights, price));
            } else if (_type == AdInfo.TYPE_INTERSTITIAL) {
                if (interstitial == null) interstitial = new ArrayList<>();
                interstitial.add(new AdItem(_parameter, _type, weights, price));
            } else if (_type == AdInfo.TYPE_VIDEO) {
                if (video == null) video = new ArrayList<>();
                video.add(new AdItem(_parameter, _type, weights, price));
            } else if (_type == AdInfo.TYPE_NATIVE) {
                if (_native == null) _native = new ArrayList<>();
                _native.add(new AdItem(_parameter, _type, weights, price));
            } else if (_type == AdInfo.TYPE_OPEN_SCREEN) {
                appOpen = new AdItem(_parameter, _type, weights, price);
            }
            return this;
        }

        public Builder addParameter(@NonNull final String _parameter,
                                    @NonNull final String _type,
                                    @IntRange(from = 0, to = 100) final int weights) {
            return addParameter(_parameter, _type, weights, AdInfo.PRICE_ALL);
        }

        public Platform build() {
            return new Platform(appId, adGroup, banner, interstitial, _native, video, appOpen);
        }
    }

}
