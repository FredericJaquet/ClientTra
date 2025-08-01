package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.bases.BaseItemDTO;
import com.frederic.clienttra.dto.read.ItemDTO;
import com.frederic.clienttra.dto.update.UpdateItemRequestDTO;
import com.frederic.clienttra.entities.Item;
import com.frederic.clienttra.entities.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Item entities and their corresponding DTOs.
 * <p>
 * Supports conversions for single Item, lists of Items,
 * as well as creation of entities from base DTOs.
 * </p>
 */
@Component
public class ItemMapper {

    /**
     * Converts an Item entity to an ItemDTO.
     *
     * @param entity the Item entity to convert
     * @return the corresponding ItemDTO
     */
    public ItemDTO toDto(Item entity){
        return ItemDTO.builder()
                .descrip(entity.getDescrip())
                .qty(entity.getQty())
                .discount(entity.getDiscount())
                .total(entity.getTotal())
                .build();
    }

    /**
     * Converts a list of Item entities to a list of ItemDTOs.
     *
     * @param entities list of Item entities
     * @return list of ItemDTOs
     */
    public List<ItemDTO> toDtos(List<Item> entities){
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Converts a BaseItemDTO to an Item entity.
     *
     * @param dto the base DTO containing item data
     * @return the corresponding Item entity
     */
    public Item toEntity(BaseItemDTO dto){
        return Item.builder()
                .descrip(dto.getDescrip())
                .qty(dto.getQty())
                .discount(dto.getDiscount())
                .total(dto.getTotal())
                .build();
    }

    /**
     * Converts a list of BaseItemDTOs to a list of Item entities.
     *
     * @param dtos list of base item DTOs or subclasses
     * @return list of Item entities
     */
    public List<Item> toEntities(List<? extends BaseItemDTO> dtos){
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
