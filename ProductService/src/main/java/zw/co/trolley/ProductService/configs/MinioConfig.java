package zw.co.trolley.ProductService.configs;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        try {
            MinioClient client = MinioClient.builder()
                    .endpoint(url)
                    .credentials(accessKey, secretKey)
                    .build();

            log.info("✅ MinIO Client initialized successfully with endpoint: {}", url);
            return client;
        } catch (Exception e) {
            log.error("❌ Error initializing MinIO Client: {}", e.getMessage());
            throw new RuntimeException("Failed to create MinIO Client", e);
        }
    }
}
