package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateDocumentRequestDTO;
import com.frederic.clienttra.dto.read.DocumentDTO;
import com.frederic.clienttra.entities.*;
import com.frederic.clienttra.exceptions.DocumentNotFoundException;
import com.frederic.clienttra.mappers.DocumentMapper;
import com.frederic.clienttra.repositories.DocumentRepository;
import com.frederic.clienttra.utils.DocumentUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl {//TODO Separar bien la lógica por Endpoints antes de llegar aquí

    private final OrderService orderService;
    private final CompanyService companyService;
    private final ChangeRateService changeRateService;
    private final BankAccountService bankAccountService;
    private final DocumentMapper documentMapper;
    private final DocumentRepository documentRepository;
    private final DocumentUtils documentUtils;


    @Transactional
    public DocumentDTO createDocument(CreateDocumentRequestDTO dto) {

        Company owner = companyService.getCurrentCompanyOrThrow();
        String notePayment=null;

        // 1. Recuperar entidades relacionadas
        ChangeRate changeRate = changeRateService.getChangeRateByIdAndOwner(dto.getIdChangeRate(), owner);
        BankAccount bankAccount = dto.getIdBankAccount() != null ? bankAccountService.getBankAccountByIdAndOwner(dto.getIdBankAccount(), owner) : null;
        Document parent = dto.getIdDocumentParent() == null ? null : documentRepository.findByOwnerCompanyAndIdDocument(owner, dto.getIdDocumentParent())
                .orElseThrow(DocumentNotFoundException::new);
        List<Order> orders = orderService.getOrdersByIdsAndOwner(dto.getOrderIds(), owner);


        if ("CUSTOMER_INVOICE".equals(dto.getDocType()) || "QUOTE".equals(dto.getDocType())) {
            Customer customer = null;
            // Obtener customer desde orders o un servicio
            customer = orderService.getCustomerByOrders(orders);//TODO Implementar este método. Tampoco entiendo porque enviar una lista entera de Orders. Todos los Orders deberían tener el mismo idCompany en este punto.
            // 2. Calcular campos adicionales

            if(customer!=null){
            notePayment=documentUtils.generateNotePayment(dto.getDocDate(), customer, bankAccount);
            }
            dto.setNotePayment(notePayment);
        }

        String currency = changeRate.getCurrency1();
        LocalDate deadline = documentUtils.calculateDeadline(dto.getDocDate(), dto.getDocType(), orders);//TODO Implementar este método. Tampoco entiendo estos parámetro. Se necesita el Cliente o Proveedor para conocer el plazo de pago.

        // 3. Crear entidad
        Document entity = documentMapper.toEntity(dto, changeRate, bankAccount, parent, orders);
        entity.setCurrency(currency);
        //entity.setNotePayment(notePayment);
        entity.setDeadline(deadline);
        entity.setOwnerCompany(owner);

        // 4. Guardar y actualizar pedidos si es factura
        documentRepository.save(entity);//TODO Y la Company?? Le tenemos que asociar la Company (la podemos encontrar el los Orders creo.
        if ("CUSTOMER_INVOICE".equals(dto.getDocType()) || "PROVIDER_INVOICE".equals(dto.getDocType())) {
            orderService.markOrdersAsBilled(orders);//TODO Implementar este método
        }

        return documentMapper.toDto(entity);
    }

}
