package com.silviomoser.demo.api.shop;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ShopHelperTest {

    @DataProvider(name = "testFormatCurrencyDP")
    public Object[][] testFormatCurrencyDP() {
        return new Object[][] {
                {1000l, "CHF", "10.00 CHF"},
                {1055l, "CHF", "10.55 CHF"},
                {10055l, "CHF", "100.55 CHF"},
                {100055l, "CHF", "1'000.55 CHF"},
                {0l, "CHF", "0.00 CHF"},
                {1000l, null, "10.00"}
        };
    }

    @Test(dataProvider = "testFormatCurrencyDP")
    public void testFormatCurrency(long input, String currency, String expected) {
        assertThat(ShopHelper.formatCurrency(input, currency)).isEqualTo(expected);
    }
}