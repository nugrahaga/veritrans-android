package com.midtrans.sdk.uikit.views.gopay.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.status.PaymentStatusActivity;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 8/7/17.
 */

public class GopayPaymentActivity extends BasePaymentActivity implements GopayPaymentView {

    private static final String TAG = GopayPaymentActivity.class.getSimpleName();
    public static final String EXTRA_PHONE_NUMBER = "extra.number";

    private TextInputLayout containerVerificationCode;
    private TextInputEditText inputVerificationCode;
    private FancyButton buttonContinue;
    private DefaultTextView textNotificationInfo;

    private GopayPaymentPresenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gopay_payment);
        bindView();
        initProperties();
        initTheme();
        initButtonContinue();
    }

    private void initProperties() {
        presenter = new GopayPaymentPresenter(this);
        String phoneNumber = getIntent().getStringExtra(EXTRA_PHONE_NUMBER);
        if (!TextUtils.isEmpty(phoneNumber)) {
            textNotificationInfo.setText(getString(R.string.info_gopay_payment, phoneNumber));
        }
    }

    private void bindView() {
        buttonContinue = (FancyButton) findViewById(R.id.btn_continue);
        inputVerificationCode = (TextInputEditText) findViewById(R.id.input_verification_code);
        containerVerificationCode = (TextInputLayout) findViewById(R.id.container_input_verification_code);
        textNotificationInfo = (DefaultTextView) findViewById(R.id.text_gopay_payment_info);
    }

    private void initTheme() {
        try {
            setBackgroundTintList(inputVerificationCode);
            setPrimaryBackgroundColor(buttonContinue);
        } catch (RuntimeException e) {
            Logger.d(TAG, "initTheme():" + e.getMessage());
        }
    }

    private void initButtonContinue() {
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String verficationCode = inputVerificationCode.getText().toString().trim();
                if (isVerificationCodeValid(verficationCode)) {
                    presenter.startGoPayPayment(verficationCode);
                }
            }
        });
    }

    private boolean isVerificationCodeValid(String verficationCode) {
        boolean valid = true;

        if (TextUtils.isEmpty(verficationCode)) {
            valid = false;
            containerVerificationCode.setError(getString(R.string.error_message_empty_verification_code));
        } else {
            containerVerificationCode.setError("");
        }

        return valid;
    }


    private void initPaymentStatus(TransactionResponse response) {
        if (presenter.isShowPaymentStatus()) {
            showStatusPage(response);
        } else {
            finishPayment();
        }
    }

    private void showStatusPage(TransactionResponse response) {
        Intent intent = new Intent(this, PaymentStatusActivity.class);
        intent.putExtra(PaymentStatusActivity.EXTRA_PAYMENT_RESULT, response);
        startActivityForResult(intent, UiKitConstants.INTENT_PAYMENT_STATUS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UiKitConstants.INTENT_PAYMENT_STATUS) {
            finishPayment();
        }
    }

    private void finishPayment() {
        Intent data = new Intent();
        data.putExtra(getString(R.string.transaction_response), presenter.getTransactionResponse());
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onPaymentSuccess(TransactionResponse response) {
        initPaymentStatus(response);
    }
}
