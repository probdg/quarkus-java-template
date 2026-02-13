package com.example.service;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Service for MinIO object storage operations.
 */
@ApplicationScoped
@Slf4j
public class MinioService {

  @ConfigProperty(name = "app.minio.endpoint")
  String endpoint;

  @ConfigProperty(name = "app.minio.access-key")
  String accessKey;

  @ConfigProperty(name = "app.minio.secret-key")
  String secretKey;

  @ConfigProperty(name = "app.minio.bucket-name")
  String bucketName;

  private MinioClient minioClient;

  @PostConstruct
  public void init() {
    try {
      minioClient =
          MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();

      // Create bucket if it doesn't exist
      boolean bucketExists =
          minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
      if (!bucketExists) {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        log.info("Created MinIO bucket: {}", bucketName);
      }
    } catch (Exception e) {
      log.error("Failed to initialize MinIO client", e);
    }
  }

  /**
   * Upload a file to MinIO.
   */
  public void uploadFile(String objectName, byte[] content, String contentType) {
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(content);
      minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName)
          .stream(bais, content.length, -1).contentType(contentType).build());
      log.info("Uploaded file to MinIO: {}", objectName);
    } catch (Exception e) {
      log.error("Failed to upload file to MinIO: {}", objectName, e);
      throw new RuntimeException("Failed to upload file", e);
    }
  }

  /**
   * Download a file from MinIO.
   */
  public InputStream downloadFile(String objectName) {
    try {
      InputStream stream = minioClient
          .getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
      log.info("Downloaded file from MinIO: {}", objectName);
      return stream;
    } catch (Exception e) {
      log.error("Failed to download file from MinIO: {}", objectName, e);
      throw new RuntimeException("Failed to download file", e);
    }
  }

  /**
   * Delete a file from MinIO.
   */
  public void deleteFile(String objectName) {
    try {
      minioClient
          .removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
      log.info("Deleted file from MinIO: {}", objectName);
    } catch (Exception e) {
      log.error("Failed to delete file from MinIO: {}", objectName, e);
      throw new RuntimeException("Failed to delete file", e);
    }
  }
}
