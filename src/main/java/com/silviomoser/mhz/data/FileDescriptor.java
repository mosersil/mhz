package com.silviomoser.mhz.data;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class FileDescriptor {
    private String name;
    private long size;
    private Date lastModified;
}
