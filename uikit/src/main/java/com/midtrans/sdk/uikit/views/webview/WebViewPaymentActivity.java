package com.midtrans.sdk.uikit.views.webview;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.MerchantPreferences;
import com.midtrans.sdk.corekit.models.snap.MerchantData;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;

/**
 * Created by ziahaqi on 8/23/17.
 */

public class WebViewPaymentActivity extends BasePaymentActivity {

    private static final String TAG = WebViewPaymentActivity.class.getSimpleName();

    public static final String EXTRA_PAYMENT_TYPE = "extra.paymentType";
    public static final String EXTRA_PAYMENT_URL = "extra.url";

    public static final String TYPE_CREDIT_CARD = "credit.card";
    public static final String TYPE_BCA_KLIKPAY = "bca.klikpay";
    public static final String TYPE_MANDIRI_ECASH = "mandiri.ecash";
    public static final String TYPE_CIMB_CLICK = "cimb.click";
    public static final String TYPE_EPAY_BRI = "epay.bri";

    private WebView webviewContainer;
    private Toolbar toolbar;
    private DefaultTextView textMerchantName;
    private ImageView imageMerchantLogo;

    private String webUrl;
    private String paymentType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initProperties();
        setContentView(R.layout.activity_webview_payment);
        initWebViewContainer();
        initToolbarBackButton();
        initMerchantLogo();
    }

    private void initProperties() {
        Intent intent = getIntent();
        if (intent != null) {
            webUrl = intent.getStringExtra(EXTRA_PAYMENT_URL);
            paymentType = intent.getStringExtra(EXTRA_PAYMENT_TYPE);
        }
    }

    @SuppressLint("AddJavascriptInterface")
    private void initWebViewContainer() {
        webviewContainer.getSettings().setAllowFileAccess(false);
        webviewContainer.getSettings().setJavaScriptEnabled(true);
        webviewContainer.setInitialScale(1);
        webviewContainer.getSettings().setLoadWithOverviewMode(true);
        webviewContainer.getSettings().setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webviewContainer.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webviewContainer.setWebViewClient(new MidtransWebViewClient(this, paymentType));
        webviewContainer.setWebChromeClient(new WebChromeClient());
        webviewContainer.loadUrl(webUrl);
    }

    @Override
    public void bindViews() {
        webviewContainer = (WebView) findViewById(R.id.webview_container);
        imageMerchantLogo = (ImageView) findViewById(R.id.merchant_logo);
        textMerchantName = (DefaultTextView) findViewById(R.id.text_page_merchant_name);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
    }

    @Override
    public void initTheme() {
        Drawable backIcon = ContextCompat.getDrawable(this, R.drawable.ic_back);
        if (backIcon != null) {
            backIcon.setColorFilter(getPrimaryColor(), PorterDuff.Mode.SRC_ATOP);
        }
        toolbar.setNavigationIcon(backIcon);
    }


    protected void initMerchantLogo() {
        try {
            MerchantData merchantData = MidtransSDK.getInstance().getMerchantData();

            if (merchantData != null) {
                MerchantPreferences preferences = merchantData.getPreference();
                if (preferences != null) {
                    String merchantName = preferences.getDisplayName();
                    String merchantLogoUrl = preferences.getLogoUrl();
                    if (!TextUtils.isEmpty(merchantLogoUrl)) {
                        if (imageMerchantLogo != null) {
                            Glide.with(this)
                                    .load(merchantLogoUrl)
                                    .into(imageMerchantLogo);
                        }
                    } else {
                        if (merchantName != null) {
                            if (textMerchantName != null && !TextUtils.isEmpty(merchantName)) {
                                textMerchantName.setVisibility(View.VISIBLE);
                                textMerchantName.setText(merchantName);
                                if (imageMerchantLogo != null) {
                                    imageMerchantLogo.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
            Logger.e(TAG, "initMerchantLogo:" + e.getMessage());
        }
    }

    protected void initToolbarBackButton() {
        try {
            if (toolbar != null) {
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isActivityRunning()) {
                            showCancelConfirmationDialog(WebViewPaymentActivity.this);
                        }
                    }
                });
            }
        } catch (RuntimeException e) {
            Logger.e(TAG, "initToolbarBackButton:" + e.getMessage());
        }
    }


    private static void showCancelConfirmationDialog(final WebViewPaymentActivity activity) {
        if (activity != null) {
            if (!activity.isFinishing()) {
                try {
                    AlertDialog dialog = new AlertDialog.Builder(activity, R.style.AlertDialogCustom)
                            .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (activity != null && !activity.isFinishing()) {
                                        dialog.dismiss();
                                        finishWebViewPayment(activity, RESULT_CANCELED);
                                    }
                                }
                            })
                            .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!activity.isFinishing()) {
                                        dialog.dismiss();
                                    }
                                }
                            })
                            .setTitle(R.string.cancel_transaction)
                            .setMessage(R.string.cancel_transaction_message)
                            .create();
                    dialog.show();
                } catch (Exception e) {
                    Logger.e(TAG, "showDialog:" + e.getMessage());
                }
            } else {
                activity.finish();
            }
        }
    }

    private static class MidtransWebViewClient extends WebViewClient {

        private final String paymentType;
        private WebViewPaymentActivity activity;

        private MidtransWebViewClient(WebViewPaymentActivity activity, String type) {
            this.paymentType = type;
            this.activity = activity;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading()>url:" + url);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "onPageFinished()>url:" + url);
            if (activity != null && activity.isActivityRunning()) {
                if (url.contains(UiKitConstants.CALLBACK_PATTERN_3DS) || url.contains(UiKitConstants.CALLBACK_PATTERN_RBA)) {
                    finishWebViewPayment(activity, RESULT_OK);
                }
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "onPageStarted()>url:" + url);
            super.onPageStarted(view, url, favicon);

            if (activity != null && activity.isActivityRunning()) {
                if (!TextUtils.isEmpty(paymentType) && paymentType.equals(TYPE_BCA_KLIKPAY)) {
                    if (url.contains("?id=")) {
                        finishWebViewPayment(activity, RESULT_OK);
                    }
                } else if (!TextUtils.isEmpty(paymentType) && paymentType.equals(TYPE_MANDIRI_ECASH)) {
                    if (url.contains("notify?id=")) {
                        finishWebViewPayment(activity, RESULT_OK);
                    }
                } else if (!TextUtils.isEmpty(paymentType) && paymentType.equals(TYPE_EPAY_BRI)) {
                    if (url.contains("briPayment?tid=")) {
                        finishWebViewPayment(activity, RESULT_OK);
                    }
                } else if (!TextUtils.isEmpty(paymentType) && paymentType.equals(TYPE_CIMB_CLICK)) {
                    if (url.contains("cimb-clicks/response")) {
                        finishWebViewPayment(activity, RESULT_OK);
                    }
                }
            }
        }


    }

    private static void finishWebViewPayment(WebViewPaymentActivity activity, int resultCode) {
        Intent returnIntent = new Intent();
        activity.setResult(resultCode, returnIntent);
        activity.finish();
        activity.overrideBackAnimation();
    }

    @Override
    public void onBackPressed() {
        showCancelConfirmationDialog(this);
    }
}
