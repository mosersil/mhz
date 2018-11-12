package com.silviomoser.demo.api.shop;

import com.silviomoser.demo.data.ShopItemPurchase;
import com.silviomoser.demo.data.ShopTransaction;

import javax.validation.constraints.NotNull;

public class ShopHelper {

    public static final int calculateTotal(@NotNull ShopTransaction shopTransaction) {
        int total = 0;
        for (ShopItemPurchase it : shopTransaction.getShopItemPurchases()) {
            float itemprice = it.getAmount() * it.getItem().getPrice();
            total += itemprice;
        }
        return total;
    }
}
