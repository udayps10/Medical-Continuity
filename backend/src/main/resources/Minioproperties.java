package com.medicalcontinuity.medicalcontinuity.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "minio")
@Getter
@Setter
public class MinioProperties {

    /** e.g. http://localhost:9000 */
    private String endpoint;
    private String accessKey;
    private String secretKey;

    /** Bucket for patient registration + unknown-case photos used in face matching. */
    private String photoBucket = "patient-photos";
}