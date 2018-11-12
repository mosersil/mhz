package com.silviomoser.demo.utils;

import com.silviomoser.demo.data.type.FileType;

import java.io.File;

public class StaticFileUtils {

    public static boolean isSupportedMimeType(String mimeType) {
        if (mimeType == null) {
            throw new IllegalArgumentException("invalid mime type");
        }
        try {
            FileType.byMimeType(mimeType);
            return true;
        } catch (IllegalArgumentException iae) {
            return false;
        }
    }

    public static File assembleTargetFile(String targetDirectory, String targetFileName, String mimeType) {
        if (!isSupportedMimeType(mimeType)) {
            throw new IllegalArgumentException(String.format("'%s' is not a supported mime Type", mimeType));
        }
        StringBuilder fileName = new StringBuilder(targetDirectory)
                .append("/")
                .append(targetFileName)
                .append(FileType.byMimeType(mimeType).getEnding());
        return new File(fileName.toString());
    }
}
