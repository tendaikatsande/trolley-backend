package zw.co.trolley.UserService.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.trolley.UserService.domain.dtos.AddressDto;
import zw.co.trolley.UserService.domain.dtos.UserProfileDto;
import zw.co.trolley.UserService.services.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile(@RequestHeader("X-User-ID") UUID userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileDto> updateProfile(
            @RequestHeader("X-User-ID") UUID userId,
            @RequestBody UserProfileDto profileDto) {
        return ResponseEntity.ok(userService.updateProfile(userId, profileDto));
    }

    @PostMapping("/addresses")
    public ResponseEntity<AddressDto> addAddress(
            @RequestHeader("X-User-ID") UUID userId,
            @RequestBody AddressDto addressDto) {
        return ResponseEntity.ok(userService.addAddress(userId, addressDto));
    }
}