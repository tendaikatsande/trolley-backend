package zw.co.trolley.AuthService.domain.dtos;

import lombok.*;

@Data
public class RegisterRequest{
    private String email;
    private String password;
}
