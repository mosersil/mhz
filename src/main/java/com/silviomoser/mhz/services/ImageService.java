package com.silviomoser.mhz.services;


import com.silviomoser.mhz.data.FileDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ImageService {

    @Autowired
    AbstractFileService abstractFileService;


    private static final String BUCKET_IMAGES = "images";
    private static final String BUCKET_BACKGROUND = "background";


    @Cacheable("backgroundImage")
    public List<byte[]> getBackgroundImages() throws ServiceException {
        return abstractFileService.getFiles(BUCKET_BACKGROUND);
    }

    @Cacheable("images")
    public byte[] getImage(String name) throws ServiceException {
        return abstractFileService.getFile(BUCKET_IMAGES, name);
    }

    @Cacheable("imageDescriptors")
    public List<FileDescriptor> getImages() throws ServiceException {
        return abstractFileService.listFileNames(BUCKET_IMAGES);
    }

}
