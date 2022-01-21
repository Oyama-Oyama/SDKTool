package com.roman.garden.adeasy;

import android.app.Activity;
import android.app.Application;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public class AdEasyImpl {

    private static AdEasyImpl _instance = null;
    private Application _application = null;

    private WeakReference<Activity> _activityImpl = null;

    private BaseAdImpl _admobImpl;
    private BaseAdImpl _unityImpl;
    private BaseAdImpl _vungleImpl;
    private BaseAdImpl _panglelImpl;
    private BaseAdImpl _adcolonyImpl;

    public static AdEasyImpl of() {
        if (_instance == null) {
            synchronized (AdEasyImpl.class) {
                if (_instance == null)
                    _instance = new AdEasyImpl();
            }
        }
        return _instance;
    }

    public AdEasyImpl() {

    }

    public void onCreate(@NonNull Activity activity) {
        _activityImpl = new WeakReference<>(activity);
    }

    public void onResume(@NonNull Activity activity) {
        _activityImpl = new WeakReference<>(activity);
    }

    public void onDestroy() {
        cancelBanner();
    }

    /**
     * Application 初始化
     *
     * @param _application
     * @param _applicationImpl
     */
    public static void init(@NonNull Application _application,
                            @NonNull ADEasyApplicationImp _applicationImpl) {
        init(_application, null, _applicationImpl);
    }

    /**
     * Application 初始化
     *
     * @param _application
     * @param testDeviceId
     * @param _applicationImpl
     */
    public static void init(@NonNull Application _application,
                            @Nullable String testDeviceId,
                            @NonNull ADEasyApplicationImp _applicationImpl) {
        AdEasyImpl.of().setApplication(_application);
        //测试模式
        boolean testMode = !StringUtil.isEmpty(testDeviceId);
        //admob
        PlatformConfig _admob = _applicationImpl.createPlatformConfig(AdInfo.GROUP_ADMOB);
        AdEasyImpl.of().initAdmob(_admob, testMode, testDeviceId);

        //unity
        PlatformConfig _unity = _applicationImpl.createPlatformConfig(AdInfo.GROUP_UNITY);
        AdEasyImpl.of().initUnity(_unity, testMode);
        //vungle
        PlatformConfig _vungle = _applicationImpl.createPlatformConfig(AdInfo.GROUP_VUNGLE);
        AdEasyImpl.of().initVungle(_vungle, testMode);
        //pangle
        PlatformConfig _pangle = _applicationImpl.createPlatformConfig(AdInfo.GROUP_PANGLE);
        AdEasyImpl.of().initPangle(_pangle, testMode);
        //ADColony
        PlatformConfig _adcolony = _applicationImpl.createPlatformConfig(AdInfo.GROUP_ADCOLONY);
        AdEasyImpl.of().initAdColony(_adcolony, testMode);
    }

    private void initAdmob(@NonNull PlatformConfig _config, boolean testMode, @Nullable String testDeviceId) {
        if (_config == null) return;
        if (_admobImpl == null) _admobImpl = new AdmobImpl();
        _admobImpl.setPlatformConfig(_config, testMode, testDeviceId);
    }

    private void initUnity(@NonNull PlatformConfig _config, boolean testMode) {
        if (_config == null) return;
        if (_unityImpl == null) _unityImpl = new UnityImpl();
        _unityImpl.setPlatformConfig(_config, testMode);
    }

    private void initVungle(@NonNull PlatformConfig _config, boolean testMode) {
        if (_config == null) return;
        if (_vungleImpl == null) _vungleImpl = new VungleImpl();
        _vungleImpl.setPlatformConfig(_config, testMode);
    }

    private void initPangle(@NonNull PlatformConfig _config, boolean testMode) {
        if (_config == null) return;
        if (_panglelImpl == null) _panglelImpl = new PangleImpl();
        _panglelImpl.setPlatformConfig(_config, testMode);
    }

    private void initAdColony(@NonNull PlatformConfig _config, boolean testMode) {
        if (_config == null) return;
        if (_adcolonyImpl == null) _adcolonyImpl = new ADColonyImpl();
        _adcolonyImpl.setPlatformConfig(_config, testMode);
    }

    public Activity getActivity() {
        return _activityImpl == null ? null : _activityImpl.get();
    }

    public Application getApplication() {
        return _application;
    }

    private void setApplication(Application _application) {
        this._application = _application;
    }

    public void loadOpenScreen(){
        if(_admobImpl != null){
            _admobImpl.loadOpenScreen();
        }
    }

    public void showOpenScreen(){
        if(_admobImpl != null){
            _admobImpl.showOpenScreen();
        }
    }

    public boolean hasBanner() {
        return AdImpl.getInstance().getBannerSize() > 0;
    }

    public void showBanner() {
        showBanner(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
    }

    public void showBanner(int gravity) {
        if (getActivity() != null) {
            getActivity().getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    boolean alreadyAdded = false;
                    try {
                        ViewGroup parent = (ViewGroup) getActivity().getWindow().getDecorView();
                        for (int i = 0; i < parent.getChildCount(); i++) {
                            Object tag = parent.getChildAt(i).getTag();
                            if (tag != null) {
                                if (StringUtil.equal(String.valueOf(tag), Const.TAG_BANNER)) {
                                    parent.getChildAt(i).setVisibility(View.VISIBLE);
                                    alreadyAdded = true;
                                    break;
                                }
                            }
                        }
                        if (!alreadyAdded) {
                            BannerView bannerView = new BannerView(getActivity());
                            FrameLayout.LayoutParams params = bannerView.buildLayoutParams(gravity);
                            parent.addView(bannerView, params);
                        }
                    } catch (Exception e) {
                        LogUtil.e("show banner failed-->" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void cancelBanner() {
        try {
            if (getActivity() != null) {
                ViewGroup parent = (ViewGroup) getActivity().getWindow().getDecorView();
                for (int i = 0; i < parent.getChildCount(); i++) {
                    Object tag = parent.getChildAt(i).getTag();
                    if (tag != null) {
                        if (StringUtil.equal(String.valueOf(tag), Const.TAG_BANNER)) {
                            parent.removeView(parent.getChildAt(i));
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("close banner failed-->" + e.getMessage());
        }
    }

    public synchronized View getBannerView() {
        View view = null;
        while (AdImpl.getInstance().getBannerSize() > 0) {
            AdItem item = AdImpl.getInstance().getBanner();
            BaseAdImpl _impl = getTargetPlatformImpl(item);
            if (_impl != null) {
                if (_impl.getBannerView() != null) {
                    view = _impl.getBannerView();
                    _impl.reloadBanner();
                    break;
                }
            }
        }
        return view;
    }

    public boolean hasInterstitial() {
        return AdImpl.getInstance().getInterstitialSize() > 0;
    }

    public void showInterstitial() {
        AdItem item = AdImpl.getInstance().getInterstitial();
        BaseAdImpl _impl = getTargetPlatformImpl(item);
        if (_impl != null)
            _impl.showInterstitial();
    }

    public boolean hasVideo() {
        return AdImpl.getInstance().getVideoSize() > 0;
    }

    public void showVideo(@Nullable IVideoResultCallback callback) {
        AdItem item = AdImpl.getInstance().getVideo();
        BaseAdImpl _impl = getTargetPlatformImpl(item);
        if (_impl != null) {
            _impl.showVideo(callback);
        } else if (callback != null) callback.onResult(false);
    }

    public boolean hasNative() {
        return AdImpl.getInstance().getNativeSize() > 0;
    }

    public synchronized View getNativeView() {
        View view = null;
        while (AdImpl.getInstance().getNativeSize() > 0) {
            AdItem item = AdImpl.getInstance().getNative();
            BaseAdImpl _impl = getTargetPlatformImpl(item);
            if (_impl != null) {
                view = _impl.getNativeView();
                _impl.reloadNative();
                if (view != null)
                    break;
            }
        }
        return view;
    }

    private <T extends BaseAdImpl> T getTargetPlatformImpl(@NonNull AdItem item) {
        if (item == null) return null;
        if (item.getAdGroup() == AdInfo.GROUP_ADMOB) {
            return (T) _admobImpl;
        } else if (item.getAdGroup() == AdInfo.GROUP_UNITY) {
            return (T) _unityImpl;
        } else if (item.getAdGroup() == AdInfo.GROUP_PANGLE) {
            return (T) _panglelImpl;
        } else if (item.getAdGroup() == AdInfo.GROUP_VUNGLE) {
            return (T) _vungleImpl;
        } else if (item.getAdGroup() == AdInfo.GROUP_ADCOLONY) {
            return (T) _adcolonyImpl;
        }
        return null;
    }


}
