package com.silviomoser.demo.services;

import com.silviomoser.demo.data.type.FileType;
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
