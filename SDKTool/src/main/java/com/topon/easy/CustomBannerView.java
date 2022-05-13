package com.topon.easy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anythink.banner.api.ATBannerListener;
import com.anythink.banner.api.ATBannerView;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;
import com.anythink.network.admob.AdmobATConst;
import com.support.easy.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class CustomBannerView extends FrameLayout implements ATBannerListener {

    private String placementId = null;

    public CustomBannerView(@NonNull Context context) {
       this(context, null);

    }

    public CustomBannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CustomBannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getPlacementId();
    }

    private void getPlacementId(){
        this.placementId = AdEasy.getBannerPlacementId();
    }

    public void setPlacementId(String placementId) {
        this.placementId = placementId;
    }

    private ATBannerView _atBannerView;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!StringUtil.isEmpty(placementId)) {
            if (_atBannerView == null) {
                _atBannerView = new ATBannerView(getContext());
                _atBannerView.setPlacementId(placementId);
                Map<String, Object> localExtra = new HashMap<>();
////since v5.7.0, Admob Adaptive banner（自适应锚定横幅、自适应大尺寸横幅）
//                localExtra.put(AdmobATConst.ADAPTIVE_TYPE, AdmobATConst.ADAPTIVE_ANCHORED);//自适应锚定横幅
////localExtra.put(AdmobATConst.ADAPTIVE_TYPE, AdmobATConst.ADAPTIVE_INLINE);//自适应大尺寸横幅
//                localExtra.put(AdmobATConst.ADAPTIVE_ORIENTATION, AdmobATConst.ORIENTATION_CURRENT);
////localExtra.put(AdmobATConst.ADAPTIVE_ORIENTATION, AdmobATConst.ORIENTATION_PORTRAIT);
////localExtra.put(AdmobATConst.ADAPTIVE_ORIENTATION, AdmobATConst.ORIENTATION_LANDSCAPE);
//                localExtra.put(AdmobATConst.ADAPTIVE_WIDTH, 320);
//                _atBannerView.setLocalExtra(localExtra);
                _atBannerView.setBannerAdListener(this);
            }
            LayoutParams params = new LayoutParams(-1, -2);
            params.gravity = Gravity.BOTTOM|Gravity.CENTER;
            addView(_atBannerView, params);
            _atBannerView.loadAd();
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAllViews();
    }

    @Override
    public void onBannerLoaded() {
        L.i("onBannerLoaded");
    }

    @Override
    public void onBannerFailed(AdError adError) {
        L.i("onBannerFailed --> " + adError.getFullErrorInfo());
    }

    @Override
    public void onBannerClicked(ATAdInfo atAdInfo) {
        L.i("onBannerClicked");
    }

    @Override
    public void onBannerShow(ATAdInfo atAdInfo) {
        L.i("onBannerShow");
    }

    @Override
    public void onBannerClose(ATAdInfo atAdInfo) {
        L.i("onBannerClose");
    }

    @Override
    public void onBannerAutoRefreshed(ATAdInfo atAdInfo) {
        L.i("onBannerAutoRefreshed");
    }

    @Override
    public void onBannerAutoRefreshFail(AdError adError) {
        L.i("onBannerAutoRefreshFail");
    }
}
