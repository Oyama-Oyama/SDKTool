package com.roman.garden.sdk;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.roman.garden.adeasy.AdEasyImpl;
import com.roman.garden.adeasy.ad.IRewardedResultCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdEasyImpl.getInstance().onCreate(this);
        findViewById(R.id.full).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdEasyImpl.getInstance().showInterstitial();
            }
        });
        findViewById(R.id.banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdEasyImpl.getInstance().showBanner(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
            }
        });
        findViewById(R.id.reward).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdEasyImpl.getInstance().showVideo(new IRewardedResultCallback() {
                    @Override
                    public void onResult(boolean result) {
                        Log.e("--------", "result-->" + result);
                    }
                });
            }
        });
        findViewById(R.id.hbanner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdEasyImpl.getInstance().cancelBanner();
            }
        });

        FrameLayout nativevv = findViewById(R.id.nativevv);
        findViewById(R.id.showNative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nativevv.addView(AdEasyImpl.getInstance().getNativeView());
            }
        });
        findViewById(R.id.hideNative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nativevv.removeAllViews();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdEasyImpl.getInstance().onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}