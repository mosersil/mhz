package com.silviomoser.demo.api.background;


import com.silviomoser.demo.services.BackgroundService;
import com.silviomoser.demo.services.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BackgroundApi {

    @Autowired
    BackgroundService imageService;

    @RequestMapping(value = "/public/api/background", produces = MediaType.IMAGE_JPEG_VALUE, method = RequestMethod.GET)
    public @ResponseBody
    byte[] getImageWithMediaType() throws ServiceException {
        return imageService.loadRandomBackgroundImage();
    }

}
