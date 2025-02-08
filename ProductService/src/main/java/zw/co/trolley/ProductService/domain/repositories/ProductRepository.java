package zw.co.trolley.ProductService.domain.repositories;

import com.netflix.appinfo.ApplicationInfoManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import zw.co.trolley.ProductService.domain.models.Product;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    ApplicationInfoManager.InstanceStatusMapper findByNameContainingAndCategoryId(String search, UUID categoryId, Pageable pageable);

    ApplicationInfoManager.InstanceStatusMapper findByNameContaining(String search, Pageable pageable);

    ApplicationInfoManager.InstanceStatusMapper findByCategoryId(UUID categoryId, Pageable pageable);
}