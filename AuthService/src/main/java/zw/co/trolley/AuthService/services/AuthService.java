package zw.co.trolley.AuthService.services;


import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import zw.co.trolley.AuthService.configs.JwtService;
import zw.co.trolley.AuthService.domain.dtos.*;
import zw.co.trolley.AuthService.domain.models.User;
import zw.co.trolley.AuthService.domain.repositories.UserRepository;
import zw.co.trolley.AuthService.exceptions.InvalidCredentialsException;
import zw.co.trolley.AuthService.exceptions.UserExistsException;
import zw.co.trolley.AuthService.exceptions.UserNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserExistsException("Email already registered");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser.getEmail());
        String refreshToken = jwtService.generateRefreshToken(savedUser.getEmail());

        savedUser.setRefreshToken(refreshToken);
        userRepository.save(savedUser);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        return response;
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        return response;
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new InvalidCredentialsException("Invalid refresh token");
        }

        String userId = jwtService.extractUserId(refreshToken);
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new InvalidCredentialsException("Invalid refresh token");
        }

        String token = jwtService.generateToken(user.getEmail());
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        return response;

    }

    public UserDto getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();

            User user = userRepository.findByEmail(currentUserName)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            List<AddressDto> addressDtoList = user.getAddresses().stream().map(address -> new AddressDto(
                    address.getId(),
                    null,
                    address.getAddressLine1(),
                    address.getAddressLine2(),
                    address.getCity(),
                    address.getState(),
                    address.getPostalCode(),
                    address.getCountry(),
                    address.isDefault())
            ).toList();

            // Map User entity to UserDto
            return new UserDto(
                    user.getUserId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getPhone(),
                    user.getEmail(),
                    addressDtoList
            );
        }
        throw new UserNotFoundException("User not found");
    }
}
