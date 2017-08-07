package com.midtrans.sdk.uikit.views.gopay.tokenization;

/**
 * Created by ziahaqi on 8/4/17.
 */

public interface GopayTokenizationView {

    public void onTokenizeSuccess(String phoneNumber);

    public void onTokenizeFailure();

    public void onTokenizeError();
}
