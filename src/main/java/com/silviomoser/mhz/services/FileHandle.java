package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.type.FileType;
import lombok.Builder;
import lombok.Data;

import java.io.File;

@Data
@Builder
public class FileHandle {

    private File file;
    private String name;
    private FileType fileType;
}
