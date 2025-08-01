package com.frederic.clienttra.validators;

import com.frederic.clienttra.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Validator class for address-related validations.
 */
@Component
@RequiredArgsConstructor
public class AddressValidator {

    private final AddressRepository addressRepository;

    /**
     * Checks if the company with the given ID has exactly one address,
     * which means it is the last address associated with that company.
     *
     * @param idCompany the ID of the company to check addresses for.
     * @return true if the company has only one address, false otherwise.
     */
    public boolean isLastAddress(Integer idCompany) {
        Integer addressesCount = addressRepository.countByCompany_IdCompany(idCompany);
        return addressesCount == 1;
    }
}
