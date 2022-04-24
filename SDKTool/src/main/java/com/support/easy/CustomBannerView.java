package com.support.easy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.appodeal.ads.Appodeal;
import com.roman.garden.adeasy.R;

public class CustomBannerView extends LinearLayout {


    public CustomBannerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        View.inflate(getContext(), R.layout.view_banner, this);
        Appodeal.setBannerViewId(R.id.appodeal_banner_view);
        // Appodeal.show((Activity) getContext(), Appodeal.BANNER_VIEW);
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                AdEasy.showBanner();
            }
        }, 1000);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAllViews();
    }


}
