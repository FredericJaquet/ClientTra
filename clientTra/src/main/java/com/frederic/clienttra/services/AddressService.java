package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateAddressRequestDTO;
import com.frederic.clienttra.dto.read.AddressDTO;
import com.frederic.clienttra.dto.update.UpdateAddressRequestDTO;
import com.frederic.clienttra.entities.Address;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.exceptions.AddressNotFoundException;
import com.frederic.clienttra.exceptions.CompanyNotFoundException;
import com.frederic.clienttra.mappers.AddressMapper;
import com.frederic.clienttra.repositories.AddressRepository;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.utils.validators.DtoValidator;
import com.frederic.clienttra.utils.validators.OwnerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final OwnerValidator ownerValidator;
    private final CompanyRepository companyRepository;
    private final DtoValidator dtoValidator;

    public List<AddressDTO> getAllAddresses(Integer idCompany){
        ownerValidator.checkOwner(idCompany);

        return addressRepository.findByCompany_IdCompany(idCompany).stream()
                .map(p -> AddressDTO.builder()
                        .idAddress(p.getIdAddress())
                        .street(p.getStreet())
                        .stNumber(p.getStNumber())
                        .apt(p.getApt())
                        .cp(p.getCp())
                        .city(p.getCity())
                        .state(p.getState())
                        .country(p.getCountry())
                        .build())
                .toList();
    }

    public AddressDTO getAddress(Integer idCompany, Integer idAddress){
        ownerValidator.checkOwner(idCompany);

        Address entity = addressRepository.findByIdAddressAndCompany_idCompany(idAddress, idCompany)
                .orElseThrow(AddressNotFoundException::new);
        return addressMapper.toAddressDTO(entity);
    }

    public void deleteAddress(Integer idCompany, Integer idAddress){
        ownerValidator.checkOwner(idCompany);

        Address entity = addressRepository.findByIdAddressAndCompany_idCompany(idAddress, idCompany)
                .orElseThrow(AddressNotFoundException::new);
        addressRepository.delete(entity);
    }

    public void createAddress(Integer idCompany, CreateAddressRequestDTO dto){
        ownerValidator.checkOwner(idCompany);

        Company company=companyRepository.findByIdCompany(idCompany)
                .orElseThrow(CompanyNotFoundException::new);
        Address entity = addressMapper.toEntity(dto);
        entity.setCompany(company);
        addressRepository.save(entity);
    }

    public AddressDTO updateAddress(Integer idCompany, Integer idAddress, UpdateAddressRequestDTO dto){
        ownerValidator.checkOwner(idCompany);
        Address entity = addressRepository.findByIdAddressAndCompany_idCompany(idAddress, idCompany)
                .orElseThrow(AddressNotFoundException::new);
        CreateAddressRequestDTO createDto = addressMapper.toCreateAddressRequestDTO(dto,entity);
        dtoValidator.validate(createDto);

        addressMapper.updateEntity(entity, dto);
        Address updated = addressRepository.save(entity);

        return addressMapper.toAddressDTO(updated);
    }

}
