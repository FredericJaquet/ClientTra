package com.frederic.clienttra.validators;

import com.frederic.clienttra.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressValidator {
    private final AddressRepository addressRepository;

    public boolean isLastAddress(Integer idCompany){
        Integer addressesCount=addressRepository.countByCompany_IdCompany(idCompany);

        return addressesCount == 1;
    }
}
