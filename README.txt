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

3.  创建 Application.java, 并在  AndroidManifest.xml 文件内引用

4.  在 Application 类内初始化 配置信息
        AdEasy.init(Application.class, ADEasyApplicationImp);
        并在 createPlatformConfig 方法内，创建不同广告平台广告 id 信息

5.  如使用 Admob 广告，在AndroidManifest.xml 中的<Application>标签内 添加
         <meta-data
                android:name="com.google.android.gms.ads.APPLICATION_ID"
                android:value="ca-app-pub-3280143961801642~1025778304"/>
         （value 值 为 Admob 应用ID）

6.  在 AppActivity.java 类内 创建 SDK 调用方法
    public class AppActivity extends Cocos2dxActivity implements AdEasyActivityImpl {

        private FrameLayout _bannerContainer = null;
        private Runnable _bannerRunnable = null;
        Handler _handler = new Handler();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.setEnableVirtualButton(false);
            super.onCreate(savedInstanceState);
            // Workaround in https://stackoverflow.com/questions/16283079/re-launch-of-activity-on-home-button-but-only-the-first-time/16447508
            if (!isTaskRoot()) {
                // Android launched another instance of the root activity into an existing task
                //  so just quietly finish and go away, dropping the user back into the activity
                //  at the top of the stack (ie: the last state of this task)
                // Don't need to finish it again since it's finished in super.onCreate .
                return;
            }
            // Make sure we're running on Pie or higher to change cutout mode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // Enable rendering into the cutout area
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                getWindow().setAttributes(lp);
            }
            // DO OTHER INITIALIZATION BELOW

            if(_bannerContainer == null){
                _bannerContainer = new FrameLayout(this);
            }

            AdEasy.of().onCreate(this, this);
            getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    FrameLayout.LayoutParams _params = new FrameLayout.LayoutParams(-2, -2);
                    _params.gravity = Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
                    ((FrameLayout)getWindow().getDecorView()).addView(_bannerContainer, _params);
                }
            });

        }

        @Override
        public ViewGroup getBannerContainer() {
            return _bannerContainer;
        }

        public void showBannerAd(){
            if (_bannerRunnable == null){
                _bannerRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (hasBanner()){
                            AdEasy.of().showBanner();
                        } else {
                            _handler.postDelayed(_bannerRunnable, 3 * 1000);
                        }
                    }
                };
            }
            _handler.removeCallbacks(_bannerRunnable);
            _handler.post(_bannerRunnable);
        }

        public void closeBannerAd(){
            if(_handler != null && _bannerRunnable != null)
                _handler.removeCallbacks(_bannerRunnable);
            AdEasy.of().hideBanner();
        }

        public static native void onRewardResult(int rewardId, boolean success);
        public static native void onRewardResult2(boolean success);

        public static boolean hasInterstitial(){
            try {
                return AdEasy.of().hasInterstitial();
            } catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }

        public static void showInterstitial() {
            try {
                UIHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        AdEasy.of().showInterstitial();
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        public static boolean hasRewardVideo(){
            try {
                return AdEasy.of().hasVideo();
            } catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }

        public static void showRewardVideo(int _id){
            try{
                UIHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        AdEasy.of().showVideo(new RewardVideoResultListener() {
                            @Override
                            public void onRewardVideoResult(boolean result) {
                                onRewardResult(_id, result);
                            }
                        });
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        public static boolean hasBanner(){
            try {
                return AdEasy.of().hasBanner();
            } catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }

        public static void showBanner(){
            try {
                UIHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        ((AppActivity) getContext()).showBannerAd();
                    }
                });

            } catch (Exception e){
                e.printStackTrace();
            }
        }

        public static void closeBanner(){
            try {
                UIHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        ((AppActivity) getContext()).closeBannerAd();
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        public static void toast(String str){
            try {
                UIHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }

      public static void share(String msg){
            try{
                Intent textIntent = new Intent(Intent.ACTION_SEND);
                textIntent.setType("text/plain");
                textIntent.putExtra(Intent.EXTRA_TEXT, "Drop the blocks to create vertically or horizontally lines of blocks without gaps. When such a line is created, it gets destroyed. Keep your board clear and keep your cool as things heat up in this simple but addictive puzzle game!\n : https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "-----" + msg);
                getContext().startActivity(Intent.createChooser(textIntent, getContext().getString(R.string.app_name) + ":"));
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        public static int getScreenWidth(){
            try{
                DisplayMetrics _metrics = new DisplayMetrics();
                ((AppActivity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(_metrics);
                return (int)(_metrics.widthPixels + _metrics.density + 0.5f);
            } catch (Exception e){
                e.printStackTrace();
            }
            return 720;
        }

        public static int getScreenHeight(){
            try{
                DisplayMetrics _metrics = new DisplayMetrics();
                ((AppActivity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(_metrics);
                return (int)(_metrics.heightPixels + _metrics.density + 0.5f);
            } catch (Exception e){
                e.printStackTrace();
            }
            return 1280;
        }

        public static int dip2px(float dp){
            float _scale = getContext().getResources().getDisplayMetrics().density;
            return (int)(dp * _scale + 0.5f);
        }

        public static int px2dp(float px){
            float _scale = getContext().getResources().getDisplayMetrics().density;
            return (int)(px / _scale + 0.5f);
        }

        public static int sp2px(float sp){
            float _scale = getContext().getResources().getDisplayMetrics().density;
            return (int)(sp * _scale + 0.5f);
        }

        @Override
        protected void onResume() {
            super.onResume();
            AdEasy.of().onResume(this, this);
        }

        @Override
        protected void onPause() {
            super.onPause();
            AdEasy.of().onPause();
        }

        @Override
        protected void onDestroy() {
            closeBannerAd();
            AdEasy.of().onDestroy();
            super.onDestroy();
        }

    }



TIP:
    1. 在调用 showBanner 接口时，需要提前调用 closeBanner 接口，防止任务重复