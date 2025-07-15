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

@Component
public class ItemMapper {

    public ItemDTO toDto(Item entity){
        return ItemDTO.builder()
                .descrip(entity.getDescrip())
                .qty((entity.getQty()))
                .discount(entity.getDiscount())
                .total(entity.getTotal())
                .build();
    }

    public List<ItemDTO> toDtos(List<Item> entities){
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Item toEntity(BaseItemDTO dto){
        return Item.builder()
                .descrip(dto.getDescrip())
                .qty(dto.getQty())
                .discount(dto.getDiscount())
                .total(dto.getTotal())
                .build();
    }

    public List<Item> toEntities(List<? extends BaseItemDTO> dtos){
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
