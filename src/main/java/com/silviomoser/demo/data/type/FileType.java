package com.silviomoser.demo.data.type;

public enum FileType {
    PDF("application/pdf", ".pdf"),
    DOC("application/msword", ".doc"),
    DOT("application/msword", ".dot"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx"),
    DOTX("application/vnd.openxmlformats-officedocument.wordprocessingml.template", ".dotx"),
    XLS("application/vnd.ms-excel", ".xls"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx"),
    MP3("audio/mpeg", "mp3");

    private String mime;
    private String ending;

    FileType(String mime, String ending) {
        this.mime = mime;
        this.ending = ending;
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
}
