package com.silviomoser.mhz.services;


import com.silviomoser.mhz.api.core.ApiController;
import com.silviomoser.mhz.api.images.ImageDescriptor;
import com.silviomoser.mhz.data.FileDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ImageService extends AbstractFileService {


    private static final String BUCKET_IMAGES = "images";
    private static final String BUCKET_BACKGROUND = "background";


    @Cacheable("backgroundImage")
    public List<byte[]> getBackgroundImages() throws ServiceException {
        return super.getFiles(BUCKET_BACKGROUND);
    }


    @Cacheable("images")
    public byte[] getImage(String name) throws ServiceException {
        return super.getFile(BUCKET_IMAGES, name);
    }

    @Cacheable("imageDescriptors")
    public List<ImageDescriptor> getImages() throws ServiceException {

        final List<FileDescriptor> imageFiles = super.listFileNames(BUCKET_IMAGES);

        final List<ImageDescriptor> images = new ArrayList<>();
        try {
            for (FileDescriptor item : imageFiles) {
                final ImageDescriptor imageDescriptor = ImageDescriptor.builder()
                        .src(ApiController.URL_PUBLIC_IMAGE + "?name=" + item.getName())
                        .thumb(ApiController.URL_PUBLIC_IMAGE + "?name=" + item.getName())
                        .build();
                images.add(imageDescriptor);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return images;
    }

}
