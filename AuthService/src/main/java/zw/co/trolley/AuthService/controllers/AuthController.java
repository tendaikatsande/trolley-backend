package zw.co.trolley.AuthService.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.trolley.AuthService.domain.dtos.*;
import zw.co.trolley.AuthService.services.AuthService;
import zw.co.trolley.AuthService.services.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile() {
        return ResponseEntity.ok(authService.getUserProfile());
    }
    @GetMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateProfile(
            @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateProfile( userDto));
    }

    @PostMapping("/addresses")
    public ResponseEntity<AddressDto> addAddress(
            @RequestBody AddressDto addressDto) {
        return ResponseEntity.ok(userService.addAddress( addressDto));
    }

    @PutMapping("/addresses/{id}")
    public ResponseEntity<AddressDto> updateAddress(
            @PathVariable("id") UUID id,
            @RequestBody AddressDto addressDto) {
        return ResponseEntity.ok(userService.updateAddress(id, addressDto));
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<?> deleteAddress(
            @PathVariable("id") UUID id) {
        userService.deleteAddress(id);
        return ResponseEntity.ok().build();
    }

}
