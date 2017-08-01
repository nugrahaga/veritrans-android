package com.midtrans.sdk.corekit.core;

import android.content.Context;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;

/**
 * Created by ziahaqi on 7/30/17.
 */

public class SdkCoreKitBuilder extends NewSdkBuilder<SdkCoreKitBuilder> {

    private static final String TAG = NewSdkBuilder.class.getSimpleName();

    private SdkCoreKitBuilder() {

    }


    public static SdkCoreKitBuilder init() {
        return new SdkCoreKitBuilder();
    }


    @Override
    public SdkCoreKitBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    @Override
    public SdkCoreKitBuilder setClientKey(String clientKey) {
        this.clientKey = clientKey;
        return this;
    }

    @Override
    public SdkCoreKitBuilder enableLog(boolean enabled) {
        this.enableLog = enabled;
        return this;
    }

    @Override
    public SdkCoreKitBuilder setMerchantBaseUrl(String merchantBaseUrl) {
        this.merchantBaseUrl = merchantBaseUrl;
        return this;
    }

    @Override
    public SdkCoreKitBuilder setTransactionFinishedCallback(TransactionFinishedCallback callback) {
        this.transactionFinishedCallback = callback;
        return this;
    }

    @Override
    public SdkCoreKitBuilder enableBuiltInTokenStorage(boolean enabled) {
        return null;
    }

    @Override
    public MidtransSDK build() {

        checkSdkProperties();
        return new MidtransSDK(this);
    }
}
