package com.silviomoser.demo.services;


import com.silviomoser.demo.api.core.ApiController;
import com.silviomoser.demo.api.images.ImageDescriptor;
import com.silviomoser.demo.config.ImageServiceConfiguration;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ImageService {

    private static Pattern VALID_NAME = Pattern.compile("[\\sA-Za-z0-9_-]+.jpg");

    @Autowired
    private ImageServiceConfiguration imageServiceConfiguration;

    @Autowired
    private MinioClient minioClient;

    private static final String BUCKET_IMAGES = "images";
    private static final String BUCKET_BACKGROUND = "background";



    @Cacheable("backgroundImage")
    public List<byte[]> getBackgroundImages() throws ServiceException {
        List<byte[]> images = null;
        try {
            boolean found = minioClient.bucketExists(BUCKET_BACKGROUND);
            if (found) {
                final Iterable<Result<Item>> myObjects = minioClient.listObjects(BUCKET_BACKGROUND);
                int numberOfAvailableImages = IterableUtils.size(myObjects);
                images = new ArrayList<>(numberOfAvailableImages);

                for (Result<Item> result : myObjects) {
                    final InputStream inputStream = minioClient.getObject(BUCKET_BACKGROUND, result.get().objectName());
                    images.add(IOUtils.toByteArray(inputStream));
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        //ignore that
                    }
                }
            } else {
                log.error("Bucket {} does not exist", BUCKET_BACKGROUND);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
        log.debug("Returning {} background images ", images.size());
        return images;
    }

    @Cacheable("imageDescriptors")
    public List<ImageDescriptor> getImages() throws ServiceException {
        final List<ImageDescriptor> images = new ArrayList<>();

        try {

            // Check whether 'my-bucketname' exist or not.
            boolean found = minioClient.bucketExists(BUCKET_IMAGES);
            if (found) {
                // List objects from 'my-bucketname'
                Iterable<Result<Item>> myObjects = minioClient.listObjects(BUCKET_IMAGES);
                for (Result<Item> result : myObjects) {
                    final Item item = result.get();
                    final ImageDescriptor imageDescriptor = ImageDescriptor.builder()
                            .src(imageServiceConfiguration.getBaseUrl() + ApiController.URL_PUBLIC_IMAGE + "?name=" + item.objectName())
                            .thumb(imageServiceConfiguration.getBaseUrl() + ApiController.URL_PUBLIC_IMAGE + "?name=" + item.objectName())
                            .build();
                    images.add(imageDescriptor);
                }
            } else {
                log.error("bucket {} does not exist", BUCKET_IMAGES);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return images;
    }


    @Cacheable("images")
    public byte[] getImage(String name) throws ServiceException {
        Matcher matcher = VALID_NAME.matcher(name);
        if (!matcher.matches()) {
            throw new ServiceException("Invalid resource name");
        }

        byte[] returnBytes;
        try {
            final MinioClient minioClient = new MinioClient(imageServiceConfiguration.getEndpoint(), imageServiceConfiguration.getAccessKey(),
                    imageServiceConfiguration.getSecretKey());

            InputStream inputStream = minioClient.getObject(BUCKET_IMAGES, name);
            returnBytes = IOUtils.toByteArray(inputStream);
            try {
                inputStream.close();
            } catch (IOException e) {
                //ignore
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }

        return returnBytes;
    }


}
