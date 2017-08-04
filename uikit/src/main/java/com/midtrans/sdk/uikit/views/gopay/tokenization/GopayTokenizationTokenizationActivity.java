package com.midtrans.sdk.uikit.views.gopay.tokenization;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 8/4/17.
 */

public class GopayTokenizationTokenizationActivity extends BasePaymentActivity implements GopayTokenizationView {
    private GopayTokenizationPresenter presenter;
    private TextInputEditText inputCountyCode;
    private TextInputEditText inputPhoneNumber;
    private FancyButton buttonPayNow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gopay_tokenization);
        initProperties();
        bindViews();
        initPaymentButton();
    }

    private void initPaymentButton() {
        buttonPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initProperties() {
        presenter = new GopayTokenizationPresenter(this);
    }


    private void bindViews() {
        inputCountyCode = (TextInputEditText) findViewById(R.id.input_country_code);
        inputCountyCode = (TextInputEditText) findViewById(R.id.input_phone_number);
        buttonPayNow = (FancyButton) findViewById(R.id.btn_continue);
    }

}
