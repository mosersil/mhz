package com.silviomoser.mhz.api.library;

import com.silviomoser.mhz.api.core.ApiException;
import com.silviomoser.mhz.services.FileBucketService;
import com.silviomoser.mhz.services.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class SampleDownload {

    public static final String CONTEXTROOT = "/api/sampledownload";

    @Autowired
    private FileBucketService fileBucketService;

    @RequestMapping(method = GET, path = CONTEXTROOT+"/{location}")
    @ResponseBody
    public void stream(HttpServletResponse response, @PathVariable String location) {
        try {
            OutputStream out = response.getOutputStream();
            InputStream is = fileBucketService.getFileStream("samples", location);
            byte[] buffer = new byte[1024];
            while (true) {
                int bytesRead = is.read(buffer);
                if (bytesRead == -1)
                    break;
                out.write(buffer, 0, bytesRead);
            }

        } catch (ServiceException | IOException e) {
            throw new ApiException(e.getMessage());
        }
    }
}
