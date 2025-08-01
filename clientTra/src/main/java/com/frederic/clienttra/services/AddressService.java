package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateAddressRequestDTO;
import com.frederic.clienttra.dto.read.AddressDTO;
import com.frederic.clienttra.dto.update.UpdateAddressRequestDTO;
import com.frederic.clienttra.entities.Address;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.exceptions.AddressNotFoundException;
import com.frederic.clienttra.exceptions.CompanyNotFoundException;
import com.frederic.clienttra.exceptions.LastAddressException;
import com.frederic.clienttra.mappers.AddressMapper;
import com.frederic.clienttra.repositories.AddressRepository;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.validators.AddressValidator;
import com.frederic.clienttra.validators.DtoValidator;
import com.frederic.clienttra.validators.OwnerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Service class responsible for managing addresses associated with companies.
 * <p>
 * Supports operations such as retrieving all addresses of a company, fetching a specific address,
 * creating, updating, and deleting addresses with proper validation and ownership checks.
 */
@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final OwnerValidator ownerValidator;
    private final CompanyRepository companyRepository;
    private final DtoValidator dtoValidator;
    private final AddressValidator addressValidator;

    /**
     * Retrieves all addresses for a given company, sorted by city (case-insensitive).
     *
     * @param idCompany the ID of the company
     * @return a sorted list of {@link AddressDTO} representing the company's addresses
     * @throws SecurityException if the current user is not authorized to access the company
     */
    @Transactional(readOnly = true)
    public List<AddressDTO> getAllAddresses(Integer idCompany){
        ownerValidator.checkOwner(idCompany);

        List<Address> entities = new ArrayList<>(addressRepository.findByCompany_IdCompany(idCompany));
        List<AddressDTO> dtos = addressMapper.toAddressDTOList(entities);
        dtos.sort(Comparator.comparing(AddressDTO::getCity, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));

        return dtos;
    }

    /**
     * Retrieves a specific address by its ID and company ID.
     *
     * @param idCompany the ID of the company owning the address
     * @param idAddress the ID of the address
     * @return the {@link AddressDTO} representing the requested address
     * @throws AddressNotFoundException if the address does not exist or does not belong to the company
     * @throws SecurityException        if the current user is not authorized to access the company
     */
    @Transactional(readOnly = true)
    public AddressDTO getAddress(Integer idCompany, Integer idAddress){
        ownerValidator.checkOwner(idCompany);

        Address entity = addressRepository.findByIdAddressAndCompany_idCompany(idAddress, idCompany)
                .orElseThrow(AddressNotFoundException::new);
        return addressMapper.toAddressDTO(entity);
    }

    /**
     * Deletes an address belonging to a company.
     * <p>
     * Throws {@link LastAddressException} if the address is the last one for the company,
     * preventing deletion to ensure at least one address remains.
     *
     * @param idCompany the ID of the company
     * @param idAddress the ID of the address to delete
     * @throws AddressNotFoundException if the address does not exist or does not belong to the company
     * @throws LastAddressException     if attempting to delete the last remaining address of the company
     * @throws SecurityException        if the current user is not authorized to access the company
     */
    @Transactional
    public void deleteAddress(Integer idCompany, Integer idAddress){
        ownerValidator.checkOwner(idCompany);

        Address entity = addressRepository.findByIdAddressAndCompany_idCompany(idAddress, idCompany)
                .orElseThrow(AddressNotFoundException::new);

        if(addressValidator.isLastAddress(idCompany)){
            throw new LastAddressException();
        }

        addressRepository.delete(entity);
    }

    /**
     * Creates a new address for a given company.
     *
     * @param idCompany the ID of the company
     * @param dto       the data transfer object containing address details
     * @throws CompanyNotFoundException if the company does not exist
     * @throws SecurityException        if the current user is not authorized to access the company
     */
    @Transactional
    public void createAddress(Integer idCompany, CreateAddressRequestDTO dto){
        ownerValidator.checkOwner(idCompany);

        Company company = companyRepository.findByIdCompany(idCompany)
                .orElseThrow(CompanyNotFoundException::new);
        Address entity = addressMapper.toEntity(dto);
        entity.setCompany(company);
        addressRepository.save(entity);
    }

    /**
     * Updates an existing address of a company.
     * <p>
     * Validates the update DTO and applies changes.
     *
     * @param idCompany the ID of the company
     * @param idAddress the ID of the address to update
     * @param dto       the update data transfer object
     * @return the updated {@link AddressDTO}
     * @throws AddressNotFoundException if the address does not exist or does not belong to the company
     * @throws SecurityException        if the current user is not authorized to access the company
     */
    @Transactional
    public AddressDTO updateAddress(Integer idCompany, Integer idAddress, UpdateAddressRequestDTO dto){
        ownerValidator.checkOwner(idCompany);
        Address entity = addressRepository.findByIdAddressAndCompany_idCompany(idAddress, idCompany)
                .orElseThrow(AddressNotFoundException::new);
        CreateAddressRequestDTO createDto = addressMapper.toCreateAddressRequestDTO(dto, entity);
        dtoValidator.validate(createDto);

        addressMapper.updateEntity(entity, dto);
        Address updated = addressRepository.save(entity);

        return addressMapper.toAddressDTO(updated);
    }
}
