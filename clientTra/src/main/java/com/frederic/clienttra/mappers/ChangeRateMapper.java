package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.bases.BaseChangeRateDTO;
import com.frederic.clienttra.dto.create.CreateChangeRateRequestDTO;
import com.frederic.clienttra.dto.read.ChangeRateDTO;
import com.frederic.clienttra.dto.update.UpdateChangeRateRequestDTO;
import com.frederic.clienttra.entities.ChangeRate;
import com.frederic.clienttra.entities.Company;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChangeRateMapper {

    public List<ChangeRateDTO> toDtos(List<ChangeRate> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ChangeRateDTO toDto(ChangeRate entity) {
        return ChangeRateDTO.builder()
                .idChangeRate(entity.getIdChangeRate())
                .currency1(entity.getCurrency1())
                .currency2(entity.getCurrency2())
                .rate(entity.getRate())
                .date(entity.getDate())
                .build();
    }

    public CreateChangeRateRequestDTO toCreateChangeRateRequestDTO(UpdateChangeRateRequestDTO dto, ChangeRate entity) {
        return CreateChangeRateRequestDTO.builder()
                .currency1(dto.getCurrency1() != null ? dto.getCurrency1() : entity.getCurrency1())
                .currency2(dto.getCurrency2() != null ? dto.getCurrency2() : entity.getCurrency2())
                .rate(dto.getRate() != null ? dto.getRate() : entity.getRate())
                .date(dto.getDate() != null ? dto.getDate() : entity.getDate())
                .build();
    }

    public ChangeRate toEntity(BaseChangeRateDTO dto) {
        return ChangeRate.builder()
                .currency1(dto.getCurrency1())
                .currency2(dto.getCurrency2())
                .rate(dto.getRate())
                .date(dto.getDate())
                .build();
    }

    public ChangeRate toEntity(CreateChangeRateRequestDTO dto, Company ownerCompany) {
        return ChangeRate.builder()
                .currency1(dto.getCurrency1())
                .currency2(dto.getCurrency2())
                .rate(dto.getRate())
                .date(dto.getDate())
                .ownerCompany(ownerCompany)
                .build();
    }

    public List<ChangeRate> toEntities(List<CreateChangeRateRequestDTO> dtos, Company ownercompany){
        return dtos.stream()
                .map(dto -> toEntity(dto, ownercompany))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void updateEntity(ChangeRate entity, UpdateChangeRateRequestDTO dto) {
        if (dto.getCurrency1() != null) {
            entity.setCurrency1(dto.getCurrency1());
        }
        if (dto.getCurrency2() != null) {
            entity.setCurrency2(dto.getCurrency2());
        }
        if (dto.getRate() != null) {
            entity.setRate(dto.getRate());
        }
        if (dto.getDate() != null) {
            entity.setDate(dto.getDate());
        }
    }
}
