package com.midtrans.sdk.uikit.models;

/**
 * Created by ziahaqi on 8/7/17.
 */

public class MessageInfo {
    public String statusCode;
    public String statusMessage;
    public String detailsMessage;

    public MessageInfo(String statusCode, String statusMessage, String detailsMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.detailsMessage = detailsMessage;
    }


}
