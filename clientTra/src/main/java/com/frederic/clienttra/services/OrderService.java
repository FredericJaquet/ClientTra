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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CompanyService companyService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ItemMapper itemMapper;
    private final DocumentUtils documentUtils;

    @Transactional(readOnly = true)
    public OrderDetailsDTO getOrderDetails(Integer idCompany, Integer idOrder) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Order order = orderRepository.findByIdOrderAndOwnerCompany(idOrder, owner)
                .orElseThrow(OrderNotFoundException::new);
        if (!order.getOwnerCompany().equals(owner) || !order.getCompany().getIdCompany().equals(idCompany)) {
            throw new OrderNotFoundException();
        }
        return orderMapper.toDetailsDto(order);
    }

    @Transactional(readOnly = true)
    public List<OrderListDTO> getOrders(Integer idCompany) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<OrderListProjection> orders = orderRepository.findByOwnerCompanyAndCompany_idCompanyOrderByDateOrderDesc(owner, idCompany);

        return orderMapper.toListDtosFromProjection(orders);
    }

    @Transactional(readOnly = true)//Esto sirve para enviar los pedidos a un documento.
    public List<OrderListDTO> getOrdersByIdsAndOwner(List<Integer> orderIds, Company owner) {
        List<Order> orders = orderRepository.findAllByIdOrderInAndOwnerCompany(orderIds, owner);

        if (orders.size() != orderIds.size()) {
            throw new OrderNotFoundException();
        }

        return orderMapper.toListDtosFromEntities(orders);
    }

    @Transactional(readOnly = true)
    public List<OrderListDTO> getPendingOrders(Integer idCompany) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<OrderListProjection> orders = orderRepository.findByOwnerCompanyAndCompany_idCompanyAndBilledFalseOrderByDateOrderDesc(owner, idCompany);

        return orderMapper.toListDtosFromProjection(orders);
    }

    @Transactional
    public OrderDetailsDTO createOrder(Integer idCompany, CreateOrderRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Company company = companyService.getCompanyById(idCompany);

        Order order = orderMapper.toEntity(dto);
        order.setOwnerCompany(owner);
        order.setCompany(company);
        order.setBilled(false);

        // Asocia cada línea al pedido y calcula totales
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

        orderRepository.save(order);
        return orderMapper.toDetailsDto(order);
    }

    @Transactional
    public OrderDetailsDTO updateOrder(Integer idCompany, Integer idOrder, UpdateOrderRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        Order entity = orderRepository.findByIdOrderAndOwnerCompany(idOrder, owner)
                .orElseThrow(OrderNotFoundException::new);

        List<Document> documents=entity.getDocuments();

        //Comprobar que el pedido no pertenezca a una factura ya pagada.
        for(Document document : documents){
            if(document.getDocType().equals(DocumentType.INV_PROV) || document.getDocType().equals(DocumentType.INV_CUST)){
                if(!document.getStatus().equals(DocumentStatus.PENDING)){
                    throw new CantModifyPaidInvoiceException();
                }
            }
        }

        // Validar que la orden pertenece a la empresa correcta
        if (!entity.getOwnerCompany().equals(owner) || !entity.getCompany().getIdCompany().equals(idCompany)) {
            throw new OrderNotFoundException();
        }

        // Actualizar campos básicos
        orderMapper.updateEntity(entity, dto);

        // Gestionar líneas (items)
        List<UpdateItemRequestDTO> updatedItemsDTO = dto.getItems() != null ? dto.getItems() : Collections.emptyList();

        // Map idItem -> Item existente para facilitar actualización
        Map<Integer, Item> existingItemsMap = entity.getItems().stream()
                .filter(i -> i.getIdItem() != null)
                .collect(Collectors.toMap(Item::getIdItem, i -> i));

        List<Item> updatedItems = new ArrayList<>();

        for (UpdateItemRequestDTO itemDTO : updatedItemsDTO) {
            if (itemDTO.getIdItem() != null && existingItemsMap.containsKey(itemDTO.getIdItem())) {
                // Actualizar línea existente
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
                // El total lo calculamos después
                updatedItems.add(item);
                existingItemsMap.remove(itemDTO.getIdItem()); // marcamos que está actualizada
            } else {
                // Línea nueva
                Item newItem = itemMapper.toEntity(itemDTO);
                newItem.setOrder(entity);
                updatedItems.add(newItem);
            }
        }

        // Las que quedaron en existingItemsMap son líneas eliminadas, no las añadimos a updatedItems

        // Reemplazar la lista entera en la entidad (orphanRemoval eliminará las que se quitaron)
        entity.getItems().clear();
        entity.getItems().addAll(updatedItems);

        // Recalcular totales y asignar pedido a cada línea
        double totalOrder = 0.0;
        for (Item item : updatedItems) {
            item.setOrder(entity);
            double discount = item.getDiscount() != null ? item.getDiscount() : 0.0;
            double lineTotal = item.getQty() * entity.getPricePerUnit() * (1 - discount / 100.0);
            item.setTotal(lineTotal);
            totalOrder += lineTotal;
        }
        if(Math.abs(entity.getTotal() - totalOrder) > 0.01) {//TODO Comprobar que esto funciona.
            entity.setTotal(totalOrder);
            if (entity.getDocuments() != null) {
                for(Document document : entity.getDocuments()) {
                    documentUtils.calculateTotals(document);
                }
            }
        }
        orderRepository.save(entity);

        return orderMapper.toDetailsDto(entity);
    }

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

    @Transactional
    public void markOrdersAsBilled(List<Order> orders){
        if (orders == null || orders.isEmpty()) {
            return;
        }
        orders.forEach(order -> order.setBilled(true));
    }
}
