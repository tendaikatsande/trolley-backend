package zw.co.trolley.AuthService.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import zw.co.trolley.AuthService.domain.models.Address;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID>, JpaSpecificationExecutor<Address> {
}