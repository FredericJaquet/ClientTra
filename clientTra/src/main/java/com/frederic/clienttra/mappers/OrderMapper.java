package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.bases.BaseOrderDTO;
import com.frederic.clienttra.dto.read.OrderDetailsDTO;
import com.frederic.clienttra.dto.read.OrderListDTO;
import com.frederic.clienttra.dto.update.UpdateOrderRequestDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Item;
import com.frederic.clienttra.entities.Order;
import com.frederic.clienttra.exceptions.InvalidOrderDescriptionException;
import com.frederic.clienttra.exceptions.InvalidOrderPriceException;
import com.frederic.clienttra.projections.OrderListProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {
     private final ItemMapper itemMapper;
     private final MinimalCompanyMapper companyMapper;

    public OrderDetailsDTO toDetailsDto(Order entity){
        return OrderDetailsDTO.builder()
                .idOrder(entity.getIdOrder())
                .descrip(entity.getDescrip())
                .dateOrder(entity.getDateOrder())
                .pricePerUnit(entity.getPricePerUnit())
                .units(entity.getUnits())
                .total(entity.getTotal())
                .billed(entity.getBilled())
                .fieldName(entity.getFieldName())
                .sourceLanguage(entity.getSourceLanguage())
                .targetLanguage(entity.getTargetLanguage())
                .items(itemMapper.toDtos(entity.getItems()))
                .company(companyMapper.toBaseCompanyMinimalDTO(entity.getCompany()))
                .build();
    }

    public List<OrderDetailsDTO> toDetailsDto(List<Order> entities){
        return entities.stream()
                .map(this::toDetailsDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public OrderListDTO toListDtosFromProjection(OrderListProjection entity){
        return OrderListDTO.builder()
                .idOrder(entity.getIdOrder())
                .descrip(entity.getDescrip())
                .dateOrder(entity.getDateOrder())
                .total(entity.getTotal())
                .billed(entity.getBilled())
                .build();
    }

    public OrderListDTO toListDtosFromEntity(Order entity) {
        return OrderListDTO.builder()
                .idOrder(entity.getIdOrder())
                .descrip(entity.getDescrip())
                .dateOrder(entity.getDateOrder())
                .total(entity.getTotal())
                .billed(entity.getBilled())
                .build();
    }

    public List<OrderListDTO> toListDtosFromProjection(List<OrderListProjection> entities){
        return entities.stream()
                .map(this::toListDtosFromProjection)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<OrderListDTO> toListDtosFromEntities(List<Order> entities){
        return entities.stream()
                .map(this::toListDtosFromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Order toEntity(BaseOrderDTO dto){
        return Order.builder()
                .descrip(dto.getDescrip())
                .dateOrder(dto.getDateOrder())
                .pricePerUnit(dto.getPricePerUnit())
                .units(dto.getUnits())
                .total(dto.getTotal())
                .billed(false)
                .fieldName(dto.getFieldName())
                .sourceLanguage(dto.getSourceLanguage())
                .targetLanguage(dto.getTargetLanguage())
                .items(itemMapper.toEntities(dto.getItems()))
                .build();
    }

    public List<Order> toEntities(List<? extends BaseOrderDTO> dtos){//TODO este probablemente nunca se use, pero si actualizar varios pedidos a la vez (para marcar como pagado)
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Order toEntity(BaseOrderDTO dto, Company ownerCompany, Company company){
        List<Item> items = new ArrayList<>(itemMapper.toEntities(dto.getItems()));

        Order order = Order.builder()
                .descrip(dto.getDescrip())
                .dateOrder(dto.getDateOrder())
                .pricePerUnit(dto.getPricePerUnit())
                .units(dto.getUnits())
                .total(dto.getTotal())
                .billed(false)
                .fieldName(dto.getFieldName())
                .sourceLanguage(dto.getSourceLanguage())
                .targetLanguage(dto.getTargetLanguage())
                .items(items)
                .company(company)
                .ownerCompany(ownerCompany)
                .build();

        for(Item item: order.getItems()){
            item.setOrder(order);
        }

        return order;
    }

    public List<Order> toEntities(List<? extends BaseOrderDTO> dtos, Company ownerCompany, Company company){
        return dtos.stream()
                .map(dto -> toEntity(dto, ownerCompany, company))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void updateEntity(Order entity, UpdateOrderRequestDTO dto){
        if(dto.getDescrip() != null){
            if(dto.getDescrip().isBlank()){
                throw new InvalidOrderDescriptionException();
            }
            entity.setDescrip(dto.getDescrip());
        }
        if(dto.getDateOrder() != null){
            entity.setDateOrder(dto.getDateOrder());
        }
        if(dto.getPricePerUnit() != null){
            if(dto.getPricePerUnit()==0.0){
                throw new InvalidOrderPriceException();
            }
            entity.setPricePerUnit(dto.getPricePerUnit());
        }
        if(dto.getUnits() != null){
            entity.setUnits(dto.getUnits());
        }
        if(dto.getTotal() != null){
           entity.setTotal(dto.getTotal());
        }
        if(dto.getBilled() != null){
            entity.setBilled(dto.getBilled());
        }
        if(dto.getFieldName() != null){
            entity.setFieldName(dto.getFieldName());
        }
        if(dto.getSourceLanguage() != null){
            entity.setSourceLanguage(dto.getSourceLanguage());
        }
        if(dto.getTargetLanguage() != null){
            entity.setTargetLanguage(dto.getTargetLanguage());
        }

    }

}
