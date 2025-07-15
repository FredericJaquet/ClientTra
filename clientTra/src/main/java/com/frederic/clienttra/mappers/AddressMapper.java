package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.bases.BaseAddressDTO;
import com.frederic.clienttra.dto.read.AddressDTO;
import com.frederic.clienttra.dto.create.CreateAddressRequestDTO;
import com.frederic.clienttra.dto.update.UpdateAddressRequestDTO;
import com.frederic.clienttra.entities.Address;
import com.frederic.clienttra.entities.Company;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;


@Component
public class AddressMapper {

    public List<AddressDTO> toAddressDTOList(List<Address> entities){

        return entities.stream()
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
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public AddressDTO toAddressDTO(Address address){
        return AddressDTO.builder()
                .idAddress(address.getIdAddress())
                .street(address.getStreet())
                .stNumber(address.getStNumber())
                .apt(address.getApt())
                .cp(address.getCp())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .build();
    }

    public CreateAddressRequestDTO toCreateAddressRequestDTO(UpdateAddressRequestDTO dto, Address entity){
        return CreateAddressRequestDTO.builder()
                .street(dto.getStreet() != null ? dto.getStreet() : entity.getStreet())
                .stNumber(dto.getStNumber() != null ? dto.getStNumber() : entity.getStNumber())
                .apt(dto.getApt() != null ? dto.getApt() : entity.getApt())
                .cp(dto.getCp() != null ? dto.getCp() : entity.getCp())
                .city(dto.getCity() != null ? dto.getCity() : entity.getCity())
                .state(dto.getState() != null ? dto.getState() : entity.getState())
                .country(dto.getCountry() != null ? dto.getCountry() : entity.getCountry())
                .build();
    }

    public Address toEntity(BaseAddressDTO dto) {
        return Address.builder()
                .street(dto.getStreet())
                .stNumber(dto.getStNumber())
                .apt(dto.getApt())
                .cp(dto.getCp())
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry())
                .build();
    }

    public List<Address> toEntities(List< ? extends BaseAddressDTO> dtos){
        return dtos.stream().map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Address toEntity(BaseAddressDTO dto, Company company) {
        return Address.builder()
                .street(dto.getStreet())
                .stNumber(dto.getStNumber())
                .apt(dto.getApt())
                .cp(dto.getCp())
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry())
                .company(company)
                .build();
    }

    public List<Address> toEntities(List< ? extends BaseAddressDTO> dtos, Company company){
        return dtos.stream()
                .map(dto -> toEntity(dto, company))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void updateEntity(Address entity, UpdateAddressRequestDTO dto) {
        if(dto.getStreet() != null) {
            entity.setStreet(dto.getStreet());
        }
        if(dto.getStNumber() != null) {
            entity.setStNumber(dto.getStNumber());
        }
        if(dto.getApt() != null){
            entity.setApt(dto.getApt());
        }
        if(dto.getCp() != null) {
            entity.setCp(dto.getCp());
        }
        if(dto.getCity() != null) {
            entity.setCity(dto.getCity());
        }
        if(dto.getState() != null) {
            entity.setState(dto.getState());
        }
        if(dto.getCountry() != null) {
            entity.setCountry(dto.getCountry());
        }
    }

}
