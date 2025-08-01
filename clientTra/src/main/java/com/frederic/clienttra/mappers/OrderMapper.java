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

/**
 * Mapper class for converting between Order entities and their corresponding DTOs.
 * Provides methods to map to/from various Order-related DTO classes.
 * Uses ItemMapper and MinimalCompanyMapper to map nested entities.
 */
@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ItemMapper itemMapper;
    private final MinimalCompanyMapper companyMapper;

    /**
     * Converts an Order entity to an OrderDetailsDTO including nested items and minimal company info.
     *
     * @param entity the Order entity to convert
     * @return the mapped OrderDetailsDTO
     */
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

    /**
     * Converts a list of Order entities to a list of OrderDetailsDTOs.
     *
     * @param entities list of Order entities
     * @return list of mapped OrderDetailsDTOs
     */
    public List<OrderDetailsDTO> toDetailsDtos(List<Order> entities){
        return entities.stream()
                .map(this::toDetailsDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Converts a projection representing an Order list entry to an OrderListDTO.
     *
     * @param entity the OrderListProjection
     * @return mapped OrderListDTO
     */
    public OrderListDTO toListDtosFromProjection(OrderListProjection entity){
        return OrderListDTO.builder()
                .idOrder(entity.getIdOrder())
                .descrip(entity.getDescrip())
                .dateOrder(entity.getDateOrder())
                .total(entity.getTotal())
                .billed(entity.getBilled())
                .build();
    }

    /**
     * Converts an Order entity to an OrderListDTO.
     *
     * @param entity the Order entity
     * @return mapped OrderListDTO
     */
    public OrderListDTO toListDtosFromEntity(Order entity) {
        return OrderListDTO.builder()
                .idOrder(entity.getIdOrder())
                .descrip(entity.getDescrip())
                .dateOrder(entity.getDateOrder())
                .total(entity.getTotal())
                .billed(entity.getBilled())
                .build();
    }

    /**
     * Converts a list of OrderListProjections to a list of OrderListDTOs.
     *
     * @param entities list of OrderListProjections
     * @return list of mapped OrderListDTOs
     */
    public List<OrderListDTO> toListDtosFromProjection(List<OrderListProjection> entities){
        return entities.stream()
                .map(this::toListDtosFromProjection)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Converts a list of Order entities to a list of OrderListDTOs.
     *
     * @param entities list of Order entities
     * @return list of mapped OrderListDTOs
     */
    public List<OrderListDTO> toListDtosFromEntities(List<Order> entities){
        return entities.stream()
                .map(this::toListDtosFromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Converts a BaseOrderDTO to an Order entity.
     * Sets billed flag to false by default.
     *
     * @param dto the BaseOrderDTO to convert
     * @return new Order entity
     */
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

    /**
     * Converts a list of BaseOrderDTOs to a list of Order entities.
     * TODO: This may be used to update multiple orders at once.
     *
     * @param dtos list of BaseOrderDTOs
     * @return list of Order entities
     */
    public List<Order> toEntities(List<? extends BaseOrderDTO> dtos){
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Converts a BaseOrderDTO to an Order entity with ownership and company references.
     * Also sets the bidirectional relationship between Order and its Items.
     *
     * @param dto the BaseOrderDTO
     * @param ownerCompany the owning Company
     * @param company the Company related to the order
     * @return new Order entity
     */
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

        // Set bidirectional link from items to this order
        for(Item item: order.getItems()){
            item.setOrder(order);
        }

        return order;
    }

    /**
     * Converts a list of BaseOrderDTOs to a list of Order entities with ownership and company references.
     *
     * @param dtos list of BaseOrderDTOs
     * @param ownerCompany the owning Company
     * @param company the Company related to the orders
     * @return list of Order entities
     */
    public List<Order> toEntities(List<? extends BaseOrderDTO> dtos, Company ownerCompany, Company company){
        return dtos.stream()
                .map(dto -> toEntity(dto, ownerCompany, company))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Updates an existing Order entity with fields from UpdateOrderRequestDTO.
     * Performs basic validation on description and price fields.
     *
     * @param entity the Order entity to update
     * @param dto the update DTO containing new values
     * @throws InvalidOrderDescriptionException if description is blank
     * @throws InvalidOrderPriceException if pricePerUnit is zero
     */
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
