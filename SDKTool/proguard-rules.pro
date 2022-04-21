# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Vungle
-dontwarn com.vungle.warren.downloader.DownloadRequestMediator$Status
-dontwarn com.vungle.warren.error.VungleError$ErrorCode

# Google
-dontwarn com.google.android.gms.common.GoogleApiAvailabilityLight
-dontwarn com.google.android.gms.ads.identifier.AdvertisingIdClient
-dontwarn com.google.android.gms.ads.identifier.AdvertisingIdClient$Info

# Moat SDK
-keep class com.moat.** { *; }
-dontwarn com.moat.**

# GSON
-keepattributes *Annotation*
-keepattributes Signature
# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# OkHttp + Okio
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn okhttp3.internal.platform.ConscryptPlatform

# pangle
-keep class com.bytedance.sdk.** { *; }
-keep class com.pgl.sys.ces.* {*;}

#IronSource
-keepclassmembers class com.ironsource.sdk.controller.IronSourceWebView$JSInterface {
    public *;
}
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
-keep public class com.google.android.gms.ads.** {
   public *;
}
-keep class com.ironsource.adapters.** { *;
}
-dontwarn com.ironsource.mediationsdk.**
-dontwarn com.ironsource.adapters.**
-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# For communication with AdColony's WebView
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
# For removing warnings due to lack of Multi-Window support
-dontwarn android.app.Activity

# Unity Ads
-keepattributes SourceFile,LineNumberTable
-keep class com.unity3d.** { *; }
-dontwarn com.unity3d.**

# Adcolony
-keep class com.jirbo.adcolony.** { *;}
-keep class com.adcolony.** { *;}
-keep class com.immersion.** { *;}
-dontnote com.immersion.**
-dontwarn android.webkit.**
-dontwarn com.jirbo.adcolony.**
-dontwarn com.adcolony.**
-keepclassmembers class com.adcolony.sdk.ADCNative** { *; }

#Vungle
-keepattributes *Annotation*, Signature
-keep class com.vungle.** { *;}
-dontwarn com.vungle.**
-dontnote com.vungle.**
-dontwarn com.moat.**
-keep class com.moat.analytics.mobile.vng.** { *;}
-keep class com.moat.** { public protected private *; }
-keep class dagger.**
-dontwarn de.greenrobot.event.util.**
-keep class de.greenrobot.event.**
-keep class javax.inject.**
-keep class rx.**
-dontwarn rx.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {undefined
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {undefined
   rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {undefined
   rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Admob
-keep class com.google.android.gms.ads.** { *; }

# IronSource
-keepclassmembers class com.ironsource.sdk.controller.IronSourceWebView$JSInterface { public *; }
-keepclassmembers class * implements android.os.Parcelable { public static final android.os.Parcelable$Creator *; }
-keep public class com.google.android.gms.ads.** { public *; }
-dontwarn com.moat.**
-keep class com.moat.** { public protected private *; }
-keep class com.ironsource.** { *; }
-dontwarn com.ironsource.**

# AdColonyV3
-keepclassmembers class * { @android.webkit.JavascriptInterface <methods>; }
-keep class com.adcolony.** { *; }
-dontwarn com.adcolony.**
-dontwarn android.app.Activity

# Google
-keep class com.google.android.gms.common.GooglePlayServicesUtil {*;}
-keep class com.google.android.gms.ads.identifier.** { *; }
-dontwarn com.google.android.gms.**
