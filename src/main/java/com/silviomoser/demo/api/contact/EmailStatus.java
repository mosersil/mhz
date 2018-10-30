package com.silviomoser.demo.api.contact;

import lombok.Data;

/**
 * Created by silvio on 20.08.18.
 */
@Data
public class EmailStatus {
    private boolean success;
    private String errorDetails;
}
