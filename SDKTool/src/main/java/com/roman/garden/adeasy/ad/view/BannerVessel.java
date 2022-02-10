package com.roman.garden.adeasy.ad.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdView;
import com.roman.garden.adeasy.AdEasyImpl;
import com.roman.garden.adeasy.ad.AdImpl;
import com.roman.garden.adeasy.ad.AdInfo;
import com.roman.garden.adeasy.ad.impl.BaseAdImpl;
import com.unity3d.services.banners.BannerView;

public class BannerVessel extends FrameLayout {


    private AdImpl _admob;
    private AdImpl _unity;

    public BannerVessel(@NonNull Context context) {
        super(context);
        setBackgroundColor(Color.DKGRAY);
    }

    public BannerVessel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BannerVessel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        BaseAdImpl admobImpl = AdEasyImpl.getInstance().getAdmobImpl();
        if (admobImpl != null) {
            _admob = admobImpl.getBannerLive();
            if (_admob != null) {
                _admob.observeForever(observer);
            }
        }

        BaseAdImpl unityImpl = AdEasyImpl.getInstance().getUnityImpl();
        if (unityImpl != null) {
            _unity = unityImpl.getBannerLive();
            if (_unity != null) {
                _unity.observeForever(observer);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (_admob != null) _admob.removeObserver(observer);
        if (_unity != null) _unity.removeObserver(observer);
        removeAllViews();
    }

    private Observer observer = new Observer<AdImpl>() {
        @Override
        public void onChanged(AdImpl adImpl) {
            if (adImpl != null) {
                if (adImpl.adItem == null) return;
                setup(adImpl);
            }
        }
    };

    private synchronized void setup(AdImpl adImpl) {
        if (getChildCount() > 0) return;
        if (adImpl.adGroup == AdInfo.GROUP_ADMOB) {
            AdView view = adImpl._banner;
            if (view != null) {
                if (view.getParent() != null)
                    ((ViewPager) view.getParent()).removeAllViews();
                addView(view);

                adImpl._banner = null;
                adImpl.reset();
            }
        } else if (adImpl.adGroup == AdInfo.GROUP_UNITY) {
            BannerView view = adImpl.bannerView;
            if (view != null) {
                if (view.getParent() != null)
                    ((ViewPager) view.getParent()).removeAllViews();
                addView(view);

                adImpl.setBannerView(null);
                adImpl.reset();
            }
        }
    }


}
