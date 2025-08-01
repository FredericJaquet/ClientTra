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

/**
 * Mapper class responsible for converting between Address entities and various Address DTOs.
 * <p>
 * Provides methods to map entities to DTOs, DTOs to entities, update entities from DTOs,
 * and handle lists of these objects.
 * </p>
 */
@Component
public class AddressMapper {

    /**
     * Converts a list of Address entities into a list of AddressDTOs.
     *
     * @param entities the list of Address entities to convert
     * @return a list of AddressDTO objects representing the entities
     */
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

    /**
     * Converts a single Address entity into an AddressDTO.
     *
     * @param address the Address entity to convert
     * @return an AddressDTO representing the given entity
     */
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

    /**
     * Converts an UpdateAddressRequestDTO and an existing Address entity
     * into a CreateAddressRequestDTO, merging updated fields and existing values.
     *
     * @param dto the UpdateAddressRequestDTO containing new values (nullable)
     * @param entity the existing Address entity with current values
     * @return a CreateAddressRequestDTO with updated and existing values combined
     */
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

    /**
     * Converts a BaseAddressDTO into an Address entity.
     *
     * @param dto the BaseAddressDTO to convert
     * @return an Address entity built from the DTO data
     */
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

    /**
     * Converts a list of BaseAddressDTO (or subclasses) into a list of Address entities.
     *
     * @param dtos the list of BaseAddressDTO objects to convert
     * @return a list of Address entities
     */
    public List<Address> toEntities(List< ? extends BaseAddressDTO> dtos){
        return dtos.stream().map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Converts a BaseAddressDTO and associates it with a Company, returning an Address entity.
     *
     * @param dto the BaseAddressDTO to convert
     * @param company the Company entity to associate with the Address
     * @return an Address entity built from the DTO and linked to the given Company
     */
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

    /**
     * Converts a list of BaseAddressDTO (or subclasses), associating each Address entity with the given Company.
     *
     * @param dtos the list of BaseAddressDTO objects to convert
     * @param company the Company entity to associate with each Address
     * @return a list of Address entities linked to the specified Company
     */
    public List<Address> toEntities(List< ? extends BaseAddressDTO> dtos, Company company){
        return dtos.stream()
                .map(dto -> toEntity(dto, company))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Updates an existing Address entity with non-null values from an UpdateAddressRequestDTO.
     *
     * @param entity the Address entity to update
     * @param dto the UpdateAddressRequestDTO containing new values (nullable)
     */
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
