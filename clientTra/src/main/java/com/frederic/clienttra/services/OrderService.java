package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateOrderRequestDTO;
import com.frederic.clienttra.dto.read.OrderDetailsDTO;
import com.frederic.clienttra.dto.read.OrderListDTO;
import com.frederic.clienttra.dto.update.UpdateItemRequestDTO;
import com.frederic.clienttra.dto.update.UpdateOrderRequestDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Document;
import com.frederic.clienttra.entities.Item;
import com.frederic.clienttra.entities.Order;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.exceptions.CantCreateOrderWithoutItemsException;
import com.frederic.clienttra.exceptions.CantModifyPaidInvoiceException;
import com.frederic.clienttra.exceptions.OrderNotFoundException;
import com.frederic.clienttra.mappers.ItemMapper;
import com.frederic.clienttra.mappers.OrderMapper;
import com.frederic.clienttra.projections.OrderListProjection;
import com.frederic.clienttra.repositories.OrderRepository;
import com.frederic.clienttra.utils.DocumentUtils;
import com.frederic.clienttra.validators.OwnerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing operations related to Orders.
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final CompanyService companyService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ItemMapper itemMapper;
    private final DocumentUtils documentUtils;
    private final OwnerValidator ownerValidator;

    /**
     * Retrieves detailed information of an order by its ID and owning company ID.
     *
     * @param idCompany the ID of the owning company.
     * @param idOrder   the ID of the order to retrieve.
     * @return a DTO with full details of the order.
     * @throws OrderNotFoundException if the order does not exist or does not belong to the company.
     */
    @Transactional(readOnly = true)
    public OrderDetailsDTO getOrderDetails(Integer idCompany, Integer idOrder) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Order order = orderRepository.findByIdOrderAndOwnerCompany(idOrder, owner)
                .orElseThrow(OrderNotFoundException::new);
        if (!order.getCompany().getIdCompany().equals(idCompany)) {
            throw new OrderNotFoundException();
        }
        return orderMapper.toDetailsDto(order);
    }

    /**
     * Retrieves a list of orders associated with a specific company.
     *
     * @param idCompany the ID of the owning company.
     * @return a list of order summary DTOs.
     */
    @Transactional(readOnly = true)
    public List<OrderListDTO> getOrders(Integer idCompany) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<OrderListProjection> orders = orderRepository.findByOwnerCompanyAndCompany_idCompanyOrderByDateOrderDesc(owner, idCompany);

        return orderMapper.toListDtosFromProjection(orders);
    }

    /**
     * Retrieves orders by their IDs and owning company, useful for sending orders to documents.
     *
     * @param orderIds list of order IDs.
     * @param owner    the owning company.
     * @return list of order summary DTOs.
     * @throws OrderNotFoundException if any of the requested orders are not found.
     */
    @Transactional(readOnly = true)
    public List<OrderListDTO> getOrdersByIdsAndOwner(List<Integer> orderIds, Company owner) {
        List<Order> orders = orderRepository.findAllByIdOrderInAndOwnerCompany(orderIds, owner);

        if (orders.size() != orderIds.size()) {
            throw new OrderNotFoundException();
        }

        return orderMapper.toListDtosFromEntities(orders);
    }

    /**
     * Retrieves pending (not yet billed) orders for a given company.
     *
     * @param idCompany the ID of the company.
     * @return list of pending order DTOs.
     */
    @Transactional(readOnly = true)
    public List<OrderListDTO> getPendingOrders(Integer idCompany) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<OrderListProjection> orders = orderRepository.findByOwnerCompanyAndCompany_idCompanyAndBilledFalseOrderByDateOrderDesc(owner, idCompany);

        return orderMapper.toListDtosFromProjection(orders);
    }

    /**
     * Retrieves all pending (not yet billed) orders.
     *
     * @return list of pending order DTOs.
     */
    @Transactional(readOnly = true)
    public List<OrderListDTO> getPendingOrders() {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<OrderListProjection> orders = orderRepository.findByOwnerCompanyAndBilledFalseOrderByDateOrderDesc(owner);

        return orderMapper.toListDtosFromProjection(orders);
    }

    /**
     * Creates a new order for a company.
     *
     * @param idCompany the ID of the company.
     * @param dto       DTO containing data to create the order.
     * @return DTO with details of the created order.
     * @throws CantCreateOrderWithoutItemsException if no items are included in the order.
     */
    @Transactional
    public OrderDetailsDTO createOrder(Integer idCompany, CreateOrderRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Company company = companyService.getCompanyById(idCompany);

        ownerValidator.checkOwner(idCompany);

        Order order = orderMapper.toEntity(dto);
        order.setOwnerCompany(owner);
        order.setCompany(company);
        order.setBilled(false);

        // Associate each item with the order and calculate totals
        double totalOrder = 0.0;
        List<Item> items = order.getItems();
        if(items.isEmpty()){
            throw new CantCreateOrderWithoutItemsException();
        }
        for (Item item : items) {
            item.setOrder(order);
            double discount = item.getDiscount() != null ? item.getDiscount() : 0.0;
            double lineTotal = item.getQty() * order.getPricePerUnit() * (1 - discount / 100.0);
            item.setTotal(lineTotal);
            totalOrder += lineTotal;
        }
        order.setTotal(totalOrder);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDetailsDto(savedOrder);
    }

    /**
     * Updates an existing order and its items.
     *
     * @param idCompany the ID of the owning company.
     * @param idOrder   the ID of the order to update.
     * @param dto       DTO containing updated data.
     * @return DTO with details of the updated order.
     * @throws OrderNotFoundException         if the order is not found or does not belong to the company.
     * @throws CantModifyPaidInvoiceException if the order belongs to an invoice that is already paid.
     */
    @Transactional
    public OrderDetailsDTO updateOrder(Integer idCompany, Integer idOrder, UpdateOrderRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        Order entity = orderRepository.findByIdOrderAndOwnerCompany(idOrder, owner)
                .orElseThrow(OrderNotFoundException::new);

        List<Document> documents=entity.getDocuments();

        // Check that the order does not belong to an already paid invoice
        for(Document document : documents){
            if(document.getDocType().equals(DocumentType.INV_PROV) || document.getDocType().equals(DocumentType.INV_CUST)){
                if(!document.getStatus().equals(DocumentStatus.PENDING)){
                    throw new CantModifyPaidInvoiceException();
                }
            }
        }

        // Validate the order belongs to the correct company
        if (!entity.getOwnerCompany().equals(owner) || !entity.getCompany().getIdCompany().equals(idCompany)) {
            throw new OrderNotFoundException();
        }

        // Update basic fields
        orderMapper.updateEntity(entity, dto);

        // Manage items
        List<UpdateItemRequestDTO> updatedItemsDTO = dto.getItems() != null ? dto.getItems() : Collections.emptyList();

        // Map existing items by their ID to facilitate update
        Map<Integer, Item> existingItemsMap = entity.getItems().stream()
                .filter(i -> i.getIdItem() != null)
                .collect(Collectors.toMap(Item::getIdItem, i -> i));

        List<Item> updatedItems = new ArrayList<>();

        for (UpdateItemRequestDTO itemDTO : updatedItemsDTO) {
            if (itemDTO.getIdItem() != null && existingItemsMap.containsKey(itemDTO.getIdItem())) {
                // Update existing item
                Item item = existingItemsMap.get(itemDTO.getIdItem());
                if (itemDTO.getDescrip() != null){
                    item.setDescrip(itemDTO.getDescrip());
                }
                if (itemDTO.getQty() != null){
                    item.setQty(itemDTO.getQty());
                }
                if (itemDTO.getDiscount() != null){
                    item.setDiscount(itemDTO.getDiscount());
                }
                // Total will be calculated later
                updatedItems.add(item);
                existingItemsMap.remove(itemDTO.getIdItem()); // mark as updated
            } else {
                // New item
                Item newItem = itemMapper.toEntity(itemDTO);
                newItem.setOrder(entity);
                updatedItems.add(newItem);
            }
        }

        // Items left in existingItemsMap are removed and thus not added

        // Replace the whole item list in the entity (orphanRemoval will delete removed items)
        entity.getItems().clear();
        entity.getItems().addAll(updatedItems);

        // Recalculate totals and set order in each item
        double totalOrder = 0.0;
        for (Item item : updatedItems) {
            item.setOrder(entity);
            double discount = item.getDiscount() != null ? item.getDiscount() : 0.0;
            double lineTotal = item.getQty() * entity.getPricePerUnit() * (1 - discount / 100.0);
            item.setTotal(lineTotal);
            totalOrder += lineTotal;
        }
        if(Math.abs(entity.getTotal() - totalOrder) > 0.01) {
            entity.setTotal(totalOrder);
            if (entity.getDocuments() != null) {
                for(Document document : entity.getDocuments()) {
                    documentUtils.calculateTotals(document);
                }
            }
        }

        Order savedEntity = orderRepository.save(entity);

        return orderMapper.toDetailsDto(savedEntity);
    }

    /**
     * Permanently deletes an order.
     *
     * @param idCompany the ID of the owning company.
     * @param idOrder   the ID of the order to delete.
     * @throws OrderNotFoundException if the order does not exist or does not belong to the company.
     */
    @Transactional
    public void deleteOrder(Integer idCompany, Integer idOrder) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Order order = orderRepository.findByIdOrderAndOwnerCompany(idOrder, owner)
                .orElseThrow(OrderNotFoundException::new);
        if (!order.getOwnerCompany().equals(owner) || !order.getCompany().getIdCompany().equals(idCompany)) {
            throw new OrderNotFoundException();
        }
        orderRepository.delete(order);
    }

    /**
     * Marks a list of orders as billed.
     *
     * @param orders the list of orders to mark as billed.
     */
    @Transactional
    public void markOrdersAsBilled(List<Order> orders){
        if (orders == null || orders.isEmpty()) {
            return;
        }
        orders.forEach(order -> order.setBilled(true));
    }
}
