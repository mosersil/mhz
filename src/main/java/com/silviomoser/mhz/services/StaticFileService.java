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
import com.silviomoser.mhz.services.error.ErrorType;
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
import java.util.UUID;

import static com.silviomoser.mhz.utils.StaticFileUtils.addTrailingSlash;
import static com.silviomoser.mhz.utils.StringUtils.isBlank;

@Service("fileService")
@Getter
@Setter
@Slf4j
public class StaticFileService extends AbstractCrudService<StaticFile> {

    @Autowired
    private FileServiceConfiguration fileServiceConfiguration;

    @Autowired
    private StaticFileRepository staticFileRepository;

    @Deprecated
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

    @Deprecated
    public ByteArrayInputStream download(long id) throws CrudServiceException {
        final StaticFile staticFile = get(id);
        return download(staticFile);
    }

    @Deprecated
    public ByteArrayInputStream download(StaticFile staticFile) throws CrudServiceException {
        if (staticFile.getRole() != null) {
            log.debug("file {} requires authorization. Required role: {} ", staticFile.getId(), staticFile.getRole());
            final Person me = SecurityUtils.getMe();
            if (!me.getUser().getRoles().contains(staticFile.getRole())) {
                log.warn("User {} is not permitted to download file {}", me.getUser().getUsername(), staticFile.getId());
                throw new CrudServiceException(ErrorType.NOT_AUTHORIZED);
            }
        }
        else {
            log.debug("Starting download of public resource {}", staticFile);
        }


        final String absolutePath = addTrailingSlash(fileServiceConfiguration.getDirectory()) + staticFile.getLocation();
        final File file = new File(absolutePath);
        if (!file.exists()) {
            log.warn(String.format("File %s does not exist", file));
            throw new CrudServiceException(ErrorType.NOT_FOUND, String.format("File %s does not exist", file));
        }
        try {
            return new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
        } catch (IOException e) {
            log.warn(String.format("Unexpected exception occured when downloading file %s: %s", staticFile, e.getMessage()), e);
            throw new CrudServiceException(ErrorType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Deprecated
    public StaticFile save(FileHandle fileHandle, String title, String description, Role role, String keywords, CalendarEvent event) {

        final StaticFile staticFile = StaticFile.builder()
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


    @Deprecated
    public List<StaticFile> getFiles(String category) {
        if (isBlank(category)) {
            return staticFileRepository.findAll();
        }
        return staticFileRepository.findByStaticFileCategoryOrderByCreatedDesc(StaticFileCategory.valueOf(category));
    }
}
