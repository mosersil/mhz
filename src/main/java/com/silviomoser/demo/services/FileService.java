package com.silviomoser.demo.services;


import com.silviomoser.demo.config.FileServiceConfiguration;
import com.silviomoser.demo.data.CalendarEvent;
import com.silviomoser.demo.data.Role;
import com.silviomoser.demo.data.StaticFile;
import com.silviomoser.demo.data.type.FileType;
import com.silviomoser.demo.repository.StaticFileRepository;
import com.silviomoser.demo.security.utils.SecurityUtils;
import com.silviomoser.demo.utils.StaticFileUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static com.silviomoser.demo.utils.StaticFileUtils.addTrailingSlash;

@Service("fileService")
@Getter
@Setter
@Slf4j
public class FileService {

    @Autowired
    private FileServiceConfiguration fileServiceConfiguration;

    @Autowired
    private StaticFileRepository staticFileRepository;

    public FileHandle create(String mimeType) {
        final UUID uuid = UUID.randomUUID();
        final File file = StaticFileUtils.assembleTargetFile(fileServiceConfiguration.getDirectory(), uuid.toString(), mimeType);
        final FileType fileType = FileType.byMimeType(mimeType);
        return FileHandle.builder()
                .file(file)
                .fileType(fileType)
                .name(uuid.toString() + fileType.getEnding())
                .build();
    }

    public StaticFile findById(long id) {
        final Optional<StaticFile> optionalStaticFile = staticFileRepository.findById(id);
        return optionalStaticFile.isPresent() ? optionalStaticFile.get() : null;
    }


    public ByteArrayInputStream download(StaticFile staticFile) throws ServiceException {
        final String absolutePath = addTrailingSlash(fileServiceConfiguration.getDirectory()) + staticFile.getLocation();
        final File file = new File(absolutePath);
        if (!file.exists()) {
            log.warn(String.format("File %s does not exist", file));
            throw new ServiceException(String.format("File %s does not exist", file));
        }
        try {
            return new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
        } catch (IOException e) {
            log.warn(String.format("Unexpected exception occured when downloading file %s: %s", staticFile, e.getMessage()), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public StaticFile save(FileHandle fileHandle, String title, String description, Role role, String keywords, CalendarEvent event) {

        final StaticFile staticFile = StaticFile.builder()
                .person(SecurityUtils.getMe())
                .fileType(fileHandle.getFileType())
                .title(title)
                .description(description)
                .role(role)
                .keywords(keywords)
                .location(fileHandle.getName())
                .event(event)
                .build();

        staticFileRepository.save(staticFile);

        return staticFile;
    }


}
