package com.silviomoser.demo.utils;

import com.silviomoser.demo.data.type.FileType;

import java.io.File;

import static com.silviomoser.demo.utils.StringUtils.isBlank;

public class StaticFileUtils {

    private static final String FORWARD_SLASH = "/";

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
        final StringBuilder fileName = new StringBuilder(targetDirectory)
                .append(FORWARD_SLASH)
                .append(targetFileName)
                .append(FileType.byMimeType(mimeType).getEnding());
        return new File(fileName.toString());
    }

    public static String addTrailingSlash(String input) {
        if (isBlank(input)) {
            return FORWARD_SLASH;
        }
        if (input.endsWith(FORWARD_SLASH)) {
            return input;
        }
        return input+FORWARD_SLASH;
    }
}
