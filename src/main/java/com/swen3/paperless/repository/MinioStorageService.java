package com.swen3.paperless.repository;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface MinioStorageService {

    String uploadPdf(MultipartFile file);

    InputStream download(String bucket, String objectName);
}