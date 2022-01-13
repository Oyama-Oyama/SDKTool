package com.roman.garden.adeasy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import java.util.List;

public class NativeView extends FrameLayout {

    public NativeView(@NonNull Context context) {
        this(context, null);
    }

    public NativeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public NativeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        AdImpl.getInstance().getNativeData().observeForever(observer);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AdImpl.getInstance().getNativeData().removeObserver(observer);
        removeAllViews();
    }

    private Observer observer = new Observer<List<AdItem>>() {
        @Override
        public void onChanged(List<AdItem> items) {
            getNative();
        }
    };

    private void getNative(){
        if (getChildCount() > 0)
            return;
        fillNative();
    }

    protected void fillNative(){
        View view = AdEasyImpl.of().getNativeView();
        if (view != null){
//            AdImpl.getInstance().getNativeData().removeObserver(observer);
            if (view.getParent() != null)
                ((ViewGroup)view.getParent()).removeView(view);
            addView(view);
        }
    }


}
