package com.medicalcontinuity.medicalcontinuity.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MinioConfig {

    private final MinioProperties minioProperties;

    private MinioClient minioClient;

    @Bean
    public MinioClient minioClient() {
        this.minioClient = MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
        return this.minioClient;
    }

    /**
     * Ensures the photo bucket exists on app startup so registration
     * and emergency-case uploads don't fail on a missing bucket.
     */
    @PostConstruct
    public void ensurePhotoBucketExists() throws Exception {
        MinioClient client = minioClient();
        boolean exists = client.bucketExists(
                BucketExistsArgs.builder().bucket(minioProperties.getPhotoBucket()).build());
        if (!exists) {
            client.makeBucket(
                    MakeBucketArgs.builder().bucket(minioProperties.getPhotoBucket()).build());
        }
    }
}