package com.silviomoser.mhz.data.type;

public enum  StaticFileCategory {
    GENERIC("static-generic"),
    PRACTICE("static-practice"),
    INTERNAL_DOC("static-internal"),
    LIBRARY_SHEETS("static-library-sheets"),
    LIBRARY_SAMPLES("static-library-samples");

    private String bucketName;

    StaticFileCategory(String bucketName) {
        this.bucketName=bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
