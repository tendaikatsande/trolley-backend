package zw.co.trolley.OrderService.domain.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class ShippingAddressDto {
    private UUID id;
    private String fullName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String phone;
}
