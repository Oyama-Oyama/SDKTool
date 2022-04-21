package com.support.easy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

public class AppStoreUtil {


    public static boolean isInstalled(Context context, String pkgName){
        try{
            if (context.getPackageManager().getPackageInfo(pkgName, 0) != null)
                return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void openPlayStore(Context context, String pkg) {
        Intent i = new Intent("android.intent.action.VIEW");
        String url = fixUrl(pkg);
        boolean isGooglePlay = isPlayStoreUrl(url);
        if (isGooglePlay) {
            if (hasPlayStore(context)) {
                launchPlayStore(context, url, i);
            } else {
                launchBrowser(context, url, i);
            }
        } else {
            launchBrowser(context, url, i);
        }
    }

    public static String fixUrl(String url) {
        return url.startsWith("http") ? url : "https://play.google.com/store/apps/details?id=" + url;
    }

    public static boolean isPlayStoreUrl(String url) {
        return url.startsWith("https://play.google.com/store/apps/details?id=");
    }

    public static boolean hasPlayStore(Context context) {
        try {
            int var2 = context.getPackageManager().getApplicationEnabledSetting("com.android.vending");
            return var2 == 0 || var2 == 1;
        } catch (Exception var21) {
            return false;
        }
    }

    public static void launchPlayStore(Context context, String url, Intent i) {
        String marketUrl = "market://details?id=";
        url = url.replace("https://play.google.com/store/apps/details?id=", "market://details?id=");
        i.setPackage("com.android.vending");
        i.setData(Uri.parse(url));
        launchApp(context, i);
    }

    public static void launchApp(Context context, Intent i) {
        try {
            if (context instanceof Activity) {
                context.startActivity(i);
            } else {
                context.startActivity(i.setFlags(268435456));
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    public static void launchBrowser(Context context, String url, Intent i) {
        i.setData(Uri.parse(url));
        String browserPackageName = getDefaultBrowserPackageName(context, i);
        if (browserPackageName != null) {
            i.setPackage(browserPackageName);
        }
        launchApp(context, i);
    }

    public static String getDefaultBrowserPackageName(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List resolveInfos = packageManager.queryIntentActivities(intent, 0);
        if (resolveInfos.size() > 0) {
            ResolveInfo info = (ResolveInfo) resolveInfos.get(0);
            return info.activityInfo.packageName;
        } else {
            return null;
        }
    }

}
