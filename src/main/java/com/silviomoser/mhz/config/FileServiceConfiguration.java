package com.silviomoser.mhz.config;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FileServiceConfiguration {

    @NotNull
    private String directory;
}
