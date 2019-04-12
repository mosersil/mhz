package com.silviomoser.demo.services;


import com.silviomoser.demo.api.core.ApiController;
import com.silviomoser.demo.api.images.ImageDescriptor;
import com.silviomoser.demo.config.ImageServiceConfiguration;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Scope(WebApplicationContext.SCOPE_APPLICATION)
@Slf4j
public class ImageService {

    private static Pattern VALID_NAME = Pattern.compile("[\\sA-Za-z0-9_-]+.jpg");


    @Autowired
    private ImageServiceConfiguration imageServiceConfiguration;

    private static final String BUCKET_IMAGES = "images";

    @Cacheable("backgroundImage")
    public byte[] getBackgroundImage(String index) throws ServiceException {
        final InputStream in = getClass().getResourceAsStream("/background/" + index + ".jpg");
        byte[] background = null;
        try {
            background = IOUtils.toByteArray(in);
        } catch (IOException ioe) {
            throw new ServiceException(ioe.getMessage(), ioe);
        }
        return background;
    }

    @Cacheable("imageDescriptors")
    public List<ImageDescriptor> getImages() throws ServiceException {
        final List<ImageDescriptor> images = new ArrayList<>();

        try {
            /* play.minio.io for test and development. */
            final MinioClient minioClient = new MinioClient(imageServiceConfiguration.getEndpoint(), imageServiceConfiguration.getAccessKey(), imageServiceConfiguration.getSecretKey());


            // Check whether 'my-bucketname' exist or not.
            boolean found = minioClient.bucketExists(BUCKET_IMAGES);
            if (found) {
                // List objects from 'my-bucketname'
                Iterable<Result<Item>> myObjects = minioClient.listObjects(BUCKET_IMAGES);
                for (Result<Item> result : myObjects) {
                    Item item = result.get();
                    System.out.println(item.lastModified() + ", " + item.size() + ", " + item.objectName());
                    ImageDescriptor imageDescriptor = ImageDescriptor.builder()
                            .src(imageServiceConfiguration.getBaseUrl() + ApiController.URL_PUBLIC_IMAGE + "?name=" + item.objectName())
                            .thumb(imageServiceConfiguration.getBaseUrl() + ApiController.URL_PUBLIC_IMAGE + "?name=" + item.objectName())
                            .build();
                    images.add(imageDescriptor);
                }
            } else {
                System.out.println("testbucket does not exist");
            }
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }

        return returnBytes;
    }


}
