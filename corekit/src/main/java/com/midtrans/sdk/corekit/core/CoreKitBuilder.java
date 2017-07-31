package com.midtrans.sdk.corekit.core;

import android.content.Context;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;

/**
 * Created by ziahaqi on 7/30/17.
 */

public class CoreKitBuilder extends NewSdkBuilder<CoreKitBuilder> {

    private static final String TAG = NewSdkBuilder.class.getSimpleName();

    public static CoreKitBuilder init() {
        return new CoreKitBuilder();
    }


    @Override
    public CoreKitBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    @Override
    public CoreKitBuilder setClientKey(String clientKey) {
        this.clientKey = clientKey;
        return this;
    }

    @Override
    public CoreKitBuilder enableLog(boolean enabled) {
        this.enableLog = enabled;
        return this;
    }

    @Override
    public CoreKitBuilder setMerchantBaseUrl(String merchantBaseUrl) {
        this.merchantBaseUrl = merchantBaseUrl;
        return this;
    }

    @Override
    public CoreKitBuilder setTransactionFinishedCallback(TransactionFinishedCallback callback) {
        this.transactionFinishedCallback = callback;
        return this;
    }

    @Override
    public CoreKitBuilder enableBuiltInTokenStorage(boolean enabled) {
        return null;
    }

    @Override
    public MidtransSDK build() {

        checkSdkProperties();
        return new MidtransSDK(this);
    }
}
