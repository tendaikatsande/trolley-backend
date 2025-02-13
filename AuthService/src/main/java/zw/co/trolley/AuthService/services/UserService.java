package zw.co.trolley.AuthService.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import zw.co.trolley.AuthService.domain.dtos.AddressDto;
import zw.co.trolley.AuthService.domain.dtos.UserDto;
import zw.co.trolley.AuthService.domain.dtos.UserInfoDetails;
import zw.co.trolley.AuthService.domain.models.Address;
import zw.co.trolley.AuthService.domain.models.User;
import zw.co.trolley.AuthService.domain.repositories.UserRepository;
import zw.co.trolley.AuthService.exceptions.UserProfileNotFoundException;


import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDto createUserProfile(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        user.setEmail(userDto.getEmail());
        userRepository.save(user);
        return new UserDto();
    }

    public UserDto getUserProfile(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserProfileNotFoundException("User profile not found for userId: " + userId));
        UserDto profileDto = new UserDto();
        profileDto.setFirstName(user.getFirstName());
        profileDto.setLastName(user.getLastName());
        profileDto.setPhone(user.getPhone());
        profileDto.setEmail(user.getEmail());
        return profileDto;

    }

    public UserDto updateProfile(UUID userId, UserDto profileDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserProfileNotFoundException("User profile not found for userId: " + userId));
        user.setFirstName(profileDto.getFirstName());
        user.setLastName(profileDto.getLastName());
        user.setPhone(profileDto.getPhone());
        user.setEmail(profileDto.getEmail());
        userRepository.save(user);
        return profileDto;
    }

    public AddressDto addAddress(UUID userId, AddressDto addressDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserProfileNotFoundException("User profile not found for userId: " + userId));
        Address address = new Address();
        address.setAddressLine1(addressDto.getAddressLine1());
        address.setAddressLine2(addressDto.getAddressLine2());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setPostalCode(addressDto.getPostalCode());
        address.setCountry(addressDto.getCountry());
        address.setDefault(addressDto.isDefault());
        address.setUser(user);
        user.getAddresses().add(address);
        userRepository.save(user);
        return addressDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetail = userRepository.findByEmail(username); // Assuming 'email' is used as username

        // Converting UserInfo to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
