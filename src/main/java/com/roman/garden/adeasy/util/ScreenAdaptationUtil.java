package com.roman.garden.adeasy.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

/**
 * 屏幕适配
 */

public class ScreenAdaptationUtil {

    /**
     * 适配打孔屏、刘海配等特殊屏幕
     * 需要在 AndroidManifest.xml 里添加
     * <meta-data
     * android:name="android.max_aspect"
     * android:value="2.1"/> //Google 官方建议值为 2.1
     *
     * @param activity
     */
    public static void setupMaxAspect(@NonNull Activity activity) {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (applicationInfo != null) {
            DisplayMetrics _metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(_metrics);
            float _rate = _metrics.widthPixels * 1.0f / _metrics.heightPixels;
            float __rate = (float) (Math.round(_rate * 100) / 100.0);
            applicationInfo.metaData.putString("android.max_aspect", String.valueOf(__rate));
        }
    }

    /**
     * 设置状态栏透明 并且全屏
     *
     * @param activity
     */
    public static void translucentStatusBarFullScreen(@NonNull Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                int options = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                window.getDecorView().setSystemUiVisibility(options);
                window.setStatusBarColor(Color.TRANSPARENT);
                //状态栏文字自适应
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(activity, true);
            } else {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置状态栏透明 并且全屏
     *
     * @param activity
     */
    public static void translucentStatusBar(@NonNull Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(activity, true);
            } else {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setTranslucentStatus(@NonNull Activity activity, boolean on) throws Exception {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 自动隐藏虚拟按键
     *
     * @param activity
     */
    public static void hideVirtualKey(@NonNull Activity activity) {
        try {
            hideVirtualKeyAction(activity);
            activity.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    try {
                        hideVirtualKeyAction(activity);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new UnsupportedOperationException(e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void hideVirtualKeyAction(@NonNull Activity activity) throws Exception {
        //保持布局状态
        int uiOptions =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions |= 0x00001000;
        } else {
            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }


}
