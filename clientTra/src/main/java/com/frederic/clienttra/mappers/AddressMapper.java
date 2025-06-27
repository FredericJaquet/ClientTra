package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.AddressDTO;
import com.frederic.clienttra.dto.NewAddressDTO;
import com.frederic.clienttra.entities.Address;
import org.springframework.stereotype.Component;

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

    public Address toEntity(AddressDTO dto) {
        return Address.builder()
                .idAddress(dto.getIdAddress())
                .street(dto.getStreet())
                .stNumber(dto.getStNumber())
                .apt(dto.getApt())
                .cp(dto.getCp())
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry())
                .build();
    }

    public Address toEntity(NewAddressDTO dto) {
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

    public void updateEntity(Address entity, AddressDTO dto) {
        entity.setStreet(dto.getStreet());
        entity.setStNumber(dto.getStNumber());
        entity.setApt(dto.getApt());
        entity.setCp(dto.getCp());
        entity.setCity(dto.getCity());
        entity.setState(dto.getState());
        entity.setCountry(dto.getCountry());
    }

}
