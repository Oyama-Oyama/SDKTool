package com.roman.garden.adeasy;

import androidx.annotation.NonNull;

import com.roman.garden.adeasy.ad.Platform;

public interface IAdEasyApplicationImpl {

    Platform getPlatform(@NonNull String adGroup);

}
