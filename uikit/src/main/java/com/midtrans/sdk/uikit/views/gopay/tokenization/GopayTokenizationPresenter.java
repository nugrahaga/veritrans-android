package com.midtrans.sdk.uikit.views.gopay.tokenization;

/**
 * Created by ziahaqi on 8/4/17.
 */

public class GopayTokenizationPresenter {
    private final GopayTokenizationView view;

    public GopayTokenizationPresenter(GopayTokenizationView view) {
        this.view =view;
    }

    public void tokenize(String countryCode, String phoneNumber) {
        // todo gopay tokenize
        view.onTokenizeSuccess(phoneNumber);
    }
}
