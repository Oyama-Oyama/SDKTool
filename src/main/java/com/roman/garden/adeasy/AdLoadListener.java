package com.roman.garden.adeasy;

public interface AdLoadListener {

    void onAdLoaded(String _adGroup, String _adType);

    void onAdLoadFailed(String _adGroup, String _adType, String _error);

    void onAdClosed(String _adGroup, String _adType);

    void onAdOpened(String _adGroup, String _adType);

    void onAdImpression(String _adGroup, String _adType);

    void onAdClicked(String _adGroup, String _adType);

    void onAdShowFailed(String _adGroup, String _adType, String error);

}
