package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.bases.BaseContactPersonDTO;
import com.frederic.clienttra.dto.create.CreateContactPersonRequestDTO;
import com.frederic.clienttra.dto.read.ContactPersonDTO;
import com.frederic.clienttra.dto.update.UpdateContactPersonRequestDTO;
import com.frederic.clienttra.entities.ContactPerson;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class responsible for converting between ContactPerson entities and their DTO representations.
 * <p>
 * Includes conversions for lists and single instances between entity and various DTO types,
 * as well as entity updates from update DTOs.
 * </p>
 */
@Component
public class ContactPersonMapper {

    /**
     * Converts a list of ContactPerson entities to a list of ContactPersonDTOs.
     *
     * @param entities the list of ContactPerson entities to convert
     * @return a list of ContactPersonDTOs representing the entities
     */
    public List<ContactPersonDTO> toContactPersonDTOList(List<ContactPerson> entities){
        return entities.stream()
                .map(p -> ContactPersonDTO.builder()
                        .idContactPerson(p.getIdContactPerson())
                        .firstname(p.getFirstname())
                        .middlename(p.getMiddlename())
                        .lastname(p.getLastname())
                        .role(p.getRole())
                        .email(p.getEmail())
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Converts a single ContactPerson entity to a ContactPersonDTO.
     *
     * @param contact the ContactPerson entity to convert
     * @return a ContactPersonDTO representing the entity
     */
    public ContactPersonDTO toContactPersonDTO(ContactPerson contact){
        return ContactPersonDTO.builder()
                .idContactPerson(contact.getIdContactPerson())
                .firstname(contact.getFirstname())
                .middlename(contact.getMiddlename())
                .lastname(contact.getLastname())
                .role(contact.getRole())
                .email(contact.getEmail())
                .build();
    }

    /**
     * Creates a CreateContactPersonRequestDTO by combining data from an UpdateContactPersonRequestDTO
     * and an existing ContactPerson entity, prioritizing non-null fields from the DTO.
     *
     * @param dto the UpdateContactPersonRequestDTO with updated fields (nullable)
     * @param entity the existing ContactPerson entity to take original values from
     * @return a CreateContactPersonRequestDTO combining updated and existing values
     */
    public CreateContactPersonRequestDTO toCreateContactPersonRequestDTO(UpdateContactPersonRequestDTO dto, ContactPerson entity){
        return CreateContactPersonRequestDTO.builder()
                .firstname(dto.getFirstname() != null ? dto.getFirstname() : entity.getFirstname())
                .middlename(dto.getMiddlename() != null ? dto.getMiddlename() : entity.getMiddlename())
                .lastname(dto.getLastname() != null ? dto.getLastname() : entity.getLastname())
                .role(dto.getRole() != null ? dto.getRole() : entity.getRole())
                .email(dto.getEmail() != null ? dto.getEmail() : entity.getEmail())
                .build();
    }

    /**
     * Converts a BaseContactPersonDTO into a ContactPerson entity.
     *
     * @param dto the BaseContactPersonDTO to convert
     * @return a ContactPerson entity constructed from the DTO data
     */
    public ContactPerson toEntity(BaseContactPersonDTO dto) {
        return ContactPerson.builder()
                .firstname(dto.getFirstname())
                .middlename(dto.getMiddlename())
                .lastname(dto.getLastname())
                .role(dto.getRole())
                .email(dto.getEmail())
                .build();
    }

    /**
     * Updates an existing ContactPerson entity with non-null values from an UpdateContactPersonRequestDTO.
     *
     * @param entity the ContactPerson entity to update
     * @param dto the UpdateContactPersonRequestDTO containing updated fields (nullable)
     */
    public void updateEntity(ContactPerson entity, UpdateContactPersonRequestDTO dto) {
        if (dto.getFirstname() != null) {
            entity.setFirstname(dto.getFirstname());
        }
        if (dto.getMiddlename() != null) {
            entity.setMiddlename(dto.getMiddlename());
        }
        if (dto.getLastname() != null) {
            entity.setLastname(dto.getLastname());
        }
        if (dto.getRole() != null) {
            entity.setRole(dto.getRole());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
    }

}
