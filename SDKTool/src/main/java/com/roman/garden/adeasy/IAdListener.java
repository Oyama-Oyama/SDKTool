package com.roman.garden.adeasy;


public abstract class IAdListener {

    public abstract void onAdLoaded();

    public abstract void onAdFailedToLoad();

    public abstract void onAdShowStart();

    public abstract void onAdShowError();

    public abstract void onAdCompleted();

}
