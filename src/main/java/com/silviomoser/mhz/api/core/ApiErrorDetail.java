package com.silviomoser.mhz.api.core;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiErrorDetail {
    String errorContext;
    String errorMessage;
}
