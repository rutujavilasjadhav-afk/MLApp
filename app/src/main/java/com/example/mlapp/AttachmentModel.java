package com.example.mlapp;

import android.net.Uri;

public class AttachmentModel {

    public enum AttachmentType {
        CAMERA,
        GALLERY,
        PDF,
        DOCUMENT,
        FILE
    }

    private final AttachmentType type;
    private final Uri uri;
    private final String fileName;
    private final String fileSize;

    public AttachmentModel(AttachmentType type, Uri uri, String fileName, String fileSize) {
        this.type = type;
        this.uri = uri;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public AttachmentType getType() {
        return type;
    }

    public Uri getUri() {
        return uri;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize;
    }
}