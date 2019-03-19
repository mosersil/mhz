package com.silviomoser.demo.services;

import com.dropbox.core.DbxRequestConfig;
import com.silviomoser.demo.api.core.ApiController;
import com.silviomoser.demo.api.images.ImageDescriptor;
import com.silviomoser.demo.config.ImageServiceConfiguration;
import com.silviomoser.demo.data.Image;
import com.silviomoser.demo.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImageService {

    private static Pattern VALID_NAME = Pattern.compile("[A-Za-z0-9_-]+.jpg");
    private final DbxRequestConfig config = DbxRequestConfig.newBuilder("images-folder").build();

    @Autowired
    private ImageServiceConfiguration imageServiceConfiguration;

    @Autowired
    private ImageRepository imageRepository;

    /*
    public List<ImageDescriptor> getImages2() throws ServiceException {
        final DbxClientV2 client = new DbxClientV2(config, imageServiceConfiguration.getAccessKey());
        final List<ImageDescriptor> images = new ArrayList<>();
        try {

            ListFolderResult result = client.files().listFolder("/images");
            while (true) {
                for (Metadata metadata : result.getEntries()) {
                    images.add(ImageDescriptor.builder()
                            .src(imageServiceConfiguration.getBaseUrl() + ApiController.URL_PUBLIC_IMAGE + "?name=" + metadata.getName())
                            .thumb(imageServiceConfiguration.getBaseUrl() + ApiController.URL_PUBLIC_IMAGE + "?name=" + metadata.getName())
                            .build());
                }

                if (!result.getHasMore()) {
                    break;
                }

                result = client.files().listFolderContinue(result.getCursor());
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return images;
    }


    @Cacheable("images")
    public byte[] getImage2(String name) throws ServiceException {
        final DbxClientV2 client = new DbxClientV2(config, imageServiceConfiguration.getAccessKey());
        final Matcher matcher = VALID_NAME.matcher(name);
        if (!matcher.matches()) {
            throw new ServiceException("Invalid resource name");
        }
        byte[] imageBytes = null;
        try {
            DbxDownloader downloader = client.files().download("/images/" + name);
            imageBytes = IOUtils.toByteArray(downloader.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageBytes;
    }
    */

    @Cacheable("imageDescriptors")
    public List<ImageDescriptor> getImages() throws ServiceException {
        List<Image> images = imageRepository.findAll();
        return images.stream().map(it ->
                ImageDescriptor.builder()
                        .src(imageServiceConfiguration.getBaseUrl()+ApiController.URL_PUBLIC_IMAGE+"?name="+it.getRaw())
                        .thumb(imageServiceConfiguration.getBaseUrl()+ApiController.URL_PUBLIC_IMAGE+"?name="+it.getThumbnail())
                        .build())
                .collect(Collectors.toList());
    }


    @Cacheable("images")
    public byte[] getImage(String name) throws ServiceException {
        Matcher matcher = VALID_NAME.matcher(name);
        if (!matcher.matches()) {
            throw new ServiceException("Invalid resource name");
        }
        File initialFile = new File(imageServiceConfiguration.getStoragePath()+name);
        byte[] returnBytes;
        try {
            returnBytes = IOUtils.toByteArray(FileUtils.openInputStream(initialFile));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
        return returnBytes;
    }


}
