package com.midtrans.sdk.uikit.views.gopay.payment;

import com.midtrans.sdk.corekit.models.TransactionResponse;

/**
 * Created by ziahaqi on 8/7/17.
 */

public interface GopayPaymentView {
    void onPaymentSuccess(TransactionResponse response);
}
