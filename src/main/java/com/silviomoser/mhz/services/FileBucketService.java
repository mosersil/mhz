package com.silviomoser.mhz.services;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class FileBucketService {
    @Autowired
    private MinioClient minioClient;

    public byte[] getFile(String bucket, String name) throws ServiceException {
        byte[] returnBytes;
        try {
            InputStream inputStream = minioClient.getObject(bucket, name);
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


    public InputStream getFileStream(String bucket, String name) throws ServiceException {
        InputStream inputStream = null;
        try {
            inputStream = minioClient.getObject(bucket, name);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }

        return inputStream;
    }

}
