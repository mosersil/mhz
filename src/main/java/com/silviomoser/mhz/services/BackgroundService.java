package com.silviomoser.mhz.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BackgroundService {

    @Autowired
    private ImageService imageService;

    public byte[] loadRandomBackgroundImage() throws ServiceException {
        List<byte[]> backgroundImages = imageService.getBackgroundImages();
        if (backgroundImages != null) {
            int availableBackgrounds = backgroundImages.size();
            int index = (int) ((Math.random()) * availableBackgrounds);
            return loadBackgroundImage(index);
        }
        log.error("Could not load any background image");
        return null;
    }


    public byte[] loadBackgroundImage(int index) throws ServiceException {
        final List<byte[]> allBackgroundImages = imageService.getBackgroundImages();
        return allBackgroundImages.get(index);
    }

}
