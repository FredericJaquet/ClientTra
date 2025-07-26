package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.bases.BaseOrderDTO;
import com.frederic.clienttra.dto.create.CreateItemRequestDTO;
import com.frederic.clienttra.dto.create.CreateOrderRequestDTO;
import com.frederic.clienttra.dto.read.OrderDetailsDTO;
import com.frederic.clienttra.dto.update.UpdateItemRequestDTO;
import com.frederic.clienttra.dto.update.UpdateOrderRequestDTO;
import com.frederic.clienttra.entities.*;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.exceptions.CantCreateOrderWithoutItemsException;
import com.frederic.clienttra.exceptions.CantModifyPaidInvoiceException;
import com.frederic.clienttra.exceptions.OrderNotFoundException;
import com.frederic.clienttra.mappers.CompanyMapper;
import com.frederic.clienttra.mappers.ItemMapper;
import com.frederic.clienttra.mappers.OrderMapper;
import com.frederic.clienttra.repositories.OrderRepository;
import com.frederic.clienttra.security.CustomUserDetails;
import com.frederic.clienttra.testutils.SecurityTestUtils;
import com.frederic.clienttra.utils.DocumentUtils;
import com.frederic.clienttra.validators.OwnerValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private CompanyService companyService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private CompanyMapper companyMapper;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private OwnerValidator ownerValidator;
    @Mock
    private DocumentUtils documentUtils;
    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private CustomUserDetails getCurrentUser(int idUser, int idCompany) {
        return new CustomUserDetails(
                idUser,                      // idUser
                "user",                      // username
                "pass",                      // password
                true,                        // enabled
                List.of(),                   // authorities (vacía, suficiente para test)
                idCompany,                   // idCompany
                "es"                         // preferredLanguage
        );
    }

    private Order.OrderBuilder baseOrder(int idOrder, Company ownerCompany, Company company){
        return Order.builder()
                .idOrder(idOrder)
                .descrip("OrderTest")
                .dateOrder(LocalDate.of(2025,1,1))
                .pricePerUnit(0.04)
                .units("word")
                .total(100.0)
                .billed(false)
                .company(company)
                .ownerCompany(ownerCompany)
                .items(new ArrayList<>());
    }

    private List<Order> baseOrdersList(Company ownerCompany, Company company){
        List<Order> orders = new ArrayList<>();
        for(int i=0; i<3; i++){
            Order order = baseOrder(i,ownerCompany,company).build();
            orders.add(order);
        }
        return orders;
    }

    private List<Item> baseItems(Order order){
        Item item1 = new Item(1, "Item1", 750.0, 0.0, 30.0, order);
        Item item2 = new Item(2, "Item2", 625.0, 20.0, 20.0, order);
        Item item3 = new Item(3, "Item2", 2500.0, 50.0, 50.0, order);
        return new ArrayList<>(List.of(item1, item2, item3));
    }

    private OrderDetailsDTO.OrderDetailsDTOBuilder baseOrderDetailsDto(Order order){
        return OrderDetailsDTO.builder()
                .idOrder(order.getIdOrder())
                .descrip(order.getDescrip())
                .dateOrder(order.getDateOrder())
                .pricePerUnit(order.getPricePerUnit())
                .units(order.getUnits())
                .total(order.getTotal())
                .billed(order.getBilled())
                .company(companyMapper.toBaseCompanyMinimalDTO(order.getCompany()))
                .items(itemMapper.toDtos(order.getItems()));
    }

    private CreateOrderRequestDTO.CreateOrderRequestDTOBuilder baseCreateOrderDTO(Order order){
        return CreateOrderRequestDTO.builder()
                .descrip(order.getDescrip())
                .dateOrder(order.getDateOrder())
                .pricePerUnit(order.getPricePerUnit())
                .units(order.getUnits())
                .total(order.getTotal())
                .billed(order.getBilled())
                .items(baseCreateItemDTOs(order.getItems()));
    }

    private List<CreateItemRequestDTO> baseCreateItemDTOs(List<Item> items){
        List<CreateItemRequestDTO> createItemDTOs = new ArrayList<>();
        for(Item item:items){
            createItemDTOs.add(CreateItemRequestDTO.builder()
                    .descrip(item.getDescrip())
                    .qty(item.getQty())
                    .discount(item.getDiscount())
                    .total(item.getTotal())
                    .build());
        }
        return createItemDTOs;
    }

    private UpdateOrderRequestDTO.UpdateOrderRequestDTOBuilder baseUpdateOrderDTO(Order order){
        return UpdateOrderRequestDTO.builder()
                .descrip("SomethingElse")
                .dateOrder(order.getDateOrder())
                .pricePerUnit(0.05)
                .units(order.getUnits())
                .total(order.getTotal())
                .billed(order.getBilled())
                .items(baseUpdateItemDTOs(order.getItems()));
    }

    private List<UpdateItemRequestDTO> baseUpdateItemDTOs(List<Item> items){
        List<UpdateItemRequestDTO> updateItemDTOs = new ArrayList<>();
        for(Item item:items){
            updateItemDTOs.add(UpdateItemRequestDTO.builder()
                    .descrip(item.getDescrip())
                    .qty(item.getQty())
                    .discount(item.getDiscount())
                    .total(item.getTotal())
                    .build());
        }
        return updateItemDTOs;
    }

    private Company getCurrentCompany(int idCompany){
        return Company.builder()
                .idCompany(idCompany)
                .vatNumber("vatNumber")
                .legalName("legalName")
                .comName("comName")
                .email("email")
                .web("web")
                .logoPath("logoPath")
                .addresses(new ArrayList<Address>())
                .phones(new ArrayList<Phone>())
                .contactPersons(new ArrayList<ContactPerson>())
                .bankAccounts(new ArrayList<BankAccount>())
                .changeRates(new ArrayList<ChangeRate>())
                .users(new ArrayList<User>())
                .customers(new ArrayList<Customer>())
                .providers(new ArrayList<Provider>())
                .build();
    }

    @Test
    void getOrderDetails_shouldReturnOrderDetailsDTO_WhenFound(){
        int idUser = 1;
        int idOwnerCompany = 99;
        int idCompany = 100;
        int idOrder = 88;
        CustomUserDetails currentUser = getCurrentUser(idUser, idOwnerCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        Company ownerCompany=getCurrentCompany(idOwnerCompany);
        Company company = new Company();
        company.setIdCompany(idCompany);
        Order order = baseOrder(idOrder, ownerCompany, company).build();

        OrderDetailsDTO dto = baseOrderDetailsDto(order).build();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(ownerCompany);
        when(orderRepository.findByIdOrderAndOwnerCompany(idOrder,ownerCompany)).thenReturn(Optional.of(order));
        when(orderMapper.toDetailsDto(order)).thenReturn(dto);

        OrderDetailsDTO result = orderService.getOrderDetails(idCompany, idOrder);

        verify(companyService).getCurrentCompanyOrThrow();
        verify(orderRepository).findByIdOrderAndOwnerCompany(idOrder, ownerCompany);
        verify(orderMapper).toDetailsDto(order);

        assertThat(result.getIdOrder()).isEqualTo(order.getIdOrder());
    }

    @Test
    void getOrderDetails_shouldThrow_OrderNotFoundException_IfNotFound(){
        int idUser = 1;
        int idOwnerCompany = 99;
        int idCompany = 100;
        int idOrder = 88;
        CustomUserDetails currentUser = getCurrentUser(idUser, idOwnerCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        Company ownerCompany=getCurrentCompany(idOwnerCompany);
        Company company = new Company();
        company.setIdCompany(idCompany);

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(ownerCompany);
        when(orderRepository.findByIdOrderAndOwnerCompany(idOrder,ownerCompany)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrderDetails(idCompany, idOrder))
                .isInstanceOf(OrderNotFoundException.class);

        verify(companyService).getCurrentCompanyOrThrow();
        verify(orderRepository).findByIdOrderAndOwnerCompany(idOrder, ownerCompany);
        verifyNoMoreInteractions(orderMapper);
    }

    @Test
    void getOrderDetails_shouldThrow_OrderNotFoundException_IfInvalidIdCompany(){
        int idUser = 1;
        int idOwnerCompany = 99;
        int idCompany = 100;
        int idOrder = 88;
        CustomUserDetails currentUser = getCurrentUser(idUser, idOwnerCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        Company ownerCompany=getCurrentCompany(idOwnerCompany);
        Company company = new Company();
        company.setIdCompany(idCompany+1);//Must be different from idCompany
        Order order = baseOrder(idOrder, ownerCompany, company).build();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(ownerCompany);
        when(orderRepository.findByIdOrderAndOwnerCompany(idOrder,ownerCompany)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.getOrderDetails(idCompany, idOrder))
                .isInstanceOf(OrderNotFoundException.class);

        verify(companyService).getCurrentCompanyOrThrow();
        verify(orderRepository).findByIdOrderAndOwnerCompany(idOrder, ownerCompany);
        verifyNoMoreInteractions(orderMapper);
    }

    @Test
    void createOrder_ShouldSaveOrder_whenItemsExist(){
        int idUser = 1;
        int idOwnerCompany = 99;
        int idCompany = 100;
        int idOrder = 88;
        double totalOrderExpected;

        CustomUserDetails currentUser = getCurrentUser(idUser, idOwnerCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        Company ownerCompany=getCurrentCompany(idOwnerCompany);
        Company company = new Company();
        company.setIdCompany(idCompany);
        Order order = baseOrder(idOrder, ownerCompany, company).build();
        order.setItems(baseItems(order));
        order.setTotal(0.0);//Lo tiene que calcular el método createOrder()
        CreateOrderRequestDTO dto = baseCreateOrderDTO(order).build();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(ownerCompany);
        when(companyService.getCompanyById(idCompany)).thenReturn(company);
        doNothing().when(ownerValidator).checkOwner(idCompany);
        when(orderMapper.toEntity(dto)).thenReturn(order);

        totalOrderExpected = order.getItems().stream()
                .mapToDouble(item -> {
                    double discount = item.getDiscount() != null ? item.getDiscount() : 0.0;
                    return item.getQty() * order.getPricePerUnit() * (1 - discount / 100.0);
                }).sum();

        orderService.createOrder(idCompany, dto);

        verify(companyService).getCurrentCompanyOrThrow();
        verify(companyService).getCompanyById(idCompany);
        verify(ownerValidator).checkOwner(idCompany);
        verify(orderRepository).save(order);
        assertThat(order.getTotal()).isEqualTo(totalOrderExpected);
        assertThat(order.getItems()).allSatisfy(item -> assertThat(item.getOrder()).isEqualTo(order));
    }

    @Test
    void createOrder_ShouldThrowCantCreateOrderWithoutItemsException_WhenNoItemFound(){
        int idUser = 1;
        int idOwnerCompany = 99;
        int idCompany = 100;
        int idOrder = 88;

        CustomUserDetails currentUser = getCurrentUser(idUser, idOwnerCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);
        Company ownerCompany=getCurrentCompany(idOwnerCompany);

        Company company = new Company();
        company.setIdCompany(idCompany);
        Order order = baseOrder(idOrder, ownerCompany, company).build();
        order.setTotal(0.0);//Lo tiene que calcular el método createOrder()
        order.setItems(Collections.emptyList());//El pedido tiene que ir SIN Items
        CreateOrderRequestDTO dto = baseCreateOrderDTO(order).build();

        doNothing().when(ownerValidator).checkOwner(idCompany);
        when(orderMapper.toEntity(dto)).thenReturn(order);

        assertThatThrownBy(() -> orderService.createOrder(idCompany, dto))
                .isInstanceOf(CantCreateOrderWithoutItemsException.class);

        verify(companyService).getCurrentCompanyOrThrow();
        verify(companyService).getCompanyById(idCompany);
        verify(ownerValidator).checkOwner(idCompany);
        verify(orderMapper).toEntity(dto);
        verifyNoMoreInteractions(orderRepository, orderMapper);
    }

    @Test
    void updateOrder_ShouldUpdateAndSave_IfFound_AndNotBilled(){
        int idUser = 1;
        int idOwnerCompany = 99;
        int idCompany = 100;
        int idOrder = 88;

        CustomUserDetails currentUser = getCurrentUser(idUser, idOwnerCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);
        Company ownerCompany=getCurrentCompany(idOwnerCompany);
        Company company = new Company();
        company.setIdCompany(idCompany);
        Order order = baseOrder(idOrder, ownerCompany, company).build();
        order.setItems(baseItems(order));
        order.setTotal(0.0);//Lo tiene que calcular el método updateOrder()
        order.setDocuments(new ArrayList<>());//El pedido no pertenece a ningún documento
        UpdateOrderRequestDTO dto = baseUpdateOrderDTO(order).build();
        dto.addItem(new UpdateItemRequestDTO(null, "Item4", 2500.0, 75.0, 31.25));

        double totalOrderExpected = dto.getItems().stream()
                .mapToDouble(item -> {
                    double discount = item.getDiscount() != null ? item.getDiscount() : 0.0;
                    return item.getQty() * dto.getPricePerUnit() * (1 - discount / 100.0);
                }).sum();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(ownerCompany);
        when(orderRepository.findByIdOrderAndOwnerCompany(idOrder,ownerCompany)).thenReturn(Optional.of(order));
        doAnswer(invocation -> { //simular el método orderMapper.updateEntity(entity, dto) para actualizar los campos básicos;
            Order orderArg = invocation.getArgument(0);
            UpdateOrderRequestDTO dtoArg = invocation.getArgument(1);

            orderArg.setDescrip(dtoArg.getDescrip());
            orderArg.setDateOrder(dtoArg.getDateOrder());
            orderArg.setPricePerUnit(dtoArg.getPricePerUnit());
            orderArg.setUnits(dtoArg.getUnits());
            orderArg.setTotal(dtoArg.getTotal());
            orderArg.setBilled(dtoArg.getBilled());
            return null;
        }).when(orderMapper).updateEntity(any(Order.class), any(UpdateOrderRequestDTO.class));
        when(itemMapper.toEntity(any(UpdateItemRequestDTO.class)))
                .thenAnswer(invocation -> {
                    UpdateItemRequestDTO dtoArg = invocation.getArgument(0);
                    Item item = new Item();
                    item.setDescrip(dtoArg.getDescrip());
                    item.setQty(dtoArg.getQty());
                    item.setDiscount(dtoArg.getDiscount());
                    item.setTotal(dtoArg.getTotal());
                    return item;
                });

        doNothing().when(documentUtils).calculateTotals(any());

        orderService.updateOrder(idCompany, idOrder, dto);

        verify(companyService).getCurrentCompanyOrThrow();
        verify(orderRepository).findByIdOrderAndOwnerCompany(idOrder, ownerCompany);
        verify(orderMapper).updateEntity(order, dto);
        verify(orderRepository).save(order);
        verify(orderMapper).toDetailsDto(any());

        assertThat(order.getItems().size()).isEqualTo(dto.getItems().size());
        assertThat(order.getTotal()).isEqualTo(totalOrderExpected, within(0.01));
        assertThat(order.getItems()).allSatisfy(item -> {
            double expectedItemTotal = item.getQty() * order.getPricePerUnit() * (1 - (item.getDiscount() != null ? item.getDiscount() : 0.0) / 100.0);
            assertThat(item.getTotal()).isEqualTo(expectedItemTotal, within(0.01));
        });

        assertThat(order.getDescrip()).isEqualTo("SomethingElse");
        assertThat(order.getPricePerUnit()).isEqualTo(0.05);
    }

    @Test
    void updateOrder_ShouldThrowOrderNotFoundException_ifNotFound(){
        int idUser = 1;
        int idOwnerCompany = 99;
        int idCompany = 100;
        int idOrder = 88;

        CustomUserDetails currentUser = getCurrentUser(idUser, idOwnerCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);
        Company ownerCompany=getCurrentCompany(idOwnerCompany);
        UpdateOrderRequestDTO dto = UpdateOrderRequestDTO.builder().build();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(ownerCompany);
        when(orderRepository.findByIdOrderAndOwnerCompany(idOrder, ownerCompany)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.updateOrder(idCompany, idOrder, dto))
                .isInstanceOf(OrderNotFoundException.class);
        verify(companyService).getCurrentCompanyOrThrow();
        verify(orderRepository).findByIdOrderAndOwnerCompany(idOrder, ownerCompany);
        verifyNoMoreInteractions(orderRepository, itemMapper, documentUtils, orderMapper);
    }

    @Test
    void updateOrder_ShouldThrowCantModifyPaidInvoiceException_IfOrderBilledAndNotPending(){
        int idUser = 1;
        int idOwnerCompany = 99;
        int idCompany = 100;
        int idOrder = 88;

        CustomUserDetails currentUser = getCurrentUser(idUser, idOwnerCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);
        Company ownerCompany=getCurrentCompany(idOwnerCompany);
        Company company = new Company();
        company.setIdCompany(idCompany);
        Order order = baseOrder(idOrder, ownerCompany, company).build();
        order.setItems(baseItems(order));
        order.setTotal(0.0);//Lo tiene que calcular el método updateOrder()
        Document document=new Document();
        document.setDocType(DocumentType.INV_CUST);
        document.setStatus(DocumentStatus.PAID);
        order.setDocuments(new ArrayList<>(List.of(document)));//El pedido pertenece a documento ya pagado
        UpdateOrderRequestDTO dto = baseUpdateOrderDTO(order).build();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(ownerCompany);
        when(orderRepository.findByIdOrderAndOwnerCompany(idOrder, ownerCompany)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.updateOrder(idCompany, idOrder, dto))
                .isInstanceOf(CantModifyPaidInvoiceException.class);
        verify(companyService).getCurrentCompanyOrThrow();
        verify(orderRepository).findByIdOrderAndOwnerCompany(idOrder, ownerCompany);
        verifyNoMoreInteractions(orderRepository, itemMapper, documentUtils, orderMapper);
    }

    @Test
    void updateOrder_ShouldThrowOrderNotFoundException_IfInvalidOwnerCompany(){
        int idUser = 1;
        int idOwnerCompany = 99;
        int idCompany = 100;
        int idOrder = 88;

        CustomUserDetails currentUser = getCurrentUser(idUser, idOwnerCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);
        Company ownerCompany=getCurrentCompany(idOwnerCompany);
        Company company = new Company();
        company.setIdCompany(idCompany);
        Order order = baseOrder(idOrder, ownerCompany, company).build();
        order.setItems(baseItems(order));
        order.setTotal(0.0);//Lo tiene que calcular el método updateOrder()
        order.setOwnerCompany(new Company());//Otro owner
        Document document=new Document();
        document.setDocType(DocumentType.INV_CUST);
        document.setStatus(DocumentStatus.PENDING);
        order.setDocuments(new ArrayList<>(List.of(document)));//El pedido no pertenece a documento ya pagado
        UpdateOrderRequestDTO dto = baseUpdateOrderDTO(order).build();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(ownerCompany);
        when(orderRepository.findByIdOrderAndOwnerCompany(idOrder, ownerCompany)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.updateOrder(idCompany, idOrder, dto))
                .isInstanceOf(OrderNotFoundException.class);

        verify(companyService).getCurrentCompanyOrThrow();
        verify(orderRepository).findByIdOrderAndOwnerCompany(idOrder, ownerCompany);
        verifyNoMoreInteractions(orderRepository, itemMapper, documentUtils, orderMapper);
    }

    @Test
    void updateOrder_ShouldThrowOrderNotFoundException_IfInvalidCompany(){
        int idUser = 1;
        int idOwnerCompany = 99;
        int idCompany = 100;
        int idOrder = 88;

        CustomUserDetails currentUser = getCurrentUser(idUser, idOwnerCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);
        Company ownerCompany=getCurrentCompany(idOwnerCompany);
        Company strangerCompany = new Company();
        strangerCompany.setIdCompany(101);//Otra company
        Order order = baseOrder(idOrder, ownerCompany, strangerCompany).build();
        order.setItems(baseItems(order));
        order.setTotal(0.0);//Lo tiene que calcular el método updateOrder()
        Document document=new Document();
        document.setDocType(DocumentType.INV_CUST);
        document.setStatus(DocumentStatus.PENDING);
        order.setDocuments(new ArrayList<>(List.of(document)));//El pedido no pertenece a documento ya pagado
        UpdateOrderRequestDTO dto = baseUpdateOrderDTO(order).build();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(ownerCompany);
        when(orderRepository.findByIdOrderAndOwnerCompany(idOrder, ownerCompany)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.updateOrder(idCompany, idOrder, dto))
                .isInstanceOf(OrderNotFoundException.class);

        verify(companyService).getCurrentCompanyOrThrow();
        verify(orderRepository).findByIdOrderAndOwnerCompany(idOrder, ownerCompany);
        verifyNoMoreInteractions(orderRepository, itemMapper, documentUtils, orderMapper);
    }

    @Test
    void deleteOrder_ShouldDelete_IfFound(){
        int idUser = 1;
        int idOwnerCompany = 99;
        int idCompany = 100;
        int idOrder = 88;

        CustomUserDetails currentUser = getCurrentUser(idUser, idOwnerCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);
        Company ownerCompany=getCurrentCompany(idOwnerCompany);
        Company company = new Company();
        company.setIdCompany(idCompany);
        Order order = baseOrder(idOrder, ownerCompany, company).build();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(ownerCompany);
        when(orderRepository.findByIdOrderAndOwnerCompany(idOrder, ownerCompany)).thenReturn(Optional.of(order));

        orderService.deleteOrder(idCompany, idOrder);

        verify(companyService).getCurrentCompanyOrThrow();
        verify(orderRepository).findByIdOrderAndOwnerCompany(idOrder, ownerCompany);
        verify(orderRepository).delete(order);
    }

    @Test
    void deleteOrder_ShouldThrowOrderNotFoundException_ifNotFound(){
        int idUser = 1;
        int idOwnerCompany = 99;
        int idCompany = 100;
        int idOrder = 88;

        CustomUserDetails currentUser = getCurrentUser(idUser, idOwnerCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);
        Company ownerCompany=getCurrentCompany(idOwnerCompany);
        Company company = new Company();
        company.setIdCompany(idCompany);
        Order order = baseOrder(idOrder, ownerCompany, company).build();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(ownerCompany);
        when(orderRepository.findByIdOrderAndOwnerCompany(idOrder, ownerCompany)).thenReturn(Optional.of(order));

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(ownerCompany);
        when(orderRepository.findByIdOrderAndOwnerCompany(idOrder, ownerCompany)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.deleteOrder(idCompany, idOrder))
                .isInstanceOf(OrderNotFoundException.class);
        verify(companyService).getCurrentCompanyOrThrow();
        verify(orderRepository).findByIdOrderAndOwnerCompany(idOrder, ownerCompany);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void deleteOrder__ShouldThrowOrderNotFoundException_IfInvalidOwnerCompany() {
        int idUser = 1;
        int idOwnerCompany = 99;
        int idCompany = 100;
        int idOrder = 88;

        CustomUserDetails currentUser = getCurrentUser(idUser, idOwnerCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);
        Company ownerCompany = getCurrentCompany(idOwnerCompany);
        Company company = new Company();
        company.setIdCompany(idCompany);
        Order order = baseOrder(idOrder, ownerCompany, company).build();
        order.setOwnerCompany(new Company());

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(ownerCompany);
        when(orderRepository.findByIdOrderAndOwnerCompany(idOrder, ownerCompany)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.deleteOrder(idCompany, idOrder))
                .isInstanceOf(OrderNotFoundException.class);

        verify(companyService).getCurrentCompanyOrThrow();
        verify(orderRepository).findByIdOrderAndOwnerCompany(idOrder, ownerCompany);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void deleteOrder_ShouldThrowOrderNotFoundException_IfInvalidCompany() {
        int idUser = 1;
        int idOwnerCompany = 99;
        int idCompany = 100;
        int idOrder = 88;

        CustomUserDetails currentUser = getCurrentUser(idUser, idOwnerCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);
        Company ownerCompany = getCurrentCompany(idOwnerCompany);
        Company strangerCompany = new Company();
        strangerCompany.setIdCompany(101);//Otra company
        Order order = baseOrder(idOrder, ownerCompany, strangerCompany).build();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(ownerCompany);
        when(orderRepository.findByIdOrderAndOwnerCompany(idOrder, ownerCompany)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.deleteOrder(idCompany, idOrder))
                .isInstanceOf(OrderNotFoundException.class);

        verify(companyService).getCurrentCompanyOrThrow();
        verify(orderRepository).findByIdOrderAndOwnerCompany(idOrder, ownerCompany);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void markOrderAsBilled_ShouldMarkAllOrdersAsBilled(){
       List<Order> orders = baseOrdersList(new Company(), new Company());

       orderService.markOrdersAsBilled(orders);

        assertThat(orders).allSatisfy(order -> {
            assertThat(order.getBilled()).isTrue();
        });
    }

}
