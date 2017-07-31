package com.midtrans.sdk.corekit.core;

import com.midtrans.sdk.corekit.core.themes.BaseColorTheme;

/**
 * Created by ziahaqi on 7/31/17.
 */

public abstract class NewUiKitBuilder extends NewSdkBuilder<NewUiKitBuilder> {
    private static final String TAG = NewUiKitBuilder.class.getSimpleName();

    protected String merchantName = null;
    protected ISdkFlow sdkFlow;
    protected String defaultText;
    protected String boldText;
    protected String semiBoldText;
    protected IScanner externalScanner;
    protected UIKitCustomSetting UIKitCustomSetting;
    protected String flow;
    protected BaseColorTheme colorTheme;


    @Override
    public MidtransSDK build() {
        checkSdkProperties();
        return new MidtransSDK(this);
    }
}
