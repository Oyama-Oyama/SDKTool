package com.roman.garden.adeasy;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import java.util.List;

/**
 * 直接添加此view到布局中，需要确保view不可滑动
 */
public class BannerView extends FrameLayout {

    public BannerView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        AdImpl.getInstance().getBannerData().observeForever(observer);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AdImpl.getInstance().getBannerData().removeObserver(observer);
        removeAllViews();
    }

    private Observer observer = new Observer<List<AdItem>>() {
        @Override
        public void onChanged(List<AdItem> items) {
            View view = AdEasyImpl.of().getBannerView();
            if (view != null) {
                AdImpl.getInstance().getBannerData().removeObserver(observer);
                BannerView.this.addView(view);
            }
        }
    };

    public LayoutParams buildLayoutParams(int gravity) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -2);
        params.gravity = gravity;
        setTag(Const.TAG_BANNER);
//        bannerView.setLayoutParams(params);
        return params;
    }


}
