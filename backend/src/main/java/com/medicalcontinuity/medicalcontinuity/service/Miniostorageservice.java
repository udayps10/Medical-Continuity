package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.config.MinioProperties;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;

/**
 * Stores registration photos (Patient) and emergency-capture photos
 * (UnknownPatientCase) in MinIO. The returned object key is what
 * gets saved in Patient.photoRef / UnknownPatientCase.photoRef, and
 * is what the face-matching service later fetches for comparison.
 */
@Service
@RequiredArgsConstructor
public class MinioStorageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    /**
     * Uploads a photo and returns the object key to store as photoRef.
     * prefix example: "patients" or "unknown-cases"
     */
    public String uploadPhoto(MultipartFile file, String prefix) {
        try {
            String extension = extractExtension(file.getOriginalFilename());
            String objectKey = prefix + "/" + UUID.randomUUID() + extension;

            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(minioProperties.getPhotoBucket())
                                .object(objectKey)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build());
            }
            return objectKey;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload photo to MinIO: " + e.getMessage(), e);
        }
    }

    /**
     * Returns a temporary signed URL so the frontend or the AI
     * face-matching service can fetch the photo without direct
     * bucket access.
     */
    public String getPhotoUrl(String objectKey) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minioProperties.getPhotoBucket())
                            .object(objectKey)
                            .expiry((int) Duration.ofMinutes(15).getSeconds())
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate photo URL: " + e.getMessage(), e);
        }
    }

    public void deletePhoto(String objectKey) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperties.getPhotoBucket())
                            .object(objectKey)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete photo from MinIO: " + e.getMessage(), e);
        }
    }

    private String extractExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "";
        }
        return originalFilename.substring(originalFilename.lastIndexOf('.'));
    }
}