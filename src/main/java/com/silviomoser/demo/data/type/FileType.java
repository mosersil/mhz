package com.silviomoser.demo.data.type;

public enum FileType {
    PDF("application/pdf", ".pdf", "pdf"),
    DOC("application/msword", ".doc", "word"),
    DOT("application/msword", ".dot", "word"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx", "word"),
    DOTX("application/vnd.openxmlformats-officedocument.wordprocessingml.template", ".dotx", "word"),
    XLS("application/vnd.ms-excel", ".xls", "excel"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx", "excel"),
    MPEG_AUDIO("audio/mpeg", ".mp3", "audio"),
    MP3_AUDIO("audio/mp3", ".mp3", "audio");


    private String mime;
    private String ending;
    private String icon;

    FileType(String mime, String ending, String icon) {
        this.mime = mime;
        this.ending = ending;
        this.icon = icon;
    }

    public static FileType byMimeType(String mime) {
        for (FileType fileType : FileType.values()) {
            if (fileType.getMime().equalsIgnoreCase(mime)) {
                return fileType;
            }
        }
        throw new IllegalArgumentException(String.format("%s is not a supported MIME type type"));
    }

    public String getMime() {
        return mime;
    }

    public String getEnding() {
        return ending;
    }

    public String getIcon() {
        return icon;
    }
}
