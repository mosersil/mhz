package com.silviomoser.demo.api.shop;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderSubmission {

    private int count;
    private long shopItemId;

}
