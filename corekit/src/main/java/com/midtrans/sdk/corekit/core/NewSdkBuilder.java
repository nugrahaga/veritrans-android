package com.midtrans.sdk.corekit.core;

import android.content.Context;

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


    public static CoreKitBuilder builder() {
        return new CoreKitBuilder();
    }

    public abstract T setContext(Context context);

    public abstract T setClientKey(String clientKey);

    public abstract T enableLog(boolean enabled);

    public abstract T setMerchantBaseUrl(String merchantBaseUrl);

    public abstract T setTransactionFinishedCallback(TransactionFinishedCallback callback);

    public abstract MidtransSDK build();

}
