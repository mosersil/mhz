package com.silviomoser.demo.config;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FileServiceConfiguration {

    @NotNull
    private String directory;
}
