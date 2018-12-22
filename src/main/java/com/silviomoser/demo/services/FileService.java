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
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service("fileService")
@Getter
@Setter
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
        Optional<StaticFile> optionalStaticFile = staticFileRepository.findById(id);
        return optionalStaticFile.isPresent() ? optionalStaticFile.get() : null;
    }


    public ByteArrayInputStream download(StaticFile staticFile) throws IOException {
        final File file = new File(fileServiceConfiguration.getDirectory() + "/" + staticFile.getLocation());
        return new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
    }

    public StaticFile save(FileHandle fileHandle, String title, String description, Role role, String keywords, CalendarEvent event) {

        StaticFile staticFile = StaticFile.builder()
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
