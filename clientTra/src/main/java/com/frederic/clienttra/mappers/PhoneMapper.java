package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.bases.BasePhoneDTO;
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

    public CreatePhoneRequestDTO toCreatePhoneRequestDTO(UpdatePhoneRequestDTO dto, Phone entity){
        return CreatePhoneRequestDTO.builder()
                .phoneNumber(dto.getPhoneNumber() != null ? dto.getPhoneNumber() : entity.getPhoneNumber())
                .kind(dto.getKind() != null ? dto.getKind() : entity.getKind())
                .build();
    }

    public Phone toEntity(BasePhoneDTO dto) {
        return Phone.builder()
                .phoneNumber(dto.getPhoneNumber())
                .kind(dto.getKind())
                .build();
    }

    public void updateEntity(Phone entity, UpdatePhoneRequestDTO dto) {
        if (dto.getPhoneNumber() != null) {
            entity.setPhoneNumber(dto.getPhoneNumber());
        }
        if( dto.getKind() != null) {
            entity.setKind(dto.getKind());
        }
    }
}
