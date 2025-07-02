package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.bases.BaseContactPersonDTO;
import com.frederic.clienttra.dto.create.CreateContactPersonRequestDTO;
import com.frederic.clienttra.dto.read.ContactPersonDTO;
import com.frederic.clienttra.dto.update.UpdateContactPersonRequestDTO;
import com.frederic.clienttra.entities.ContactPerson;
import org.springframework.stereotype.Component;

@Component
public class ContactPersonMapper {

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

    public CreateContactPersonRequestDTO toCreateContactPersonRequestDTO(UpdateContactPersonRequestDTO dto, ContactPerson entity){
        return CreateContactPersonRequestDTO.builder()
                .firstname(dto.getFirstname() != null ? dto.getFirstname() : entity.getFirstname())
                .middlename(dto.getMiddlename() != null ? dto.getMiddlename() : entity.getMiddlename())
                .lastname(dto.getLastname() != null ? dto.getLastname() : entity.getLastname())
                .role(dto.getRole() != null ? dto.getRole() : entity.getRole())
                .email(dto.getEmail() != null ? dto.getEmail() : entity.getEmail())
                .build();
    }

    public ContactPerson toEntity(BaseContactPersonDTO dto) {
        return ContactPerson.builder()
                .firstname(dto.getFirstname())
                .middlename(dto.getMiddlename())
                .lastname(dto.getLastname())
                .role(dto.getRole())
                .email(dto.getEmail())
                .build();
    }

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
