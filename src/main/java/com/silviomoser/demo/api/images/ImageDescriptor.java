package com.silviomoser.demo.api.images;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageDescriptor {
    String src;
    String thumb;
}
