package com.roman.garden.adeasy;

import android.app.Activity;
import android.app.Application;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.GravityInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.roman.garden.adeasy.ad.AdInfo;
import com.roman.garden.adeasy.ad.IRewardedResultCallback;
import com.roman.garden.adeasy.ad.Platform;
import com.roman.garden.adeasy.ad.impl.AdmobImpl;
import com.roman.garden.adeasy.ad.impl.BaseAdImpl;
import com.roman.garden.adeasy.ad.impl.UnityImpl;
import com.roman.garden.adeasy.ad.view.BannerVessel;
import com.roman.garden.adeasy.ad.view.NativeVessel;
import com.roman.garden.adeasy.util.AppStoreUtil;
import com.roman.garden.adeasy.util.CacheUtil;

import java.lang.ref.WeakReference;

public final class AdEasyImpl implements IAdEasy {

    private static AdEasyImpl _instance = null;
    private static final String TAG_BANNER = "tagBanner";

    private Application _application;
    private WeakReference<Activity> _activityImpl = null;
    private BaseAdImpl _admobImpl = null;
    private BaseAdImpl _unityImpl = null;

    private AdEasyImpl() {
    }

    public static AdEasyImpl getInstance() {
        if (_instance == null) {
            synchronized (AdEasyImpl.class) {
                if (_instance == null)
                    _instance = new AdEasyImpl();
            }
        }
        return _instance;
    }

    @Override
    public void init(@NonNull Application application, IAdEasyApplicationImpl impl) {
        this.init(application, impl, null);
    }

    @Override
    public void init(@NonNull Application application, IAdEasyApplicationImpl impl, String testDeviceId) {
        _application = application;
        CacheUtil.init(application);
        if (impl == null)
            throw new NullPointerException("AdEasy initialize error, IAdEasyApplicationImpl can not be null");
        initAdGroup(impl, testDeviceId);
    }

    @Override
    public void onCreate(@NonNull Activity activity) {
        _activityImpl = new WeakReference<>(activity);
    }

    @Override
    public void onResume(@NonNull Activity activity) {
        _activityImpl = new WeakReference<>(activity);
    }

    @Override
    public void onPause(@NonNull Activity activity) {

    }

    @Override
    public void onDestroy() {
        if (_admobImpl != null)
            _admobImpl.destroy();
        _admobImpl = null;
        if (_unityImpl != null)
            _unityImpl.destroy();
        _unityImpl = null;
    }

    public Application getApplication() {
        return _application;
    }

    public Activity getActivity() {
        if (_activityImpl == null) return null;
        return _activityImpl.get();
    }

    @Override
    public boolean hasBanner() {
        return (_admobImpl != null && _admobImpl.hasBanner()) ||
                (_unityImpl != null && _unityImpl.hasBanner());
    }

    @Override
    public View getBannerView() {
        return new BannerVessel(getApplication());
    }

    @Override
    public void showBanner() {
        this.showBanner(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void showBanner(@GravityInt int position) {
        if (getActivity() != null) {
            FrameLayout root = getActivity().findViewById(android.R.id.content);
            View view = getBannerView();
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
            params.gravity = position;
            view.setTag(TAG_BANNER);
            root.addView(view, params);
        }
    }

    @Override
    public void cancelBanner() {
        if (getActivity() != null) {
            FrameLayout root = getActivity().findViewById(android.R.id.content);
            View view = root.findViewWithTag(TAG_BANNER);
            if (view != null) {
                root.removeView(view);
            }
        }
    }

    @Override
    public boolean hasNative() {
        return (_admobImpl != null && _admobImpl.hasNative()) ||
                (_unityImpl != null && _unityImpl.hasNative());
    }

    @Override
    public View getNativeView() {
        return getNativeView(R.layout.template_medium);
    }

    @Override
    public View getNativeView(@LayoutRes int templateLayoutId) {
        return new NativeVessel(getApplication(), templateLayoutId);
    }

    @Override
    public boolean hasInterstitial() {
        return (_admobImpl != null && _admobImpl.hasInterstitial()) ||
                (_unityImpl != null && _unityImpl.hasInterstitial());
    }

    @Override
    public void showInterstitial() {
       showInterstitial(null);
    }

    @Override
    public void showInterstitial(@Nullable IAdListener adListener) {
        if (_admobImpl != null && _admobImpl.hasInterstitial() &&
                _unityImpl != null && _unityImpl.hasInterstitial()) {
            int _admobWeights = _admobImpl.getWeights(AdInfo.TYPE_INTERSTITIAL);
            int _unityWeights = _unityImpl.getWeights(AdInfo.TYPE_INTERSTITIAL);
            if (_admobWeights >= _unityWeights) {
                _admobImpl.showInterstitial(adListener);
            } else {
                _unityImpl.showInterstitial(adListener);
            }
        } else if (_admobImpl != null && _admobImpl.hasInterstitial()) {
            _admobImpl.showInterstitial(adListener);
        } else if (_unityImpl != null && _unityImpl.hasInterstitial()) {
            _unityImpl.showInterstitial(adListener);
        }
    }

    @Override
    public boolean hasVideo() {
        return (_admobImpl != null && _admobImpl.hasVideo()) ||
                (_unityImpl != null && _unityImpl.hasVideo());
    }

    @Override
    public void showVideo(@Nullable IRewardedResultCallback callback) {
        if (_admobImpl != null && _admobImpl.hasInterstitial() &&
                _unityImpl != null && _unityImpl.hasInterstitial()) {
            int _admobWeights = _admobImpl.getWeights(AdInfo.TYPE_INTERSTITIAL);
            int _unityWeights = _unityImpl.getWeights(AdInfo.TYPE_INTERSTITIAL);
            if (_admobWeights >= _unityWeights) {
                _admobImpl.showVideo(callback);
            } else {
                _unityImpl.showVideo(callback);
            }
        } else if (_admobImpl != null && _admobImpl.hasInterstitial()) {
            _admobImpl.showVideo(callback);
        } else if (_unityImpl != null && _unityImpl.hasInterstitial()) {
            _unityImpl.showVideo(callback);
        }
    }

    @Override
    public void loadAppOpen() {
        if (_admobImpl != null)
            _admobImpl.loadAppOpen();
    }

    @Override
    public void showAppOpen() {
        if (_admobImpl != null)
            _admobImpl.showAppOpen();
    }

    @Override
    public void registerAppOpenListener(Observer<Boolean> listener) {
        if (_admobImpl != null)
            _admobImpl.registerAppOpenListener(listener);
    }

    @Override
    public void unregisterAppOpenListener(Observer<Boolean> listener) {
        if (_admobImpl != null)
            _admobImpl.unregisterAppOpenListener(listener);
    }


    @Override
    public void rate() {
        AppStoreUtil.openPlayStore(getApplication(), getApplication().getPackageName());
    }

    public BaseAdImpl getAdmobImpl() {
        return _admobImpl;
    }

    public BaseAdImpl getUnityImpl() {
        return _unityImpl;
    }

    private void initAdGroup(IAdEasyApplicationImpl impl, String testDeviceId) {
        Platform _admob = impl.getPlatform(AdInfo.GROUP_ADMOB);
        if (_admob != null) {
            if (_admobImpl == null) _admobImpl = new AdmobImpl();
            _admobImpl.setup(_admob, testDeviceId);
        }
        Platform _unity = impl.getPlatform(AdInfo.GROUP_UNITY);
        if (_unity != null) {
            if (_unityImpl == null) _unityImpl = new UnityImpl();
            _unityImpl.setup(_unity, testDeviceId);
        }
    }


}
