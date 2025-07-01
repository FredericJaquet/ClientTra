package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.bases.BaseAddressDTO;
import com.frederic.clienttra.dto.create.CreatePhoneRequestDTO;
import com.frederic.clienttra.dto.read.AddressDTO;
import com.frederic.clienttra.dto.create.CreateAddressRequestDTO;
import com.frederic.clienttra.dto.update.UpdateAddressRequestDTO;
import com.frederic.clienttra.dto.update.UpdatePhoneRequestDTO;
import com.frederic.clienttra.entities.Address;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Component
public class AddressMapper {

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

    public CreateAddressRequestDTO toCreateAddressRequestDTO(UpdateAddressRequestDTO addressDTO){
        return CreateAddressRequestDTO.builder()
                .street(addressDTO.getStreet())
                .stNumber(addressDTO.getStNumber())
                .apt(addressDTO.getApt())
                .cp(addressDTO.getCp())
                .city(addressDTO.getCity())
                .state(addressDTO.getState())
                .country(addressDTO.getCountry())
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
