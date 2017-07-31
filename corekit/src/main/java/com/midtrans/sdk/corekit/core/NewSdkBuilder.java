package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;

/**
 * Created by ziahaqi on 7/30/17.
 */

public abstract class NewSdkBuilder<T> {
    public static final String CORE_FLOW = "core";
    public static final String UI_FLOW = "ui";
    public static final String WIDGET = "widget";

    private static final String TAG = NewSdkBuilder.class.getSimpleName();
    protected String clientKey;
    protected Context context;
    protected boolean enableLog = false;
    protected boolean enableBuiltInTokenStorage = true;
    protected String merchantBaseUrl;
    protected TransactionFinishedCallback transactionFinishedCallback;
    protected String flow;



    public abstract T setContext(Context context);

    public abstract T setClientKey(String clientKey);

    public abstract T enableLog(boolean enabled);

    public abstract T setMerchantBaseUrl(String merchantBaseUrl);

    public abstract T setTransactionFinishedCallback(TransactionFinishedCallback callback);

    public abstract T enableBuiltInTokenStorage(boolean enabled);

    public abstract MidtransSDK build();


    public void checkSdkProperties() {
        if (transactionFinishedCallback == null || clientKey == null || context == null) {

            if (context == null) {
                String message = "Context cannot be null. Please pass application context using setContext()";
                RuntimeException runtimeException = new RuntimeException(message);
                Log.e(NewSdkBuilder.CORE_FLOW, message, runtimeException);
                throw runtimeException;
            }

            if (TextUtils.isEmpty(clientKey)) {
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

            Logger.e(TAG, "invalid data supplied to sdk");
        }
    }
}
