package zw.co.trolley.UserService.domain.dtos;


import lombok.Data;
import zw.co.trolley.UserService.domain.models.UserProfile;

@Data
public class AddressDto {
    private UserProfile userProfile;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private boolean isDefault;
}
