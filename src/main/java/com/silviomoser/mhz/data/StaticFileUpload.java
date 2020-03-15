package com.silviomoser.mhz.data;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class StaticFileUpload {
    private int id;
    private String title;
    private String category;
    private String role;
    private MultipartFile file;
}
