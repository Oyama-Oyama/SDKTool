package com.roman.garden.adeasy.ad;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.roman.garden.adeasy.AdEasyImpl;
import com.roman.garden.adeasy.util.StringUtil;

import java.util.Date;

public class AdmobAppOpenManager implements LifecycleObserver {

    private String AD_UNIT_ID = null;//"ca-app-pub-3940256099942544/3419835294";

    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

    private AppOpenAd appOpenAd = null;
    private boolean isLoadingAd = false;
    private boolean isShowingAd = false;

    /**
     * Keep track of the time an app open ad is loaded to ensure you don't show an expired ad.
     */
    private long loadTime = 0;

    private AdmobAppOpenManager() {
    }

    /**
     * Constructor.
     */
    public AdmobAppOpenManager(@NonNull String ad_unit_id) {
        this.AD_UNIT_ID = ad_unit_id;
        if (!StringUtil.isEmpty(AD_UNIT_ID)) {
            ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
            ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        }
    }

    public void destroy() {
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
    }

    /**
     * 应用进入前台
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onMoveToForeground() {
        // Show the ad (if available) when the app moves to foreground.
        AdEasyImpl.getInstance().showAppOpen();
    }

    /**
     * 应用进入后台
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onMoveToBackground() {
        // Show the ad (if available) when the app moves to foreground.
        AdEasyImpl.getInstance().loadAppOpen();
    }

    /**
     * Load an ad.
     *
     * @param context the context of the activity that loads the ad
     */
    public void loadAd(Context context) {
        if (AD_UNIT_ID == null) return;
        if (AD_UNIT_ID.trim().equals("")) return;
        // Do not load ad if there is an unused ad or one is already loading.
        if (isLoadingAd || isAdAvailable()) {
            return;
        }

        isLoadingAd = true;
        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(
                context,
                AD_UNIT_ID,
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                new AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        appOpenAd = ad;
                        isLoadingAd = false;
                        loadTime = (new Date()).getTime();
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        isLoadingAd = false;
                    }
                });
    }

    /**
     * Check if ad was loaded more than n hours ago.
     */
    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    /**
     * Check if ad exists and can be shown.
     */
    private boolean isAdAvailable() {
        // Ad references in the app open beta will time out after four hours, but this time limit
        // may change in future beta versions. For details, see:
        // https://support.google.com/admob/answer/9341964?hl=en
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    /**
     * Show the ad if one isn't already showing.
     *
     * @param activity the activity that shows the app open ad
     */
    public void showAdIfAvailable(@NonNull final Activity activity) {
        showAdIfAvailable(
                activity,
                new OnShowAdCompleteListener() {
                    @Override
                    public void onShowAdComplete() {
                        // Empty because the user will go back to the activity that shows the ad.
//                        AdEasyImpl.of().loadOpenScreen();
                    }
                });
    }

    /**
     * Show the ad if one isn't already showing.
     *
     * @param activity                 the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    private void showAdIfAvailable(
            @NonNull final Activity activity,
            @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        // If the app open ad is already showing, do not show the ad again.
        if (isShowingAd) {
            return;
        }

        // If the app open ad is not available yet, invoke the callback then load the ad.
        if (!isAdAvailable()) {
            onShowAdCompleteListener.onShowAdComplete();
            return;
        }

        appOpenAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    /** Called when full screen content is dismissed. */
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null;
                        isShowingAd = false;

                        onShowAdCompleteListener.onShowAdComplete();
                    }

                    /** Called when fullscreen content failed to show. */
                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        appOpenAd = null;
                        isShowingAd = false;

                        onShowAdCompleteListener.onShowAdComplete();
                    }

                    /** Called when fullscreen content is shown. */
                    @Override
                    public void onAdShowedFullScreenContent() {

                    }
                });

        isShowingAd = true;
        appOpenAd.show(activity);
    }
}