package com.jaykabra.jaykart.service.impl;

import com.jaykabra.jaykart.dto.AddressDto;
import com.jaykabra.jaykart.dto.ProfileRequestDto;
import com.jaykabra.jaykart.dto.ProfileResponseDto;
import com.jaykabra.jaykart.entity.Address;
import com.jaykabra.jaykart.entity.Customer;
import com.jaykabra.jaykart.repository.CustomerRepository;
import com.jaykabra.jaykart.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements IProfileService {

    private final CustomerRepository customerRepository;

    @Override
    public ProfileResponseDto getProfile() {
        Customer customer = getAuthenticatedCustomer();
        return mapCustomerToProfileResponseDto(customer);
    }

    @Override
    public ProfileResponseDto updateProfile(ProfileRequestDto profileRequestDto) {
        Customer customer = getAuthenticatedCustomer();
        boolean isEmailUpdated = !customer.getEmail().equals(profileRequestDto.getEmail().trim());
        BeanUtils.copyProperties(profileRequestDto, customer);
        Address address = customer.getAddress();
        if (address == null) {
            address = new Address();
            address.setCustomer(customer);
        }
        address.setStreet(profileRequestDto.getStreet());
        address.setCity(profileRequestDto.getCity());
        address.setState(profileRequestDto.getState());
        address.setPostalCode(profileRequestDto.getPostalCode());
        address.setCountry(profileRequestDto.getCountry());
        customer.setAddress(address);
        customer = customerRepository.save(customer);
        ProfileResponseDto profileResponseDto = mapCustomerToProfileResponseDto(customer);
        profileResponseDto.setEmailUpdated(isEmailUpdated);
        return profileResponseDto;
    }

    public Customer getAuthenticatedCustomer() {
        Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return customerRepository.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private ProfileResponseDto mapCustomerToProfileResponseDto(Customer customer) {
        ProfileResponseDto profileResponseDto = new ProfileResponseDto();
        BeanUtils.copyProperties(customer, profileResponseDto);
        if (customer.getAddress() != null) {
            AddressDto addressDto = new AddressDto();
            BeanUtils.copyProperties(customer.getAddress(), addressDto);
            profileResponseDto.setAddress(addressDto);
        }
        return profileResponseDto;
    }
}
