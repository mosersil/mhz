package com.silviomoser.mhz.data;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ShopOrderSubmission {

    private int count;
    private long shopItemId;

}
