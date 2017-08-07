package com.midtrans.sdk.uikit.views.gopay.payment;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

/**
 * Created by ziahaqi on 8/7/17.
 */

public class GopayPaymentPresenter {
    private final GopayPaymentView view;
    private TransactionResponse transactionResponse;

    public GopayPaymentPresenter(GopayPaymentView view) {
        this.view = view;
    }

    public boolean isShowPaymentStatus() {
        return MidtransSDK.getInstance().getUIKitCustomSetting().isShowPaymentStatus();
    }

    public void startGoPayPayment(String verficationCode) {
        // todo gopay payment
        TransactionResponse response = new TransactionResponse(UiKitConstants.STATUS_CODE_200,
                "payment success", "234324", "3131312", "20000", PaymentType.GOPAY, "10.20", "success");
        this.transactionResponse = response;
        view.onPaymentSuccess(response);
    }

    public TransactionResponse getTransactionResponse() {
        return transactionResponse;
    }
}
