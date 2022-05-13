package com.roman.garden.sdk;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.topon.easy.AdEasy;
import com.topon.easy.IRewardedVideoCallback;
import com.topon.easy.L;


public class MainActivity extends AppCompatActivity {

    private FrameLayout nativevv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //   AdEasy.init(this, "78642e9cf4be658208e758a7892fa76f6439a1fa2d0f2a4a", true);
//F3EDE78A2C3C4127A07CA5E97F0FDD02
        AdEasy.onCreate(this);

        nativevv = findViewById(R.id.nativevv);

        findViewById(R.id.full).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AdEasy.hasInterstitial())
                    AdEasy.showInterstitial(MainActivity.this);
                else
                    toast("显示插屏失败");
            }
        });
        findViewById(R.id.banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdEasy.showBanner(MainActivity.this);
            }
        });
        findViewById(R.id.reward).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AdEasy.hasRewardedVideo())
                    AdEasy.showRewardedVideo(MainActivity.this, new IRewardedVideoCallback() {
                        @Override
                        public void onRewarded(boolean result) {
                            L.E("rewared video result-->" + result);
                        }
                    });
                else
                    toast("显示 激励广告 失败");
            }
        });
        findViewById(R.id.hbanner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdEasy.hideBanner();
            }
        });
        findViewById(R.id.showNativeNormal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        findViewById(R.id.showNativeAppWall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nativevv.removeAllViews();
                View nativeView = AdEasy.getNativeAdView(view.getContext());
                if (nativeView != null) {
                    nativevv.addView(nativeView);
                } else {
                    toast("native view null");
                }
            }
        });

        findViewById(R.id.showNativeNewsFeed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                nativevv.removeAllViews();
//                View nativeView = AdEasy.buildNativeViewNewsFeed(view.getContext());
//                if (nativeView != null) {
//                    nativevv.addView(nativeView);
//                } else {
//                    toast("native view null");
//                }
            }
        });

        findViewById(R.id.showNativeContentStream).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                nativevv.removeAllViews();
//                View nativeView = AdEasy.buildNativeViewContentStream(view.getContext());
//                if (nativeView != null) {
//                    nativevv.addView(nativeView);
//                } else {
//                    toast("native view null");
//                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void toast(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }


}