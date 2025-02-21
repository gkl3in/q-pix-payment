package br.com.klein.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public abstract class S3ClientResource {

    @ConfigProperty(name = "bucket.name")
    String bucketName;

    protected PutObjectRequest buildPutRequest(final String uuid, final String mediaType) {
        return
                PutObjectRequest.builder().bucket(bucketName)
                        .key(uuid)
                        .contentType(mediaType)
                        .build();
    }
    protected GetObjectRequest buildGetRequest(final String uuid) {
        return GetObjectRequest.builder().bucket(bucketName)
                .key(uuid).build();
    }
}
