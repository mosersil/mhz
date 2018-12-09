package com.silviomoser.demo.api.shop;

import com.silviomoser.demo.data.ShopItemPurchase;
import com.silviomoser.demo.data.ShopTransaction;
import com.silviomoser.demo.data.type.ShopOrderStatusType;

import javax.validation.constraints.NotNull;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

public class ShopHelper {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###,###,###,###.##");


    public static final int calculateTotal(@NotNull ShopTransaction shopTransaction) {
        return calculateTotal(shopTransaction.getShopItemPurchases());
    }

    public static final int calculateTotal(@NotNull Set<ShopItemPurchase> purchaseList) {
        int total = 0;
        for (ShopItemPurchase purchase : purchaseList) {
            float itemprice = purchase.getAmount() * purchase.getItem().getPrice();
            total += itemprice;
        }
        return total;
    }

    public static final int calculateTotal(@NotNull ShopItemPurchase shopItemPurchase) {
        int total  = (int) (shopItemPurchase.getAmount() * shopItemPurchase.getItem().getPrice());

        return total;
    }

    public static String formatCurrency(long cents, String currency) {

        final float payment = cents / ((float) 100);
        final StringBuilder output = new StringBuilder();
        DECIMAL_FORMAT.setDecimalSeparatorAlwaysShown(true);
        DECIMAL_FORMAT.setMinimumFractionDigits(2);
        output.append(DECIMAL_FORMAT.format(payment));
        if (currency!=null && !currency.isEmpty()) {
            output.append(" ").append(currency);
        }
        return output.toString();
    }

    public static String formatStatus(ShopTransaction shopTransaction) {
        StringBuilder stringBuilder = new StringBuilder();
        switch (shopTransaction.getPayment()) {
            case CREDITCARD:
                stringBuilder.append("Ihre Kreditkarte wurde mit dem Gesamtbetrag belastet. Vielen Dank!");
                break;
            case BILL:
                stringBuilder.append("Zahlung per Rechnung.");
                break;
            case ADVANCE:
                stringBuilder.append("Bitte Gesamtbetrag auf folgendes Konto Überweisen: CH98 0900 0000 8002 1249 2\nKontoinhaber: Musikverein Harmonie Zürich, 8000 Zürich");
                break;
        }
        return stringBuilder.toString();

    }
}
