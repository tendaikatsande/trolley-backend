package zw.co.trolley.AuthService.domain.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import zw.co.trolley.AuthService.domain.models.User;

@Data
@AllArgsConstructor
public class AddressDto {
    private User user;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private boolean isDefault;
}
