package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.*;
import com.frederic.clienttra.dto.read.*;
import com.frederic.clienttra.dto.update.*;
import com.frederic.clienttra.entities.*;
import com.frederic.clienttra.exceptions.CustomerNotFoundException;
import com.frederic.clienttra.mappers.*;
import com.frederic.clienttra.repositories.*;
import com.frederic.clienttra.utils.validators.DtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {//Me falta los esquemas aquí!!

    private final AddressRepository addressRepository;
    private final PhoneRepository phoneRepository;
    private final BankAccountRepository bankAccountRepository;
    private final ContactPersonRepository contactPersonRepository;
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final CustomerMapper customerMapper;
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;
    private final PhoneMapper phoneMapper;
    private final AddressMapper addressMapper;
    private final BankAccountMapper bankAccountMapper;
    private final ContactPersonMapper contactPersonMapper;
    private final DtoValidator dtoValidator;

    @Override
    public List<CustomersForListDTO> getAllCustomers() {
        Company owner = companyService.getCurrentCompanyOrThrow();

        return customerRepository.findListByOwnerCompany(owner).stream()
                .map(p -> CustomersForListDTO.builder()
                        .idCustomer(p.getIdCustomer())
                        .comName(p.getComName())
                        .vatNumber(p.getVatNumber())
                        .email(p.getEmail())
                        .web(p.getWeb())
                        .enabled(p.getEnabled())
                        .build())
                .toList();
    }

    @Override
    public CustomerDetailsDTO getCustomerById(int id) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Customer customer = customerRepository.findByOwnerCompanyAndIdCustomer(owner, id)
                .orElseThrow(CustomerNotFoundException::new);
        return customerMapper.toCustomerDetailsDTO(customer);
    }

    @Override
    public void createCustomer(CreateCustomerRequestDTO dto) {//TODO Definir NewXXXDTO y comprobar validaciones en UpdateXXXDTO
        Company owner = companyService.getCurrentCompanyOrThrow();

        Company companyEntity = companyMapper.toEntity(dto);
        companyEntity.setOwnerCompany(owner);

        Company savedCompany = companyRepository.save(companyEntity);

        Customer customer = customerMapper.toEntity(dto);
        customer.setCompany(savedCompany);
        customer.setOwnerCompany(owner);
        customer.setEnabled(true);

        customerRepository.save(customer);
    }

    @Override
    public void updateCustomer(int id, UpdateCustomerRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Customer customer = customerRepository.findByOwnerCompanyAndIdCustomer(owner,id)
                .orElseThrow(CustomerNotFoundException::new);

        Company company = customer.getCompany();

        companyMapper.updateEntity(company,dto);

        /*updatePhones(dto.getPhones(), company);
        updateAddresses(dto.getAddresses(), company);
        updateBankAccounts(dto.getBankAccounts(), company);
        updateContactPersons(dto.getContactPersons(), company);*/

        customerMapper.updateEntity(customer, dto);

        companyRepository.save(company);
        customerRepository.save(customer);
    }

    @Override
    public void disableCustomer(int id) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Customer customer = customerRepository.findByOwnerCompanyAndIdCustomer(owner,id)
                .orElseThrow(CustomerNotFoundException::new);
        customer.setEnabled(false);
        customerRepository.save(customer);
    }

    @Override
    public List<CustomersForListDTO> searchByNameOrVat(String input) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        String query = "%" + input + "%";
        return customerRepository.findListByComNameOrLegalNameOrVatNumber(owner, query).stream()
                .map(p -> CustomersForListDTO.builder()
                        .idCustomer(p.getIdCustomer())
                        .comName(p.getComName())
                        .vatNumber(p.getVatNumber())
                        .email(p.getEmail())
                        .web(p.getWeb())
                        .enabled(p.getEnabled())
                        .build())
                .toList();
    }

    /*private void updatePhones(List<UpdatePhoneRequestDTO> dtos, Company company) {
        updateCollection(
                dtos,
                company.getPhones(),
                phoneMapper::toEntity,
                phoneMapper::updateEntity,
                company::addPhone,
                (entity, dto) -> entity.getIdPhone() != null && entity.getIdPhone().equals(dto.getIdPhone()),
                dto -> dto.getIdPhone() != null,
                (dto, entity) -> {
                    Phone fullEntity = phoneRepository.findByIdPhoneAndCompany(dto.getIdPhone(), company)
                            .orElseThrow(PhoneNotFoundException::new);
                    CreatePhoneRequestDTO createDto = phoneMapper.toCreatePhoneRequestDTO(dto, entity);
                    dtoValidator.validate(createDto);
                }
        );
    }

    private void updateAddresses(List<UpdateAddressRequestDTO> dtos, Company company) {
        updateCollection(
                dtos,
                company.getAddresses(),
                addressMapper::toEntity,
                addressMapper::updateEntity,
                company::addAddress,
                (entity, dto) -> entity.getIdAddress() != null && entity.getIdAddress().equals(dto.getIdAddress()),
                dto -> dto.getIdAddress() != null,
                (dto, entity) -> {
                    Address fullEntity = addressRepository.findByIdAddressAndCompany(dto.getIdAddress(), company)
                            .orElseThrow(AddressNotFoundException::new);
                    CreateAddressRequestDTO createDto = addressMapper.toCreateAddressRequestDTO(dto, fullEntity);
                    dtoValidator.validate(createDto);
                }
        );
    }

    private void updateBankAccounts(List<UpdateBankAccountRequestDTO> dtos, Company company) {
        updateCollection(
                dtos,
                company.getBankAccounts(),
                bankAccountMapper::toEntity,
                bankAccountMapper::updateEntity,
                company::addBankAccount,
                (entity, dto) -> entity.getIdBankAccount() != null && entity.getIdBankAccount().equals(dto.getIdBankAccount()),
                dto -> dto.getIdBankAccount() != null,
                (dto, entity) -> {
                    BankAccount fullEntity = bankAccountRepository.findByIdBankAccountAndCompany(dto.getIdBankAccount(), company)
                            .orElseThrow(BankAccountNotFoundException::new);
                    CreateBankAccountRequestDTO createDto = bankAccountMapper.toCreateBankAccountRequestDTO(dto, entity);
                    dtoValidator.validate(createDto);
                }
        );
    }

    private void updateContactPersons(List<UpdateContactPersonRequestDTO> dtos, Company company) {
        updateCollection(
                dtos,
                company.getContactPersons(),
                contactPersonMapper::toEntity,
                contactPersonMapper::updateEntity,
                company::addContactPerson,
                (entity, dto) -> entity.getIdContactPerson() != null && entity.getIdContactPerson().equals(dto.getIdContactPerson()),
                dto -> dto.getIdContactPerson() != null,
                (dto, entity) -> {
                    ContactPerson fullEntity = contactPersonRepository.findByIdContactPersonAndCompany(dto.getIdContactPerson(), company)
                            .orElseThrow(ContactPersonNotFoundException::new);
                    CreateContactPersonRequestDTO createDto = contactPersonMapper.toCreateContactPersonRequestDTO(dto, entity);
                    dtoValidator.validate(createDto);
                }
        );
    }

    private <E, D> void updateCollection(
            List<D> dtos,
            List<E> currentEntities,
            Function<D, E> dtoToEntityMapper,
            BiConsumer<E, D> updateEntityWithDto,
            Consumer<E> addEntityToParent,
            BiPredicate<E, D> entityMatchesDto,
            Predicate<D> hasId,
            BiConsumer<D, E> validateDtoWithEntity
    ) {
        if (dtos != null) {
            currentEntities.removeIf(entity ->
                    dtos.stream().noneMatch(dto -> entityMatchesDto.test(entity, dto))
            );

            for (D dto : dtos) {
                if (!hasId.test(dto)) {
                    System.out.println("CustomerServiceImpl linea 246: Entra aquí");
                    E newEntity = dtoToEntityMapper.apply(dto);
                    validateDtoWithEntity.accept(dto, newEntity);
                    addEntityToParent.accept(newEntity);
                } else {
                    currentEntities.stream()
                            .filter(entity -> entityMatchesDto.test(entity, dto))
                            .findFirst()
                            .ifPresent(entity -> {
                                validateDtoWithEntity.accept(dto, entity);
                                updateEntityWithDto.accept(entity, dto);
                            });
                }
            }
        }
    }*/

}


