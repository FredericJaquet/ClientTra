package com.frederic.clienttra.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.frederic.clienttra.dto.create.CreateOrderRequestDTO;
import com.frederic.clienttra.dto.create.CreateProviderRequestDTO;
import com.frederic.clienttra.dto.create.CreateUserRequestDTO;
import com.frederic.clienttra.dto.demo.DemoCompanyDTO;
import com.frederic.clienttra.dto.demo.DemoDocumentDTO;
import com.frederic.clienttra.dto.demo.DemoOwnerCompanyDTO;
import com.frederic.clienttra.entities.*;
import com.frederic.clienttra.exceptions.CompanyNotFoundException;
import com.frederic.clienttra.mappers.*;
import com.frederic.clienttra.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DemoDataService {
    private final AddressRepository addressRepository;
    private final BankAccountRepository bankAccountRepository;
    private final ChangeRateRepository changeRateRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final DocumentRepository documentRepository;
    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;
    private final BankAccountMapper bankAccountMapper;
    private final ChangeRateMapper changeRateMapper;
    private final CustomerMapper customerMapper;
    private final ObjectMapper objectMapper;
    private final ProviderMapper providerMapper;
    private final UserMapper userMapper;
    private final CompanyService companyService;

    @Transactional
    public void loadData(CreateUserRequestDTO userDTO){
        try {
            Company demoCompany = loadDemoCompany(userDTO.getUsername());

            User user = userMapper.toEntity(userDTO, demoCompany);
            userRepository.save(user);

            loadDemoCustomers(demoCompany);
            loadDemoProviders(demoCompany);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Company loadDemoCompany(String userName) {
        // 2. Convertir JSON a DTO
        DemoOwnerCompanyDTO demoCompanyDTO = readJsonFromResource("db/demo_data/company.json", new TypeReference<>() {});

        // 3. Crear empresa con el DTO y devolver
        return createDemoCompany(demoCompanyDTO, userName);
    }

    private Company createDemoCompany(DemoOwnerCompanyDTO dto, String userName) {
        // 1. Crear entidad Company
        Company company = new Company();
        company.setVatNumber(dto.getVatNumber());
        company.setComName(dto.getComName() + " for " + userName);
        company.setLegalName(dto.getLegalName()+ " for "+ userName +" S.A.");
        company.setEmail(dto.getEmail());
        company.setWeb(dto.getWeb());

        Company savedCompany = companyRepository.save(company);

        // 2. Añadir direcciones
        List<Address> addresses = addressMapper.toEntities(dto.getAddresses(), savedCompany);
        List<Address> savedAddresses=addressRepository.saveAll(addresses);

        savedCompany.setAddresses(savedAddresses);

        // 3. Añadir cuentas bancarias
        List<BankAccount> accountEntities = bankAccountMapper.toEntities(dto.getBankAccounts(), savedCompany);
        List<BankAccount> savedAccounts = bankAccountRepository.saveAll(accountEntities);

        savedCompany.setBankAccounts(savedAccounts);

        //4. Añadir Tipos de cambio.
        List<ChangeRate> changeRateEntities = changeRateMapper.toEntities(dto.getChangeRates(), savedCompany);
        List<ChangeRate> savedChangeRate = changeRateRepository.saveAll(changeRateEntities);

        savedCompany.setChangeRates(savedChangeRate);

        return savedCompany;
    }

    private void loadDemoUsers(Company company){

            // 1. Convertir JSON a DTO
            List<CreateUserRequestDTO> dtos = readJsonFromResource("db/demo_data/users.json", new TypeReference<>() {});

            // 2. Crear usuarios con los DTOs
            List<User> entities = userMapper.toEntities(dtos, company);
            userRepository.saveAll(entities);
    }

    private void loadDemoCustomers(Company company){

        // 1. Convertir JSON a DTO
        List<DemoCompanyDTO> dtos = readJsonFromResource("db/demo_data/customers.json", new TypeReference<>() {});

        // 2. Crear Clientes con los DTOs
        List<Customer> entities = customerMapper.toEntities(dtos, company);
        List<Company> companyEntities = new ArrayList<>();

        //Guardar la Companies primero
        for(Customer entity:entities) {
            companyEntities.add(entity.getCompany());
        }

        companyRepository.saveAll(companyEntities);

        customerRepository.saveAll(entities);
    }

    private void loadDemoProviders(Company company){
        List<DemoCompanyDTO> dtos = readJsonFromResource("db/demo_data/providers.json", new TypeReference<>() {});

        List<Provider> entities = providerMapper.toEntities(dtos, company);
        List<Company> companyEntities = new ArrayList<>();

        for(Provider  entity:entities) {
            companyEntities.add(entity.getCompany());
        }

        companyRepository.saveAll(companyEntities);

        providerRepository.saveAll(entities);
    }

    @Transactional
    public void deleteDemoData() {
        Company ownerCompany = companyService.getCurrentCompanyOrThrow();

        List<Company> companies = companyRepository.findAllByOwnerCompany(ownerCompany);
        // 2. Romper relaciones ManyToMany entre documentos y órdenes
        for (Company company : companies) {
            for (Document doc : company.getDocuments()) {
                doc.getOrders().clear(); // ⚠️ Esto rompe la relación ManyToMany (tabla intermedia)
                documentRepository.save(doc);
            }
        }

        companyRepository.deleteAll(companies);
    }

    private <T> T readJsonFromResource(String path, TypeReference<T> typeRef) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + path);
            }

            // Configura el ObjectMapper
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

            return objectMapper.readValue(is, typeRef);
        } catch (Exception e) {
            throw new RuntimeException("Error reading JSON from " + path, e);
        }
    }

}
