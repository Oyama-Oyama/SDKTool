package com.support.easy;

public interface IRewardedVideoCallback {

    void onRewardedVideoShown();

    void onRewardedVideoShowFailed();

    void onRewarded(boolean result);

    void onRewardedVideoClicked();

    void onRewardedVideoClosed();

}
