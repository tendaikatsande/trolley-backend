package zw.co.trolley.AuthService.domain.dtos;

import lombok.*;

@Data
public class AuthResponse {
    private String token;
    private String refreshToken;
}


