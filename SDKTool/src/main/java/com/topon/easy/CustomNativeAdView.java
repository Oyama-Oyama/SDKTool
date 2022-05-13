package com.topon.easy;

import android.content.Context;
import android.util.AttributeSet;

import com.anythink.core.api.ATAdInfo;
import com.anythink.nativead.api.ATNativeAdView;
import com.anythink.nativead.api.ATNativeEventExListener;
import com.anythink.nativead.api.NativeAd;

public class CustomNativeAdView extends ATNativeAdView {

    private NativeAd _nativeAd = null;

    public CustomNativeAdView(Context context) {
        super(context);
    }

    public CustomNativeAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNativeAdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        post(runnable);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(runnable);
        removeAllViews();
        if (_nativeAd != null)
            _nativeAd.destory();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (_nativeAd != null)
                return;
            _nativeAd = AdEasy.getNativeAd();
            if (_nativeAd != null) {
                CustomNativeAdView.this.removeAllViews();
                _nativeAd.setNativeEventListener(new ATNativeEventExListener() {
                    @Override
                    public void onDeeplinkCallback(ATNativeAdView atNativeAdView, ATAdInfo atAdInfo, boolean b) {
                        L.i("native ad --> onDeeplinkCallback");
                    }

                    @Override
                    public void onAdImpressed(ATNativeAdView atNativeAdView, ATAdInfo atAdInfo) {
                        L.i("native ad --> onAdImpressed");
                        AdEasy.loadNative();
                    }

                    @Override
                    public void onAdClicked(ATNativeAdView atNativeAdView, ATAdInfo atAdInfo) {
                        L.i("native ad --> onAdClicked");
                    }

                    @Override
                    public void onAdVideoStart(ATNativeAdView atNativeAdView) {
                        L.i("native ad --> onAdVideoStart");
                    }

                    @Override
                    public void onAdVideoEnd(ATNativeAdView atNativeAdView) {
                        L.i("native ad --> onAdVideoEnd");
                    }

                    @Override
                    public void onAdVideoProgress(ATNativeAdView atNativeAdView, int i) {
                        L.i("native ad --> onAdVideoProgress");
                    }
                });
                NativeDemoRender render = new NativeDemoRender(getContext());
                _nativeAd.renderAdView(CustomNativeAdView.this, render);
                _nativeAd.prepare(CustomNativeAdView.this, render.getClickView(), null);
            } else {
                CustomNativeAdView.this.postDelayed(runnable, 600);
            }
        }
    };

}
