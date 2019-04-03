package com.silviomoser.demo.config;

import lombok.Data;

@Data
public class ImageServiceConfiguration {

    private String baseUrl;
    private String storagePath;
    private String accessKey;
    private String secretKey;
    private String endpoint;
}
