package com.midtrans.sdk.uikit.views.creditcard.register;

import android.app.Activity;
import android.content.Context;

import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.uikit.abstracts.BasePresenter;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;

import java.util.List;

/**
 * Created by ziahaqi on 8/18/17.
 */

public class CardRegistrationPresenter extends BasePresenter<CardRegistrationView> {

    private final List<BankBinsResponse> bankBins;

    public CardRegistrationPresenter(Context context, CardRegistrationView view) {
        this.bankBins = SdkUIFlowUtil.getBankBins(context);
        this.view = view;
    }

    public void startScan(Activity activity, int intentScanCard) {
        MidtransSDK.getInstance().getExternalScanner().startScan(activity, intentScanCard);
    }

    public void register(String cardNumber, String cvv, String expiryMonth, String expiryYear) {
        MidtransSDK.getInstance().cardRegistration(cardNumber, cvv, expiryMonth, expiryYear, new CardRegistrationCallback() {
            @Override
            public void onSuccess(CardRegistrationResponse response) {
                CardRegistrationCallback callback = MidtransSDK.getInstance().getCardRegistrationCallback();
                if (callback != null) {
                    callback.onSuccess(response);
                }
                view.onRegisterCardSuccess(response);
            }

            @Override
            public void onFailure(CardRegistrationResponse response, String reason) {
                CardRegistrationCallback callback = MidtransSDK.getInstance().getCardRegistrationCallback();
                if (callback != null) {
                    callback.onFailure(response, reason);
                }
                view.onRegisterFailure(response, reason);
            }

            @Override
            public void onError(Throwable error) {
                CardRegistrationCallback callback = MidtransSDK.getInstance().getCardRegistrationCallback();
                if (callback != null) {
                    callback.onError(error);
                }
                view.onRegisterError(error);
            }
        });
    }

    public String getBankByCardBin(String cardBin) {
        if (bankBins != null) {
            for (BankBinsResponse savedBankBin : bankBins) {
                if (savedBankBin.getBins() != null && !savedBankBin.getBins().isEmpty()) {
                    String bankBin = findBankByCardBin(savedBankBin, cardBin);
                    if (bankBin != null) {
                        return bankBin;
                    }
                }
            }
        }
        return null;
    }

    private String findBankByCardBin(BankBinsResponse savedBankBin, String cardBin) {
        for (String savedBin : savedBankBin.getBins()) {
            if (savedBin.contains(cardBin)) {
                return savedBankBin.getBank();
            }
        }
        return null;
    }

    public boolean isMandiriDebitCard(String cleanCardNumber) {
        return false;
    }
}
