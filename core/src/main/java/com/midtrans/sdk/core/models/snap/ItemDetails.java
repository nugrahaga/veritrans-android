package com.midtrans.sdk.core.models.snap;

/**
 * Created by rakawm on 10/19/16.
 */

public class ItemDetails {
    public final String id;
    public final int price;
    public final int quantity;
    public final String name;

    public ItemDetails(String id, int price, int quantity, String name) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.name = name;
    }
}
