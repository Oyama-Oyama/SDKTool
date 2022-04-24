package com.support.easy;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.appodeal.ads.NativeAd;
import com.appodeal.ads.native_ad.views.NativeAdViewAppWall;
import com.appodeal.ads.native_ad.views.NativeAdViewContentStream;
import com.appodeal.ads.native_ad.views.NativeAdViewNewsFeed;
import com.roman.garden.adeasy.R;

public class CustomNativeView extends LinearLayout {

    private int format = 1;

    public CustomNativeView(Context context) {
        this(context, null);
    }

    public CustomNativeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CustomNativeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NativeType);
        format = array.getInt(R.styleable.NativeType_format, 1);
        array.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        this.post(runnable);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.removeCallbacks(runnable);
        this.removeAllViews();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            NativeAd nativeAd = AdEasy.getNativeAd();
            if (nativeAd != null) {
                if (format == 1) {
                    NativeAdViewNewsFeed newsFeed = new NativeAdViewNewsFeed(getContext(), nativeAd);
                    addView(newsFeed);
                } else if (format == 2) {
                    NativeAdViewAppWall appWall = new NativeAdViewAppWall(getContext(), nativeAd);
                    addView(appWall);
                } else if (format == 3) {
                    NativeAdViewContentStream contentStream = new NativeAdViewContentStream(getContext(), nativeAd);
                    addView(contentStream);
                }
            } else {
                CustomNativeView.this.postDelayed(runnable, 600);
            }
        }
    };


}
