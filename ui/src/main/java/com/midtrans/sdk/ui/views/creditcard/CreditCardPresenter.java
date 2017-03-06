package com.midtrans.sdk.ui.views.creditcard;

import android.content.Context;
import android.support.annotation.NonNull;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.papi.CardTokenRequest;
import com.midtrans.sdk.core.models.papi.CardTokenResponse;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentParams;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePresenter;
import com.midtrans.sdk.ui.models.CreditCardDetails;
import com.midtrans.sdk.ui.models.CreditCardTransaction;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.models.Transaction;
import com.midtrans.sdk.ui.utils.Logger;
import com.midtrans.sdk.ui.utils.UiUtils;

import java.util.List;

/**
 * Created by ziahaqi on 2/26/17.
 */

public class CreditCardPresenter extends BasePresenter implements CreditCardContract.Presenter {

    private Context context;
    private CreditCardContract.CreditCardDetailView cardDetailView;
    private CreditCardContract.SavedCreditCardsView savedCreditCardsView;

    private boolean newCardPayment;
    private int termInstallmentSelected = 0;
    private CardTokenResponse cardTokenResponse;
    private CreditCardPaymentResponse paymentResponse;
    private CreditCardTransaction creditCardTransaction = new CreditCardTransaction();
    public PaymentResult getPaymentResult;


    public CreditCardPresenter(@NonNull Context context) {
        this.context = context;
        this.midtransUiSdk = MidtransUi.getInstance();
        creditCardTransaction.setProperties(midtransUiSdk.getTransaction().getCreditCard(), UiUtils.getBankBins(context));
    }

    public void setCardDetailView(@NonNull CreditCardContract.CreditCardDetailView cardDetailView) {
        this.cardDetailView = cardDetailView;
        this.cardDetailView.setPresenter(this);
    }

    public void setSavedCreditCardsView(@NonNull CreditCardContract.SavedCreditCardsView savedCreditCardsView) {
        this.savedCreditCardsView = savedCreditCardsView;
        this.savedCreditCardsView.setPresenter(this);
    }

    public boolean isNormalMode() {
        return midtransUiSdk.isCreditCardNormalMode();
    }

    public void getSavedCards() {
        if (midtransUiSdk.isBuiltInTokenStorage()) {

        } else {

        }
    }

    @Override
    public void oneClickPayment(@NonNull String maskedCardNumber) {

    }

    @Override
    public void twoClicksPayment(@NonNull CreditCardDetails cardDetailModel, @NonNull String cardCVV) {

    }

    @Override
    public void normalPayment(String cardNumber, String cvv, String month, String year, boolean checked) {
        newCardPayment = true;
        CardTokenRequest cardTokenRequest = CardTokenRequest.newNormalCard(cardNumber, cvv, month, year);
        if (MidtransCore.getInstance().isSecureCreditCardPayment()) {
            cardTokenRequest.setSecure(true);
            cardTokenRequest.setGrossAmount(midtransUiSdk.getTransaction().getGrossAmount());
        }
        initCardTokenRequestCustomValues(cardTokenRequest);
        getCreditCardToken(cardTokenRequest);
    }

    private void getCreditCardToken(CardTokenRequest cardTokenRequest) {
        MidtransCore.getInstance().getCardToken(cardTokenRequest, new MidtransCoreCallback<CardTokenResponse>() {
            @Override
            public void onSuccess(CardTokenResponse response) {
                cardTokenResponse = response;
                cardDetailView.onGetCardTokenSuccess(response);
            }

            @Override
            public void onFailure(CardTokenResponse response) {
                cardTokenResponse = response;
                cardDetailView.onGetCardTokenFailure(response);
            }

            @Override
            public void onError(Throwable throwable) {
                cardDetailView.onGetCardTokenError(throwable.getMessage());
            }
        });
    }


    private void initCardTokenRequestCustomValues(CardTokenRequest cardTokenRequest) {
        Transaction transaction = midtransUiSdk.getTransaction();
        if (transaction != null) {

            //init acquiring
            cardTokenRequest.setChannel(transaction.getAquiringChannel());
            cardTokenRequest.setBank(transaction.getAcquiringBank());

            //init installment
            if (termInstallmentSelected > 0) {
                cardTokenRequest.setInstallment(true);
                cardTokenRequest.setInstallmentTerm(termInstallmentSelected);
            }
        }
    }

