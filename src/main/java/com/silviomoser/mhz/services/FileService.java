package com.silviomoser.mhz.services;


import com.silviomoser.mhz.config.FileServiceConfiguration;
import com.silviomoser.mhz.data.CalendarEvent;
import com.silviomoser.mhz.data.Person;
import com.silviomoser.mhz.data.Role;
import com.silviomoser.mhz.data.StaticFile;
import com.silviomoser.mhz.data.type.FileType;
import com.silviomoser.mhz.data.type.StaticFileCategory;
import com.silviomoser.mhz.repository.StaticFileRepository;
import com.silviomoser.mhz.security.utils.SecurityUtils;
import com.silviomoser.mhz.utils.StaticFileUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.silviomoser.mhz.utils.StaticFileUtils.addTrailingSlash;
import static com.silviomoser.mhz.utils.StringUtils.isBlank;

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

    public ByteArrayInputStream download(long id) throws ServiceException {
        final StaticFile staticFile = findById(id);
        if (staticFile == null) {
            log.warn(String.format("File with id %s does not exist", id));
            throw new ServiceException(String.format("File %s does not exist", id));
        }
        return download(staticFile);
    }

    public ByteArrayInputStream download(StaticFile staticFile) throws ServiceException {
        if (staticFile.getRole() != null) {
            log.debug("file {} requires authorization. Required role: {} ", staticFile.getId(), staticFile.getRole());
            final Person me = SecurityUtils.getMe();
            if (!me.getUser().getRoles().contains(staticFile.getRole())) {
                log.warn("User {} is not permitted to download file {}", me.getUser().getUsername(), staticFile.getId());
                throw new ServiceException("Not authorized");
            }
        }
        else {
            log.debug("Starting download of public resource {}", staticFile);
        }


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
                .staticFileCategory(StaticFileCategory.GENERIC)
                .description(description)
                .role(role)
                .keywords(keywords)
                .location(fileHandle.getName())
                .event(event)
                .created(LocalDateTime.now())
                .build();

        staticFileRepository.save(staticFile);

        return staticFile;
    }


    public List<StaticFile> getFiles(String category) {
        if (isBlank(category)) {
            return staticFileRepository.findAll();
        }
        return staticFileRepository.findByStaticFileCategoryOrderByCreatedDesc(StaticFileCategory.valueOf(category));
    }
}
