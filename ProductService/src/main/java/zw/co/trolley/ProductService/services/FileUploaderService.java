package zw.co.trolley.ProductService.services;

import io.minio.*;
import io.minio.http.Method;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class FileUploaderService {

    private final MinioClient minioClient;

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.presigned.duration:3600}") // Default 1 hour
    private long presignedUrlDuration;

    public FileUploaderService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * Uploads a file to MinIO storage and makes the bucket publicly accessible.
     *
     * @param bucketName Name of the bucket
     * @param objectName Name to be used for the uploaded object
     * @param filePath   Path to the file to be uploaded
     * @return The ETag of the uploaded object
     * @throws RuntimeException if upload fails
     */
    public String uploadFile(String bucketName, String objectName, String filePath) {
        validateInputs(bucketName, objectName, filePath);

        try {
            ensureBucketExists(bucketName);
            setPublicBucketPolicy(bucketName);

            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .filename(filePath)
                            .build());

            log.info("File '{}' uploaded successfully to bucket '{}'", objectName, bucketName);
            return objectName;

        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Error uploading file to MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }

    /**
     * Gets a presigned URL for temporary access to a file.
     *
     * @param bucketName Name of the bucket
     * @param objectName Name of the object
     * @return Presigned URL for the object
     * @throws RuntimeException if URL generation fails
     */
    public String getFileUrl(String bucketName, String objectName) {
        validateInputs(bucketName, objectName);

        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)  // Explicitly specify GET method
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry((int) presignedUrlDuration, TimeUnit.SECONDS)
                            .build());
        } catch (Exception e) {
            log.error("Error generating presigned URL: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate presigned URL: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a file from MinIO storage.
     *
     * @param bucketName Name of the bucket
     * @param objectName Name of the object to delete
     * @throws RuntimeException if deletion fails
     */
    public void deleteFile(String bucketName, String objectName) {
        validateInputs(bucketName, objectName);

        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
            log.info("File '{}' deleted successfully from bucket '{}'", objectName, bucketName);
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Error deleting file from MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("File deletion failed: " + e.getMessage(), e);
        }
    }

    private void validateInputs(String... inputs) {
        for (String input : inputs) {
            if (!StringUtils.hasText(input)) {
                throw new IllegalArgumentException("Input parameters cannot be null or empty");
            }
        }
    }

    private void ensureBucketExists(String bucketName) throws MinioException, IOException,
            NoSuchAlgorithmException, InvalidKeyException {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            log.info("Bucket '{}' created successfully", bucketName);
        }
    }

    private void setPublicBucketPolicy(String bucketName) throws MinioException, IOException,
            NoSuchAlgorithmException, InvalidKeyException {
        String policyJson = String.format("""
                {
                    "Version": "2012-10-17",
                    "Statement": [
                        {
                            "Effect": "Allow",
                            "Principal": "*",
                            "Action": "s3:GetObject",
                            "Resource": "arn:aws:s3:::%s/*"
                        }
                    ]
                }""", bucketName);

        minioClient.setBucketPolicy(
                SetBucketPolicyArgs.builder()
                        .bucket(bucketName)
                        .config(policyJson)
                        .build());
    }
}