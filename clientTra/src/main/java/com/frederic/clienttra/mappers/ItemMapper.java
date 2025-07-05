package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.bases.BaseItemDTO;
import com.frederic.clienttra.dto.read.ItemDTO;
import com.frederic.clienttra.dto.update.UpdateItemRequestDTO;
import com.frederic.clienttra.dto.update.UpdateSchemeLineRequestDTO;
import com.frederic.clienttra.entities.Item;
import com.frederic.clienttra.entities.Order;
import com.frederic.clienttra.entities.Scheme;
import com.frederic.clienttra.entities.SchemeLine;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
                .toList();
    }

    public Item toEntity(BaseItemDTO dto){
        return Item.builder()
                .descrip(dto.getDescrip())
                .qty(dto.getQty())
                .discount(dto.getDiscount())
                .total(dto.getTotal())//TODO Lógica para verificar el total
                .build();
    }

    public List<Item> toEntities(List<BaseItemDTO> dtos){
        return dtos.stream()
                .map(this::toEntity)
                .toList();
    }

    public void updateEntities(Order entity, List<UpdateItemRequestDTO> dtos){
        Map<Integer, Item> existingLinesMap = entity.getItems().stream()
                .filter(line -> line.getIdItem() != null)
                .collect(Collectors.toMap(Item::getIdItem, Function.identity()));

        for (UpdateItemRequestDTO dto : dtos) {
            Integer id = dto.getIdItem();
            if (id != null && existingLinesMap.containsKey(id)) {
                Item line = existingLinesMap.get(id);
                if (dto.getDescrip() != null) {
                    line.setDescrip(dto.getDescrip());
                }
                if(dto.getQty() != null){
                    line.setDescrip(dto.getDescrip());
                }
                if (dto.getDiscount() != null) {
                    line.setDiscount(dto.getDiscount());
                }
                if(dto.getTotal() != null){
                    line.setTotal(dto.getTotal());//TODO Lógica para verificar el total
                }
            }
        }
    }


}
