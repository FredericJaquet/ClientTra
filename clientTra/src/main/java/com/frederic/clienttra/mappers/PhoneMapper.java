package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.PhoneDTO;
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

    public Phone toEntity(PhoneDTO dto) {
        return Phone.builder()
                .idPhone(dto.getIdPhone())
                .phoneNumber(dto.getPhoneNumber())
                .kind(dto.getKind())
                .build();
    }

    public void updateEntity(Phone entity, PhoneDTO dto) {
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setKind(dto.getKind());
    }
}
