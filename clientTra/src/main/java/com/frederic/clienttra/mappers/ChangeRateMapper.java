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

/**
 * Mapper class responsible for converting between ChangeRate entities and various ChangeRate DTOs.
 * <p>
 * Provides methods to map entities to DTOs, DTOs to entities, update entities from DTOs,
 * and handle lists of these objects.
 * </p>
 */
@Component
public class ChangeRateMapper {

    /**
     * Converts a list of ChangeRate entities into a list of ChangeRateDTOs.
     *
     * @param entities the list of ChangeRate entities to convert
     * @return a list of ChangeRateDTO objects representing the entities
     */
    public List<ChangeRateDTO> toDtos(List<ChangeRate> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Converts a single ChangeRate entity into a ChangeRateDTO.
     *
     * @param entity the ChangeRate entity to convert
     * @return a ChangeRateDTO representing the given entity
     */
    public ChangeRateDTO toDto(ChangeRate entity) {
        return ChangeRateDTO.builder()
                .idChangeRate(entity.getIdChangeRate())
                .currency1(entity.getCurrency1())
                .currency2(entity.getCurrency2())
                .rate(entity.getRate())
                .date(entity.getDate())
                .build();
    }

    /**
     * Converts an UpdateChangeRateRequestDTO and an existing ChangeRate entity
     * into a CreateChangeRateRequestDTO, merging updated fields and existing values.
     *
     * @param dto the UpdateChangeRateRequestDTO containing new values (nullable)
     * @param entity the existing ChangeRate entity with current values
     * @return a CreateChangeRateRequestDTO with updated and existing values combined
     */
    public CreateChangeRateRequestDTO toCreateChangeRateRequestDTO(UpdateChangeRateRequestDTO dto, ChangeRate entity) {
        return CreateChangeRateRequestDTO.builder()
                .currency1(dto.getCurrency1() != null ? dto.getCurrency1() : entity.getCurrency1())
                .currency2(dto.getCurrency2() != null ? dto.getCurrency2() : entity.getCurrency2())
                .rate(dto.getRate() != null ? dto.getRate() : entity.getRate())
                .date(dto.getDate() != null ? dto.getDate() : entity.getDate())
                .build();
    }

    /**
     * Converts a BaseChangeRateDTO into a ChangeRate entity.
     *
     * @param dto the BaseChangeRateDTO to convert
     * @return a ChangeRate entity built from the DTO data
     */
    public ChangeRate toEntity(BaseChangeRateDTO dto) {
        return ChangeRate.builder()
                .currency1(dto.getCurrency1())
                .currency2(dto.getCurrency2())
                .rate(dto.getRate())
                .date(dto.getDate())
                .build();
    }

    /**
     * Converts a CreateChangeRateRequestDTO and associates it with a Company,
     * returning a ChangeRate entity.
     *
     * @param dto the CreateChangeRateRequestDTO to convert
     * @param ownerCompany the Company entity to associate with the ChangeRate
     * @return a ChangeRate entity built from the DTO and linked to the given Company
     */
    public ChangeRate toEntity(CreateChangeRateRequestDTO dto, Company ownerCompany) {
        return ChangeRate.builder()
                .currency1(dto.getCurrency1())
                .currency2(dto.getCurrency2())
                .rate(dto.getRate())
                .date(dto.getDate())
                .ownerCompany(ownerCompany)
                .build();
    }

    /**
     * Converts a list of CreateChangeRateRequestDTOs, associating each ChangeRate entity with the given Company.
     *
     * @param dtos the list of CreateChangeRateRequestDTO objects to convert
     * @param ownerCompany the Company entity to associate with each ChangeRate
     * @return a list of ChangeRate entities linked to the specified Company
     */
    public List<ChangeRate> toEntities(List<CreateChangeRateRequestDTO> dtos, Company ownerCompany){
        return dtos.stream()
                .map(dto -> toEntity(dto, ownerCompany))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Updates an existing ChangeRate entity with non-null values from an UpdateChangeRateRequestDTO.
     *
     * @param entity the ChangeRate entity to update
     * @param dto the UpdateChangeRateRequestDTO containing new values (nullable)
     */
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
