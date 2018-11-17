package com.silviomoser.demo.api.background;


import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class BackgroundApi {

    private static final int NUMBER_AVAILABLE_IMAGES = 3;

    @RequestMapping(value = "/public/api/background", produces = MediaType.IMAGE_JPEG_VALUE, method = RequestMethod.GET)
    public @ResponseBody
    byte[] getImageWithMediaType(@RequestParam(name = "index", required = false) Integer index) throws IOException {

        if (index == null) {
            index = (int) ((Math.random()) * 3 + 1);
        }
        final InputStream in = getClass().getResourceAsStream("/background/" + index + ".jpg");
        return IOUtils.toByteArray(in);
    }

}
