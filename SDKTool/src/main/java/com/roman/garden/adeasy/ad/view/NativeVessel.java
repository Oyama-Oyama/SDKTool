package com.roman.garden.adeasy.ad.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.roman.garden.adeasy.AdEasyImpl;
import com.roman.garden.adeasy.R;
import com.roman.garden.adeasy.ad.AdImpl;
import com.roman.garden.adeasy.ad.impl.BaseAdImpl;

public class NativeVessel extends FrameLayout {

    protected int templateLayoutId = -1;

    private AdImpl impl = null;

    public NativeVessel(@NonNull Context context) {
        this(context, null);
    }

    public NativeVessel(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public NativeVessel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TemplateView);
        templateLayoutId = array.getResourceId(R.styleable.TemplateView_gnt_template_type, R.layout.template_small);
        array.recycle();
    }

    public NativeVessel(@NonNull Context context, @LayoutRes int templateLayoutId) {
        super(context);
        this.templateLayoutId = templateLayoutId;
        setBackgroundColor(Color.DKGRAY);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BaseAdImpl admobImpl = AdEasyImpl.getInstance().getAdmobImpl();
        if (admobImpl != null) {
            impl = admobImpl.getNativeLive();
            if (impl != null) {
                impl.observeForever(observer);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (impl != null)
            impl.removeObserver(observer);
        removeAllViews();
    }

    private Observer observer = new Observer<AdImpl>() {
        @Override
        public void onChanged(AdImpl adImpl) {
            if (adImpl != null) {
                if (adImpl.adItem == null) return;
                if (adImpl.nativeAd == null) return;
                boolean status = setup();
                if (status) {
                    adImpl.nativeAd = null;
                    adImpl.reset();
                }
            }
        }
    };

    private synchronized boolean setup() {
        if (getChildCount() > 0) return false;
        try {
            View templateView = AdEasyImpl.getInstance().getAdmobImpl().getNativeView(templateLayoutId);
            if (templateView.getParent() != null)
                ((ViewGroup) templateView.getParent()).removeAllViews();
            addView(templateView);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
