package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.FileDescriptor;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public abstract class AbstractFileService {

    private static Pattern VALID_NAME = Pattern.compile("[\\sA-Za-z0-9_-]+.jpg");

    @Autowired
    private MinioClient minioClient;



    public List<byte[]> getFiles(String bucket) throws ServiceException {
        List<byte[]> files = null;
        try {
            boolean found = minioClient.bucketExists(bucket);
            if (found) {
                final Iterable<Result<Item>> myObjects = minioClient.listObjects(bucket);
                int numberOfAvailableImages = IterableUtils.size(myObjects);
                files = new ArrayList<>(numberOfAvailableImages);

                for (Result<Item> result : myObjects) {
                    final InputStream inputStream = minioClient.getObject(bucket, result.get().objectName());
                    files.add(IOUtils.toByteArray(inputStream));
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.error("Unexpected error: " + e.getMessage(), e);
                    }
                }
            } else {
                log.error("Bucket {} does not exist", bucket);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
        log.debug("Returning {} background images ", files.size());
        return files;
    }



    public byte[] getFile(String bucket, String name) throws ServiceException {
        Matcher matcher = VALID_NAME.matcher(name);
        if (!matcher.matches()) {
            throw new ServiceException("Invalid resource name");
        }

        byte[] returnBytes;
        try {
            //final MinioClient minioClient = new MinioClient(imageServiceConfiguration.getEndpoint(), imageServiceConfiguration.getAccessKey(),
                    //imageServiceConfiguration.getSecretKey());

            final InputStream inputStream = minioClient.getObject(bucket, name);
            returnBytes = IOUtils.toByteArray(inputStream);
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }

        return returnBytes;
    }


    public List<FileDescriptor> listFileNames(String bucket) throws ServiceException {
        List<FileDescriptor> files = null;
        try {
            if (minioClient.bucketExists(bucket)) {
                final Iterable<Result<Item>> myObjects = minioClient.listObjects(bucket);
                int numberOfAvailableImages = IterableUtils.size(myObjects);
                files = new ArrayList<>(numberOfAvailableImages);

                for (Result<Item> result : myObjects) {

                    final FileDescriptor fileDescriptor = FileDescriptor.builder()
                            .name(result.get().objectName())
                            .size(result.get().objectSize())
                            .lastModified(result.get().lastModified())
                            .build();
                    files.add(fileDescriptor);
                }
            } else {
                log.error("Bucket {} does not exist", bucket);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
        log.debug("Returning {} background images ", files.size());
        return files;
    }

    public void putFile(String bucket, String name, InputStream inputStream, String contentType) throws ServiceException {
        try {
            if (minioClient.bucketExists(bucket)) {
                minioClient.putObject(bucket, name, inputStream, contentType);
                log.debug(String.format("%s uploaded successfully to bucket %s", name, bucket));
            }
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

}
