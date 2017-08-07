package com.midtrans.sdk.uikit.views.gopay.tokenization;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.gopay.payment.GopayPaymentActivity;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 8/4/17.
 */

public class GopayTokenizationTokenizationActivity extends BasePaymentActivity implements GopayTokenizationView {

    private static final String TAG = GopayTokenizationTokenizationActivity.class.getSimpleName();

    private TextInputEditText inputCountyCode;
    private TextInputEditText inputPhoneNumber;
    private TextInputLayout containerCountryCode;
    private TextInputLayout containerPhoneNumber;
    private FancyButton buttonContinue;

    private GopayTokenizationPresenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gopay_tokenization);
        initProperties();
        bindViews();
        initTheme();
        initContinueButton();
        initInputPhoneNumber();
    }

    private void initInputPhoneNumber() {
        inputCountyCode.setSelection(inputCountyCode.getText().toString().length());
    }

    private void initContinueButton() {
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String countryCode = inputCountyCode.getText().toString().trim();
                String phoneNumber = inputPhoneNumber.getText().toString().trim();

                if (checkNumberValidity(countryCode, phoneNumber)) {
                    startTokenize(countryCode, phoneNumber);
                }
            }
        });
    }

    private boolean checkNumberValidity(String countryCode, String phoneNumber) {
        boolean valid = true;

        if (TextUtils.isEmpty(countryCode)) {
            valid = false;
            containerCountryCode.setError(getString(R.string.message_error_empty_country_code));
        } else {
            containerCountryCode.setError("");
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            valid = false;
            containerPhoneNumber.setError(getString(R.string.message_error_empty_phone_number));
        } else {
            containerPhoneNumber.setError("");
        }

        return valid;
    }


    private void startTokenize(String countryCode, String phoneNumber) {
        presenter.tokenize(countryCode, phoneNumber);
    }

    private void startPaymentPage(String phoneNumber) {

        Intent intent = new Intent(this, GopayPaymentActivity.class);
        intent.putExtra(GopayPaymentActivity.EXTRA_PHONE_NUMBER, phoneNumber);
        startActivityForResult(intent, UiKitConstants.INTENT_CODE_PAYMENT);
    }

    private void initProperties() {
        presenter = new GopayTokenizationPresenter(this);
    }


    private void bindViews() {
        inputCountyCode = (TextInputEditText) findViewById(R.id.input_country_code);
        inputPhoneNumber = (TextInputEditText) findViewById(R.id.input_phone_number);
        containerCountryCode = (TextInputLayout) findViewById(R.id.container_input_country_code);
        containerPhoneNumber = (TextInputLayout) findViewById(R.id.container_input_phone_number);
        buttonContinue = (FancyButton) findViewById(R.id.btn_continue);
    }

    private void initTheme() {
        try {
            setBackgroundTintList(inputCountyCode);
            setBackgroundTintList(inputPhoneNumber);
            setPrimaryBackgroundColor(buttonContinue);

        } catch (RuntimeException e) {
            Logger.d(TAG, "initTheme():" + e.getMessage());
        }
    }

    @Override
    public void onTokenizeSuccess(String phoneNumber) {
        startPaymentPage(phoneNumber);
        // todo gopay onsuccess
    }

    @Override
    public void onTokenizeFailure() {
        // todo gopay onfailure
    }

    @Override
    public void onTokenizeError() {
        // todo gopay onerror

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                finishPayment(data);
            }
        }
    }

    private void finishPayment(Intent data) {
        setResult(RESULT_OK, data);
        finish();
    }
}
