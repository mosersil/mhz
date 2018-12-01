package com.silviomoser.demo.api.images;


import lombok.Data;

import java.util.List;

@Data
public class ImageGalleryDescriptor {
    List<ImageDescriptor> imageDescriptors;
}
