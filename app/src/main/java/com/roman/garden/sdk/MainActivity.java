package com.roman.garden.sdk;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.support.easy.AdEasy;
import com.support.easy.IInterstitialCallback;
import com.support.easy.IRewardedVideoCallback;


public class MainActivity extends AppCompatActivity {

    private FrameLayout nativevv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdEasy.init(this, "78642e9cf4be658208e758a7892fa76f6439a1fa2d0f2a4a", true);

        nativevv = findViewById(R.id.nativevv);

        findViewById(R.id.full).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AdEasy.canShowInterstitial())
                    AdEasy.showInterstitial(MainActivity.this, new IInterstitialCallback() {
                        @Override
                        public void onInterstitialShown() {
                            Log.e("-------", "onInterstitialShown");
                        }

                        @Override
                        public void onInterstitialShowFailed() {
                            Log.e("-------", "onInterstitialShowFailed");
                        }

                        @Override
                        public void onInterstitialClicked() {
                            Log.e("-------", "onInterstitialClicked");
                        }

                        @Override
                        public void onInterstitialClosed() {
                            Log.e("-------", "onInterstitialClosed");
                        }
                    });
                else
                    toast("显示插屏失败");
            }
        });
        findViewById(R.id.banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AdEasy.canShowBanner())
                    AdEasy.showBanner(MainActivity.this);
                else
                    toast("显示banner 失败");
            }
        });
        findViewById(R.id.reward).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AdEasy.canShowRewardedVideo())
                    AdEasy.showRewardedVideo(MainActivity.this, new IRewardedVideoCallback() {
                        @Override
                        public void onRewardedVideoShown() {
                            Log.e("-------", "onRewardedVideoShown");
                        }

                        @Override
                        public void onRewardedVideoShowFailed() {
                            Log.e("-------", "onRewardedVideoShowFailed");
                        }

                        @Override
                        public void onRewarded(boolean result) {
                            Log.e("-------", "onRewarded");
                        }

                        @Override
                        public void onRewardedVideoClicked() {
                            Log.e("-------", "onRewardedVideoClicked");
                        }

                        @Override
                        public void onRewardedVideoClosed() {
                            Log.e("-------", "onRewardedVideoClosed");
                        }
                    });
                else
                    toast("显示 激励广告 失败");
            }
        });
        findViewById(R.id.hbanner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdEasy.hideBanner(MainActivity.this);
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
                View nativeView = AdEasy.buildNativeViewAppWall(view.getContext());
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
                nativevv.removeAllViews();
                View nativeView = AdEasy.buildNativeViewNewsFeed(view.getContext());
                if (nativeView != null) {
                    nativevv.addView(nativeView);
                } else {
                    toast("native view null");
                }
            }
        });

        findViewById(R.id.showNativeContentStream).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nativevv.removeAllViews();
                View nativeView = AdEasy.buildNativeViewContentStream(view.getContext());
                if (nativeView != null) {
                    nativevv.addView(nativeView);
                } else {
                    toast("native view null");
                }
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