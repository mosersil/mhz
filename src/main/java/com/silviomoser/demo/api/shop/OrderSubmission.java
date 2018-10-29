package com.silviomoser.demo.api.shop;


import lombok.Data;

@Data
public class OrderSubmission {



    private int count;
    private long shopItemId;


    @Override
    public String toString() {
        return "OrderSubmission{" +
                "count=" + count +
                ", shopItemId=" + shopItemId +
                '}';
    }
}
