package com.silviomoser.demo.api.images;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageDescriptor {
    int height;
    int width;
    String resource;
}
