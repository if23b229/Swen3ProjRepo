package com.swen3.paperless.service;

import io.minio.*;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class MinioStorageServiceImpl implements MinioStorageService {

    private final MinioClient minioClient;
    private final String bucketName;

    public MinioStorageServiceImpl(
            MinioClient minioClient,
            @Value("${app.minio.bucket}") String bucketName
    ) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    @Override
    public String uploadPdf(MultipartFile file) {
        try {
            String objectName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            try (InputStream is = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .stream(is, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            }
            return "minio://" + bucketName + "/" + objectName;
        } catch (MinioException e) {
            throw new RuntimeException("MinIO error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload to MinIO", e);
        }
    }

    @Override
    public InputStream download(String bucket, String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to download from MinIO", e);
        }
    }
}