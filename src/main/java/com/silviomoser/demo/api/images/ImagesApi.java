package com.silviomoser.demo.api.images;


import com.silviomoser.demo.api.core.ApiController;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ImagesApi implements ApiController {


    @RequestMapping(value = URL_PUBLIC_IMAGES, method = RequestMethod.GET)
    public @ResponseBody
    ImageGalleryDescriptor getImageWithMediaType(@RequestParam(name = "index", required = false) Integer index) throws IOException {

        ImageDescriptor imageDescriptor = ImageDescriptor.builder().height(300).width(500).resource("https://www.sample-videos.com/img/Sample-jpg-image-500kb.jpg").build();

        ImageGalleryDescriptor imageGalleryDescriptor = new ImageGalleryDescriptor();
        List<ImageDescriptor> list = new ArrayList<>(1);
        list.add(imageDescriptor);
        imageGalleryDescriptor.setImageDescriptors(list);

        return imageGalleryDescriptor;
    }

}
