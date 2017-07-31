package com.midtrans.sdk.uikit;

import android.content.Context;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.NewUiKitBuilder;
import com.midtrans.sdk.corekit.core.MidtransSDK;

/**
 * Created by ziahaqi on 7/31/17.
 */

public class UikitBuilder extends NewUiKitBuilder {


    @Override
    public UikitBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    @Override
    public UikitBuilder setClientKey(String clientKey) {
        this.clientKey = clientKey;
        return this;
    }

    @Override
    public UikitBuilder enableLog(boolean enabled) {
        this.enableLog = enabled;
        return this;
    }

    @Override
    public UikitBuilder setMerchantBaseUrl(String merchantBaseUrl) {
        this.merchantBaseUrl = merchantBaseUrl;
        return this;
    }

    @Override
    public UikitBuilder setTransactionFinishedCallback(TransactionFinishedCallback callback) {
        this.transactionFinishedCallback = callback;
        return this;
    }

    @Override
    public NewUiKitBuilder enableBuiltInTokenStorage(boolean enabled) {
        this.enableBuiltInTokenStorage = enabled;
        return this;
    }

}
