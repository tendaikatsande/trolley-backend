package zw.co.trolley.OrderService.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import zw.co.trolley.OrderService.domain.models.ShippingAddress;

import java.util.UUID;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, UUID>, JpaSpecificationExecutor<ShippingAddress> {
}