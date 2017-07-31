package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.util.Log;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;

/**
 * Created by ziahaqi on 7/30/17.
 */

public class CoreKitBuilder extends NewSdkBuilder<CoreKitBuilder> {

    private static final String TAG = NewSdkBuilder.class.getSimpleName();


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
    public MidtransSDK build() {

        if (context == null) {
            String message = "Context cannot be null. Please pass application context using setContext()";
            RuntimeException runtimeException = new RuntimeException(message);
            Log.e(NewSdkBuilder.CORE_FLOW, message, runtimeException);
            throw runtimeException;
        }

        if (clientKey == null || clientKey.equalsIgnoreCase("")) {
            String message = "Client key cannot be null or empty. Please pass the client key using setClientKey()";
            RuntimeException runtimeException = new RuntimeException(message);
            Log.e(NewSdkBuilder.CORE_FLOW, message, runtimeException);
            throw runtimeException;
        }

        if (transactionFinishedCallback == null) {
            String message = "You must implement transactionFinishedCallback. Please pass the transaction finished callback using setTransactionFinishedCallback()";
            RuntimeException runtimeException = new RuntimeException(message);
            Log.e(NewSdkBuilder.CORE_FLOW, message, runtimeException);
            throw runtimeException;
        }

        return new MidtransSDK(this);
    }
}
