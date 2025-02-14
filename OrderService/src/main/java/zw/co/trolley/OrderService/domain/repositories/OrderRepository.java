package zw.co.trolley.OrderService.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import zw.co.trolley.OrderService.domain.models.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
  Page<Order> findByUserId(UUID userId, Pageable pageable);
  Optional<Order> findByIdAndUserId(UUID id, UUID userId);
}