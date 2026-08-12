package com.jaykabra.jaykart.service;

import com.jaykabra.jaykart.dto.ProfileRequestDto;
import com.jaykabra.jaykart.dto.ProfileResponseDto;

public interface IProfileService {

    ProfileResponseDto getProfile();

    ProfileResponseDto updateProfile(ProfileRequestDto profileRequestDto);
}
