package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.create.CreatePhoneRequestDTO;
import com.frederic.clienttra.dto.read.PhoneDTO;
import com.frederic.clienttra.dto.update.UpdatePhoneRequestDTO;
import com.frederic.clienttra.entities.Phone;
import org.springframework.stereotype.Component;

@Component
public class PhoneMapper {

    public PhoneDTO toPhoneDTO(Phone phone){
        return PhoneDTO.builder()
                .idPhone(phone.getIdPhone())
                .phoneNumber(phone.getPhoneNumber())
                .kind(phone.getKind())
                .build();
    }

    public CreatePhoneRequestDTO toCreatePhoneRequestDTO(UpdatePhoneRequestDTO phoneDTO){
        return CreatePhoneRequestDTO.builder()
                .idPhone(phoneDTO.getIdPhone())
                .phoneNumber(phoneDTO.getPhoneNumber())
                .kind(phoneDTO.getKind())
                .build();
    }

    public Phone toEntity(CreatePhoneRequestDTO dto) {
        return Phone.builder()
                .idPhone(dto.getIdPhone())
                .phoneNumber(dto.getPhoneNumber())
                .kind(dto.getKind())
                .build();
    }

    public Phone toEntity(UpdatePhoneRequestDTO dto) {
        return Phone.builder()
                .idPhone(dto.getIdPhone())
                .phoneNumber(dto.getPhoneNumber())
                .kind(dto.getKind())
                .build();
    }

    public void updateEntity(Phone entity, UpdatePhoneRequestDTO dto) {
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setKind(dto.getKind());
    }
}
