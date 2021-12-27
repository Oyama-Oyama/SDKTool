package com.roman.garden.adeasy;

import androidx.annotation.NonNull;

public final class AdItem {

    private String _adId;
    private int _adWidget;
    private String _adGroup;

    private AdItem(){}

    public static AdItem build(@NonNull String _group, @NonNull String _id, int _widget){
        AdItem _info = new AdItem();
        _info._adGroup = _group;
        _info._adId = _id;
        _info._adWidget = _widget;
        return _info;
    }

    public String getAdId() {
        return _adId;
    }

    public int getAdWidget() {
        return _adWidget;
    }

    public String getAdGroup() {
        return _adGroup;
    }

    @Override
    public String toString() {
        return "AdItem{" +
                "_adId='" + _adId + '\'' +
                ", _adWidget=" + _adWidget +
                ", _adGroup='" + _adGroup + '\'' +
                '}';
    }
}
