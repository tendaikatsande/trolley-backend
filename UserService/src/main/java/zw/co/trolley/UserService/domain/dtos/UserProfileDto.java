package zw.co.trolley.UserService.domain.dtos;

import lombok.Data;
import java.util.List;

@Data
public class UserProfileDto {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private List<AddressDto> addresses;
}
