package zw.co.trolley.AuthService.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.trolley.AuthService.domain.dtos.AddressDto;
import zw.co.trolley.AuthService.domain.dtos.UserDto;
import zw.co.trolley.AuthService.services.UserService;


import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getProfile(@RequestHeader("X-User-ID") UUID userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateProfile(
            @RequestHeader("X-User-ID") UUID userId,
            @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateProfile(userId, userDto));
    }

    @PostMapping("/addresses")
    public ResponseEntity<AddressDto> addAddress(
            @RequestHeader("X-User-ID") UUID userId,
            @RequestBody AddressDto addressDto) {
        return ResponseEntity.ok(userService.addAddress(userId, addressDto));
    }
}