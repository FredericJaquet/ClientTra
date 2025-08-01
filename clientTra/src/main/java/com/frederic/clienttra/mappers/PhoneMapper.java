package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.bases.BasePhoneDTO;
import com.frederic.clienttra.dto.create.CreatePhoneRequestDTO;
import com.frederic.clienttra.dto.read.PhoneDTO;
import com.frederic.clienttra.dto.update.UpdatePhoneRequestDTO;
import com.frederic.clienttra.entities.Phone;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class to convert between Phone entities and various Phone-related DTOs.
 */
@Component
public class PhoneMapper {

    /**
     * Converts a list of Phone entities to a list of PhoneDTOs.
     *
     * @param entities the list of Phone entities to convert
     * @return a list of PhoneDTO objects
     */
    public List<PhoneDTO> toPhoneDTOList(List<Phone> entities){
        return entities.stream()
                .map(p -> PhoneDTO.builder()
                        .idPhone(p.getIdPhone())
                        .phoneNumber(p.getPhoneNumber())
                        .kind(p.getKind())
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Converts a Phone entity to a PhoneDTO.
     *
     * @param phone the Phone entity to convert
     * @return the corresponding PhoneDTO
     */
    public PhoneDTO toPhoneDTO(Phone phone){
        return PhoneDTO.builder()
                .idPhone(phone.getIdPhone())
                .phoneNumber(phone.getPhoneNumber())
                .kind(phone.getKind())
                .build();
    }

    /**
     * Creates a CreatePhoneRequestDTO from an UpdatePhoneRequestDTO and the existing Phone entity.
     * This is useful for patch updates where some fields may be null and should be taken from the existing entity.
     *
     * @param dto the update DTO with possibly partial data
     * @param entity the existing Phone entity
     * @return a CreatePhoneRequestDTO with all necessary data
     */
    public CreatePhoneRequestDTO toCreatePhoneRequestDTO(UpdatePhoneRequestDTO dto, Phone entity){
        return CreatePhoneRequestDTO.builder()
                .phoneNumber(dto.getPhoneNumber() != null ? dto.getPhoneNumber() : entity.getPhoneNumber())
                .kind(dto.getKind() != null ? dto.getKind() : entity.getKind())
                .build();
    }

    /**
     * Converts a BasePhoneDTO to a Phone entity.
     *
     * @param dto the BasePhoneDTO containing phone data
     * @return a new Phone entity
     */
    public Phone toEntity(BasePhoneDTO dto) {
        return Phone.builder()
                .phoneNumber(dto.getPhoneNumber())
                .kind(dto.getKind())
                .build();
    }

    /**
     * Updates an existing Phone entity using data from an UpdatePhoneRequestDTO.
     * Only non-null fields from the DTO will be updated.
     *
     * @param entity the Phone entity to update
     * @param dto the DTO containing update data
     */
    public void updateEntity(Phone entity, UpdatePhoneRequestDTO dto) {
        if (dto.getPhoneNumber() != null) {
            entity.setPhoneNumber(dto.getPhoneNumber());
        }
        if( dto.getKind() != null) {
            entity.setKind(dto.getKind());
        }
    }

}
