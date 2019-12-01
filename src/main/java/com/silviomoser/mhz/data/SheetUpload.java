package com.silviomoser.mhz.data;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class SheetUpload {
    private int id;
    private String title;
    private MultipartFile file;
}
