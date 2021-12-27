package com.roman.garden.adeasy;

import android.app.Activity;
import android.app.Application;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AdEasyImpl {

    private static AdEasyImpl _instance = null;
    private Application _application = null;

    private WeakReference<Activity> _activityImpl = null;

    private BaseAdImpl _admobImpl;
    private BaseAdImpl _unityImpl;
    private BaseAdImpl _vungleImpl;
    private BaseAdImpl _pangelImpl;

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
        AdImpl.getInstance().getBannerData().observeForever(_bannerDataObserver);
    }

    public void onCreate(@NonNull Activity activity) {
        _activityImpl = new WeakReference<>(activity);
    }

    public void onResume(@NonNull Activity activity) {
        _activityImpl = new WeakReference<>(activity);
    }

    public void onDestroy() {
//        AdImpl.getInstance().getBannerData().removeObserver(_bannerDataObserver);
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
        if (_pangelImpl == null) _pangelImpl = new PangleImpl();
        _pangelImpl.setPlatformConfig(_config, testMode);
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

    private List<Pair<Integer, WeakReference<ViewGroup>>> _bannerContainers = new ArrayList<>();

    private Observer _bannerDataObserver = new Observer<List<AdItem>>() {
        @Override
        public void onChanged(List<AdItem> items) {
            Iterator<Pair<Integer, WeakReference<ViewGroup>>> itor = _bannerContainers.iterator();
            while (itor.hasNext()) {
                Pair<Integer, WeakReference<ViewGroup>> pair = itor.next();
                if (pair.second != null && pair.second.get() != null) {
                    if (pair.second.get().getChildCount() == 0) {
                        View bannerView = getBannerView();
                        if (bannerView != null) {
                            pair.second.get().addView(bannerView);
                        }
                    } else {
                        itor.remove();
                    }
                } else {
                    itor.remove();
                }
            }
        }
    };

    public boolean hasBanner() {
        return AdImpl.getInstance().getBannerSize() > 0;
    }

    public void showBanner() {
        if (getActivity() != null) {
            getActivity().getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup _container = null;
                    int taskId = getActivity().getTaskId();
                    Iterator<Pair<Integer, WeakReference<ViewGroup>>> itor = _bannerContainers.iterator();
                    while (itor.hasNext()) {
                        Pair<Integer, WeakReference<ViewGroup>> pair = itor.next();
                        if (pair.first == taskId) {
                            if (pair.second != null && pair.second.get() != null)
                                _container = pair.second.get();
                            else {
                                itor.remove();
                                break;
                            }
                        }
                    }

                    if (_container == null) {
                        _container = new FrameLayout(getActivity());
                        _container.setTag(101);
                        FrameLayout.LayoutParams _params = new FrameLayout.LayoutParams(-2, -2);
                        _params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                        ((FrameLayout) getActivity().getWindow().getDecorView()).addView(_container, _params);
                        View bannerView = getBannerView();
                        if (bannerView != null)
                            _container.addView(bannerView);
                        else {
                            _bannerContainers.add(new Pair<>(taskId, new WeakReference<>(_container)));
                        }
                    }
//                    else {
//                        if (_container.getChildCount() == 0) {
//                            View bannerView = getBannerView();
//                            if (bannerView != null)
//                                _container.addView(bannerView);
//                        }
//                    }
                }
            });
        }
    }

    public void cancelBanner() {
        if (getActivity() != null) {
            int taskId = getActivity().getTaskId();
            Iterator<Pair<Integer, WeakReference<ViewGroup>>> itor = _bannerContainers.iterator();
            while (itor.hasNext()) {
                Pair<Integer, WeakReference<ViewGroup>> pair = itor.next();
                if (pair.first == taskId) {
                    if (pair.second != null && pair.second.get() != null)
                        pair.second.get().removeAllViews();
                    itor.remove();
                }
            }
        }
    }

    private synchronized View getBannerView() {
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

    public View getNativeView() {
        View view = null;
        while (AdImpl.getInstance().getNativeSize() > 0) {
            AdItem item = AdImpl.getInstance().getNative();
            BaseAdImpl _impl = getTargetPlatformImpl(item);
            if (_impl != null) {
                if (_impl.getNativeView() != null) {
                    view = _impl.getNativeView();
                    _impl.reloadNative();
                    break;
                }
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
            return (T) _pangelImpl;
        } else if (item.getAdGroup() == AdInfo.GROUP_VUNGLE) {
            return (T) _vungleImpl;
        }
        return null;
    }


}
