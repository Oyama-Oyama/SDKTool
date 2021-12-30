package com.roman.garden.adeasy;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

final class AdImpl {


    private List<AdItem> _banners = new ArrayList<>();
    private List<AdItem> _interstitials = new ArrayList<>();
    private List<AdItem> _videos = new ArrayList<>();
    private List<AdItem> _natives = new ArrayList<>();

    private MutableLiveData<List<AdItem>> _bannerData = null;

    private static AdImpl _impl = null;

    private AdImpl() {
    }

    public static AdImpl getInstance() {
        if (_impl == null) {
            synchronized (AdImpl.class) {
                if (_impl == null)
                    _impl = new AdImpl();
            }
        }
        return _impl;
    }

    public void sort(@NonNull List<AdItem> items) {
        Collections.sort(items, new Comparator<AdItem>() {
            @Override
            public int compare(AdItem item, AdItem t1) {
                return t1.getAdWidget() - item.getAdWidget();
//                return item.getAdWidget() - t1.getAdWidget();
            }
        });
    }

    public MutableLiveData<List<AdItem>> getBannerData() {
        if (_bannerData == null)
            _bannerData = new MutableLiveData<>();
        return _bannerData;
    }

    public void addBanner(@NonNull AdItem item) {
        _banners.add(item);
        sort(_banners);
        if (_bannerData != null)
            _bannerData.postValue(_banners);
    }

    public AdItem getBanner() {
        return getTopItem(_banners);
    }

    public int getBannerSize() {
        return _banners.size();
    }

    public void addInterstitial(@NonNull AdItem item) {
        _interstitials.add(item);
        sort(_interstitials);
    }

    public AdItem getInterstitial() {
        return getTopItem(_interstitials);
    }

    public int getInterstitialSize() {
        return _interstitials.size();
    }

    public void addVideo(@NonNull AdItem item) {
        _videos.add(item);
        sort(_videos);
    }

    public AdItem getVideo() {
        return getTopItem(_videos);
    }

    public int getVideoSize() {
        return _videos.size();
    }

    public void addNative(@NonNull AdItem item) {
        _natives.add(item);
        sort(_natives);
    }

    public AdItem getNative() {
        return getTopItem(_natives);
    }

    public int getNativeSize() {
        return _natives.size();
    }

    private AdItem getTopItem(@NonNull List<AdItem> items) {
        Iterator<AdItem> itemIterator = items.iterator();
        while (itemIterator.hasNext()) {
            AdItem item = itemIterator.next();
            itemIterator.remove();
            return item;
        }
        return null;
    }


}
