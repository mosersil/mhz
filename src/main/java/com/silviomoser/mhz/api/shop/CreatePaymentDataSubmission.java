package com.silviomoser.mhz.api.shop;

import lombok.Data;

@Data
public class CreatePaymentDataSubmission {
    private int transactionId;
    private String cardholder_name;
    private String token;
}
