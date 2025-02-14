package zw.co.trolley.ProductService.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zw.co.trolley.ProductService.domain.dtos.ProductDto;
import zw.co.trolley.ProductService.domain.models.Product;
import zw.co.trolley.ProductService.services.ProductService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID categoryId,
            Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(search, categoryId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    // Create product endpoint without file upload (JSON only)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.createProduct(productDto));
    }

    // Create product endpoint with file upload (multipart/form-data)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> createProductWithFile(
            @RequestPart("product") ProductDto productDto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                // Create a temporary file from the uploaded MultipartFile
                Path tempFile = Files.createTempFile("upload-", file.getOriginalFilename());
                file.transferTo(tempFile.toFile());

                // Pass the temporary file path to the service
                ProductDto createdProduct = productService.createProduct(productDto, tempFile.toAbsolutePath().toString());

                // Delete the temporary file after upload
                Files.deleteIfExists(tempFile);

                return ResponseEntity.ok(createdProduct);
            } catch (IOException e) {
                throw new RuntimeException("Error processing file upload", e);
            }
        } else {
            return ResponseEntity.ok(productService.createProduct(productDto));
        }
    }

    // Update product endpoint without file upload (JSON only)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable UUID id,
            @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.updateProduct(id, productDto));
    }

    // Update product endpoint with file upload (multipart/form-data)
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> updateProductWithFile(
            @PathVariable UUID id,
            @RequestPart("product") ProductDto productDto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                // Create a temporary file from the uploaded MultipartFile
                Path tempFile = Files.createTempFile("upload-", file.getOriginalFilename());
                file.transferTo(tempFile.toFile());

                // Pass the temporary file path to the service
                ProductDto updatedProduct = productService.updateProduct(id, productDto, tempFile.toAbsolutePath().toString());

                // Delete the temporary file after upload
                Files.deleteIfExists(tempFile);

                return ResponseEntity.ok(updatedProduct);
            } catch (IOException e) {
                throw new RuntimeException("Error processing file upload", e);
            }
        } else {
            return ResponseEntity.ok(productService.updateProduct(id, productDto));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
