package zw.co.trolley.AuthService.domain.dtos;

import lombok.*;

@Data
public class AuthRequest {
    private String email;
    private String password;
}


