package zw.co.trolley.AuthService.domain.dtos;

import lombok.*;

import java.util.List;

@Data
public class RegisterRequest{
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private List<AddressDto> addresses;
}