    @Override
    public boolean isValidInstallment() {
        return creditCardTransaction.isInstallmentValid();
    }

    @Override
    public boolean isSaveCardChecked() {
        return false;
    }

    @Override
    public boolean isWhiteListBinsAvailable() {
        return creditCardTransaction.isWhiteListBinsAvailable();
    }

    @Override
    public boolean isCardBinValid(String cardBin) {
       return  creditCardTransaction.isInWhiteList(cardBin);
    }

    @Override
    public List<Integer> getInstallmentTerms(String cleanCardNumber) {
        return creditCardTransaction.getInstallmentTerms(cleanCardNumber);
    }

    @Override
    public boolean isBankSupportInstallment() {
        return creditCardTransaction.isBankSupportInstallment();
    }

    @Override
    public int getInstallmentTerm(int installmentCurrentPosition) {
        return creditCardTransaction.getInstallmentTerm(installmentCurrentPosition);
    }

    @Override
    public void setInstallment(int installmentTermPosition) {
        creditCardTransaction.setInstallment(installmentTermPosition);
    }

    @Override
    public String getBankByBin(String cleanCardNumber) {
        return creditCardTransaction.getBankByBin(cleanCardNumber);
    }

    @Override
    public boolean isInstallmentAvailable() {
        return creditCardTransaction.isInstallmentAvailable();
    }

    @Override
    public void setInstallmentAvailableStatus(boolean availableStatus) {
        creditCardTransaction.setInstallmentAvailableStatus(availableStatus);
    }

    @Override
    public boolean isSecureCardpayment() {
        return MidtransCore.getInstance().isSecureCreditCardPayment();
    }

    @Override
    public void payUsingCard() {
        CreditCardPaymentParams cardPaymentParams;
        if (midtransUiSdk.getTransaction().isCreditCardOneClickMode()) {
            cardPaymentParams = CreditCardPaymentParams.newOneClickPaymentParams(cardDetailView.getMaskedCardNumber());
        } else if (midtransUiSdk.getTransaction().isCreditCardTwoClickMode()) {
            cardPaymentParams = CreditCardPaymentParams.newBasicPaymentParams(cardTokenResponse.tokenId);
        } else {
            cardPaymentParams = CreditCardPaymentParams.newBasicPaymentParams(cardTokenResponse.tokenId);
        }
        cardPaymentParams.setSaveCard(cardDetailView.isSaveEnabled());

        // set installment properties
        int installmentTermSelected = creditCardTransaction.getInstallmentTermSelected();
        String installmentBankSeleted = creditCardTransaction.getInstallmentBankSelected();
        Logger.d("presenter", "INSTALLMENT>term:" + installmentTermSelected);
        Logger.d("presenter", "INSTALLMENT>bank:" + installmentBankSeleted);
        if (installmentTermSelected > 0) {
            cardPaymentParams.setInstallment(installmentBankSeleted + "_" + installmentTermSelected);
        }

        MidtransCore.getInstance().paymentUsingCreditCard(midtransUiSdk.readCheckoutToken(),
                cardPaymentParams,
                midtransUiSdk.getTransaction().createSnapCustomerDetails(),
                new MidtransCoreCallback<CreditCardPaymentResponse>() {
                    @Override
                    public void onSuccess(CreditCardPaymentResponse response) {
                        paymentResponse = response;
                        cardDetailView.onCreditCardPaymentSuccess(response);
                    }

                    @Override
                    public void onFailure(CreditCardPaymentResponse response) {
                        paymentResponse = response;
                        cardDetailView.onCreditCardPaymentFailure(response);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        cardDetailView.onCreditCardPaymentError(throwable);
                    }
                }
        );
    }

    @Override
    public boolean isPrimaryDarkColorAvailable() {
        return false;
    }

    @Override
    public int getPrimaryDarkColor() {
        return 0;
    }

    @Override
    public boolean isPrimaryColorAvailable() {
        return false;
    }

    @Override
    public int getPrimaryColor() {
        return 0;
    }


    public void setCardDetailFromScanner(String cardNumber, String cvv, String expired) {
        if (cardDetailView != null) {

        }
    }
}
