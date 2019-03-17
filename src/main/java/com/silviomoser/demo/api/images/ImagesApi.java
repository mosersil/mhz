package com.silviomoser.demo.api.images;


import com.silviomoser.demo.api.core.ApiController;
import com.silviomoser.demo.api.core.ApiException;
import com.silviomoser.demo.services.ImageService;
import com.silviomoser.demo.services.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
public class ImagesApi implements ApiController {

    @Autowired
    private ImageService imageService;

    @RequestMapping(value = URL_PUBLIC_IMAGES, method = RequestMethod.GET)
    public @ResponseBody
    List<ImageDescriptor> getImageWithMediaType() {
        try {
            final List<ImageDescriptor> imageDescriptors = imageService.getImages();
            Collections.shuffle(imageDescriptors);
            return imageDescriptors;
        } catch (ServiceException se) {
            throw new ApiException("Unexpected error: " + se.getMessage(), se);
        }
    }


    @RequestMapping(value = URL_PUBLIC_IMAGE)
    public void getImageAsByteArray(@RequestParam(name="name") String name, HttpServletResponse response) throws IOException {
        log.debug("enter getImageAsByteArray: " + name);
        InputStream in = null;
        try {
            in = new ByteArrayInputStream(imageService.getImage(name));
        } catch (ServiceException e) {
            throw new ApiException(e.getMessage(), e);
        }
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }

}
