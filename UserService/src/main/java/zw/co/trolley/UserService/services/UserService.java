package zw.co.trolley.UserService.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zw.co.trolley.UserService.domain.dtos.AddressDto;
import zw.co.trolley.UserService.domain.dtos.UserProfileDto;
import zw.co.trolley.UserService.domain.models.Address;
import zw.co.trolley.UserService.domain.models.UserProfile;
import zw.co.trolley.UserService.domain.repositories.UserProfileRepository;
import zw.co.trolley.UserService.exceptions.UserProfileNotFoundException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileDto createUserProfile(UserProfileDto profileDto) {
        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName(profileDto.getFirstName());
        userProfile.setLastName(profileDto.getLastName());
        userProfile.setPhone(profileDto.getPhone());
        userProfile.setEmail(profileDto.getEmail());
        userProfileRepository.save(userProfile);
        return new UserProfileDto();
    }

    public UserProfileDto getUserProfile(UUID userId) {
        UserProfile userProfile = userProfileRepository.findById(userId).orElseThrow(() -> new UserProfileNotFoundException("User profile not found for userId: " + userId));
        UserProfileDto profileDto = new UserProfileDto();
        profileDto.setFirstName(userProfile.getFirstName());
        profileDto.setLastName(userProfile.getLastName());
        profileDto.setPhone(userProfile.getPhone());
        profileDto.setEmail(userProfile.getEmail());
        return profileDto;

    }

    public UserProfileDto updateProfile(UUID userId, UserProfileDto profileDto) {
        UserProfile userProfile = userProfileRepository.findById(userId).orElseThrow(() -> new UserProfileNotFoundException("User profile not found for userId: " + userId));
        userProfile.setFirstName(profileDto.getFirstName());
        userProfile.setLastName(profileDto.getLastName());
        userProfile.setPhone(profileDto.getPhone());
        userProfile.setEmail(profileDto.getEmail());
        userProfileRepository.save(userProfile);
        return profileDto;
    }

    public AddressDto addAddress(UUID userId, AddressDto addressDto) {
        UserProfile userProfile = userProfileRepository.findById(userId).orElseThrow(() -> new UserProfileNotFoundException("User profile not found for userId: " + userId));
        Address address = new Address();
        address.setAddressLine1(addressDto.getAddressLine1());
        address.setAddressLine2(addressDto.getAddressLine2());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setPostalCode(addressDto.getPostalCode());
        address.setCountry(addressDto.getCountry());
        address.setDefault(addressDto.isDefault());
        address.setUserProfile(userProfile);
        userProfile.getAddresses().add(address);
        userProfileRepository.save(userProfile);
        return addressDto;
    }
}
