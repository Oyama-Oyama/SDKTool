1. 使用 AndroidX
    在 gradle.properties 文件内添加
        android.useAndroidX=true
        # Automatically convert third-party libraries to use AndroidX
        android.enableJetifier=true

2. 添加网络权限
    在 AndroidManifest.xml 文件内添加
        <uses-permission android:name="android.permission.INTERNET"/>
        <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
        <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
        <uses-permission android:name="android.permission.WAKE_LOCK" />

3.  创建 Application.java, 并在  AndroidManifest.xml 文件内引用

4.  在 Application 类内初始化 配置信息
        AdEasyImpl.init(Application.class, AdEasyImplApplicationImp);
        并在 createPlatformConfig 方法内，创建不同广告平台广告 id 信息

5.  如使用 Admob 广告，在AndroidManifest.xml 中的<Application>标签内 添加
         <meta-data
                android:name="com.google.android.gms.ads.APPLICATION_ID"
                android:value="ca-app-pub-3280143961801642~1025778304"/>
         （value 值 为 Admob 应用ID）


 6. 如果需要在admob瀑布流增加AdColony广告源，需要添加 AdColony SDK 引用

 7. Vungle 在admob后台增加即可


TIP:
    1.Pangle 需要填写公司信息  暂时无法使用
    2. 在调用 showBanner 接口时，需要提前调用 closeBanner 接口，防止任务重复