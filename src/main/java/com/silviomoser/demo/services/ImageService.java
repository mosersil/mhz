package com.silviomoser.demo.services;

import com.silviomoser.demo.api.core.ApiController;
import com.silviomoser.demo.api.images.ImageDescriptor;
import com.silviomoser.demo.config.ImageServiceConfiguration;
import com.silviomoser.demo.data.Image;
import com.silviomoser.demo.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImageService {

    private static Pattern VALID_NAME= Pattern.compile("[A-Za-z0-9_-]+.jpg");

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageServiceConfiguration imageServiceConfiguration;


    public List<ImageDescriptor> getImages() {
        List<Image> images = imageRepository.findAll();
        return images.stream().map(it ->
                ImageDescriptor.builder()
                        .src(imageServiceConfiguration.getBaseUrl()+ApiController.URL_PUBLIC_IMAGE+"?name="+it.getRaw())
                        .thumb(imageServiceConfiguration.getBaseUrl()+ApiController.URL_PUBLIC_IMAGE+"?name="+it.getThumbnail())
                        .build())
                .collect(Collectors.toList());
    }


    public InputStream getImage(String name) throws ServiceException {
        Matcher matcher = VALID_NAME.matcher(name);
        if (!matcher.matches()) {
            throw new ServiceException("Invalid resource name");
        }
        File initialFile = new File(imageServiceConfiguration.getStoragePath()+name);
        InputStream targetStream;
        try {
            targetStream = FileUtils.openInputStream(initialFile);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
        return targetStream;
    }
}
