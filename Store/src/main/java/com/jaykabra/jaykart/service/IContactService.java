package com.jaykabra.jaykart.service;

import com.jaykabra.jaykart.dto.ContactRequestDto;
import com.jaykabra.jaykart.dto.ContactResponseDto;
import com.jaykabra.jaykart.dto.ProductDto;

import java.util.List;

public interface IContactService {

    boolean saveContact(ContactRequestDto contactRequestDto);

    List<ContactResponseDto> getAllOpenMessages();

    void updateMessageStatus(Long contactId, String status);
}
