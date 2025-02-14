package zw.co.trolley.ProductService.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import zw.co.trolley.ProductService.domain.dtos.ProductDto;
import zw.co.trolley.ProductService.domain.models.Product;
import zw.co.trolley.ProductService.domain.repositories.ProductRepository;
import zw.co.trolley.ProductService.exceptions.ProductNotFoundException;

import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    @Value("${minio.bucket.products}")
    private String bucketName;
    private final ProductRepository productRepository;
    private final FileUploaderService fileUploaderService; // Injecting our MinIO-based uploader

    public ProductDto getProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
        return mapToDto(product);
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "fallback")
    public Page<ProductDto> getAllProducts(String search, UUID categoryId, Pageable pageable) {
        Specification<Product> spec = Specification.where(null);
        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"));
        }
        if (categoryId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("category").get("id"), categoryId));
        }
        return productRepository.findAll(spec, pageable).map(this::mapToDto);
    }

    // Create product without file upload (kept for backward compatibility)
    public ProductDto createProduct(ProductDto productDto) {
        Product product = mapToEntity(productDto);
        Product savedProduct = productRepository.save(product);
        return mapToDto(savedProduct);
    }

    // Create product with file upload using a file path.
    public ProductDto createProduct(ProductDto productDto, String filePath) {
        String imageUrl = uploadFile(filePath,bucketName);
        Product product = mapToEntity(productDto);
        product.setImageUrl(imageUrl);
        Product savedProduct = productRepository.save(product);
        return mapToDto(savedProduct);
    }

    // Update product without file upload (kept for backward compatibility)
    public ProductDto updateProduct(UUID id, ProductDto productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
        updateEntity(product, productDto);
        Product updatedProduct = productRepository.save(product);
        return mapToDto(updatedProduct);
    }

    // Update product with file upload using a file path.
    public ProductDto updateProduct(UUID id, ProductDto productDto, String filePath) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));

        if (filePath != null && !filePath.trim().isEmpty()) {
            String imageUrl = uploadFile(filePath,bucketName);
            product.setImageUrl(imageUrl);
        }
        updateEntity(product, productDto);
        Product updatedProduct = productRepository.save(product);
        return mapToDto(updatedProduct);
    }

    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
        fileUploaderService.deleteFile(bucketName,product.getImageUrl());
        productRepository.delete(product);
    }

    // Fallback method for the CircuitBreaker on getAllProducts.
    public Page<ProductDto> fallback(String search, UUID categoryId, Pageable pageable, Exception e) {
        log.warn("Circuit breaker triggered for getAllProducts: {}", e.getMessage());
        return Page.empty(pageable);
    }

    // --- Helper Methods ---

    private String uploadFile(String filePath,String bucketName) {
        try {
            // Extract the original file name from the file path.
            String originalFileName = Paths.get(filePath).getFileName().toString();
            // Generate a unique object name to avoid collisions.
            String objectName = UUID.randomUUID().toString() + "_" + originalFileName;
            // Assuming "product-images" is the bucket name.
            return fileUploaderService.uploadFile(bucketName, objectName, filePath);
        } catch (Exception e) {
            log.error("Error uploading file: {}", e.getMessage());
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }

    private ProductDto mapToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setQuantity(product.getQuantity());
        if(product.getImageUrl()!=null) {
            productDto.setImageUrl(fileUploaderService.getFileUrl(bucketName,product.getImageUrl()));
        }
        productDto.setCategory(product.getCategory());
        productDto.setRating(product.getRating());
        productDto.setNumRatings(product.getNumRatings());
        return productDto;
    }

    private Product mapToEntity(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setImageUrl(productDto.getImageUrl());
        product.setCategory(productDto.getCategory());
        product.setRating(productDto.getRating());
        product.setNumRatings(productDto.getNumRatings());
        return product;
    }

    // Updates non-file fields from the DTO into an existing Product.
    private void updateEntity(Product product, ProductDto productDto) {
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setCategory(productDto.getCategory());
        product.setRating(productDto.getRating());
        product.setNumRatings(productDto.getNumRatings());
    }
}
