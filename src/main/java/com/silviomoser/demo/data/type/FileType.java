package com.silviomoser.demo.data.type;

public enum FileType {
    PDF("application/pdf"),
    DOC("application/msword"),
    XLS("application/vnd.ms-excel"),
    MP3("audio/mpeg");

    private String mime;

    FileType(String mime) {
        this.mime = mime;
    }

    public static FileType byMimeType(String mime) {
        for (FileType fileType : FileType.values()) {
            if (fileType.getMime().equalsIgnoreCase(mime)) {
                return fileType;
            }
        }
        throw new IllegalArgumentException(String.format("%s is not a supported MIME type type"));
    }

    String getMime() {
        return mime;
    }
}
