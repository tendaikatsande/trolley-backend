package zw.co.trolley.AuthService.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import zw.co.trolley.AuthService.domain.dtos.AddressDto;
import zw.co.trolley.AuthService.domain.dtos.UserDto;
import zw.co.trolley.AuthService.domain.dtos.UserInfoDetails;
import zw.co.trolley.AuthService.domain.models.Address;
import zw.co.trolley.AuthService.domain.models.User;
import zw.co.trolley.AuthService.domain.repositories.AddressRepository;
import zw.co.trolley.AuthService.domain.repositories.UserRepository;
import zw.co.trolley.AuthService.exceptions.UserNotFoundException;
import zw.co.trolley.AuthService.exceptions.UserProfileNotFoundException;


import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public UserDto createUserProfile(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        user.setEmail(userDto.getEmail());
        userRepository.save(user);
        return new UserDto();
    }


    public UserDto updateProfile(UserDto profileDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            User user = userRepository.findByEmail(currentUserName)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            user.setFirstName(profileDto.getFirstName());
            user.setLastName(profileDto.getLastName());
            user.setPhone(profileDto.getPhone());
            user.setEmail(profileDto.getEmail());
            userRepository.save(user);
            return profileDto;
        }
        throw new UserNotFoundException("User not found");
    }

    public AddressDto addAddress(AddressDto addressDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            User user = userRepository.findByEmail(currentUserName)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
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
        throw new UserNotFoundException("User not found");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetail = userRepository.findByEmail(username); // Assuming 'email' is used as username

        // Converting UserInfo to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public AddressDto updateAddress(UUID id, AddressDto addressDto) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address does not exist"));
        address.setId(id);
        address.setAddressLine1(addressDto.getAddressLine1());
        address.setAddressLine2(addressDto.getAddressLine2());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setPostalCode(addressDto.getPostalCode());
        address.setCountry(addressDto.getCountry());
        address.setDefault(addressDto.isDefault());
        addressRepository.save(address);
        return addressDto;
    }

    public void deleteAddress(UUID id) {
        addressRepository.deleteById(id);
    }
}
