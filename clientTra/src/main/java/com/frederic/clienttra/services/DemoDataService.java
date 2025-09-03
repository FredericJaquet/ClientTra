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
import java.util.List;

/**
 * Service class to load and delete demo data into/from the database.
 *
 * <p><b>Note:</b> If this functionality is kept in production,
 * appropriate unit and integration tests should be implemented.</p>
 */
@Service
@RequiredArgsConstructor
public class DemoDataService { // TODO: Add unit/integration tests if this function remains in production

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

    /**
     * Loads demo data for a new user including company, customers, and providers.
     *
     * @param userDTO the user creation data transfer object containing username
     */
    @Transactional
    public void loadData(CreateUserRequestDTO userDTO) {
        try {
            Company demoCompany = loadDemoCompany(userDTO.getUsername());

            if(userDTO.getIdRole()==null) {
                userDTO.setIdRole(1);
            }
            if(userDTO.getIdPlan()==null) {
                userDTO.setIdPlan(1);
            }
            User user = userMapper.toEntity(userDTO, demoCompany);
            user.setPreferredLanguage("es");
            user.setPreferredTheme("blue");
            user.setDarkMode(false);
            userRepository.save(user);

            loadDemoCustomers(demoCompany);
            loadDemoProviders(demoCompany);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a demo company from JSON resource and creates it in the database.
     *
     * @param userName the username to customize the demo company name
     * @return the persisted Company entity
     */
    private Company loadDemoCompany(String userName) {
        DemoOwnerCompanyDTO demoCompanyDTO = readJsonFromResource("db/demo_data/company.json", new TypeReference<>() {});

        return createDemoCompany(demoCompanyDTO, userName);
    }

    /**
     * Creates a demo company entity based on the provided DTO and username.
     *
     * @param dto      the demo company data transfer object
     * @param userName the username to append to company details
     * @return the persisted Company entity
     */
    private Company createDemoCompany(DemoOwnerCompanyDTO dto, String userName) {
        Company company = new Company();
        company.setVatNumber(dto.getVatNumber());
        company.setComName(dto.getComName() + " for " + userName);
        company.setLegalName(dto.getLegalName() + " for " + userName + " S.A.");
        company.setEmail(dto.getEmail());
        company.setWeb(dto.getWeb());

        Company savedCompany = companyRepository.save(company);

        List<Address> addresses = addressMapper.toEntities(dto.getAddresses(), savedCompany);
        List<Address> savedAddresses = addressRepository.saveAll(addresses);
        savedCompany.setAddresses(savedAddresses);

        List<BankAccount> accountEntities = bankAccountMapper.toEntities(dto.getBankAccounts(), savedCompany);
        List<BankAccount> savedAccounts = bankAccountRepository.saveAll(accountEntities);
        savedCompany.setBankAccounts(savedAccounts);

        List<ChangeRate> changeRateEntities = changeRateMapper.toEntities(dto.getChangeRates(), savedCompany);
        List<ChangeRate> savedChangeRate = changeRateRepository.saveAll(changeRateEntities);
        savedCompany.setChangeRates(savedChangeRate);

        return savedCompany;
    }

    /**
     * Loads demo users for a given company from JSON resource and saves them.
     *
     * @param company the company to associate the demo users with
     */
    private void loadDemoUsers(Company company) {
        List<CreateUserRequestDTO> dtos = readJsonFromResource("db/demo_data/users.json", new TypeReference<>() {});
        List<User> entities = userMapper.toEntities(dtos, company);
        userRepository.saveAll(entities);
    }

    /**
     * Loads demo customers from JSON resource and saves them along with their companies.
     *
     * @param company the owning company of the demo customers
     */
    private void loadDemoCustomers(Company company) {
        List<DemoCompanyDTO> dtos = readJsonFromResource("db/demo_data/customers.json", new TypeReference<>() {});

        List<Customer> entities = customerMapper.toEntities(dtos, company);
        List<Company> companyEntities = new ArrayList<>();

        for (Customer entity : entities) {
            companyEntities.add(entity.getCompany());
        }

        companyRepository.saveAll(companyEntities);
        customerRepository.saveAll(entities);
    }

    /**
     * Loads demo providers from JSON resource and saves them along with their companies.
     *
     * @param company the owning company of the demo providers
     */
    private void loadDemoProviders(Company company) {
        List<DemoCompanyDTO> dtos = readJsonFromResource("db/demo_data/providers.json", new TypeReference<>() {});

        List<Provider> entities = providerMapper.toEntities(dtos, company);
        List<Company> companyEntities = new ArrayList<>();

        for (Provider entity : entities) {
            companyEntities.add(entity.getCompany());
        }

        companyRepository.saveAll(companyEntities);
        providerRepository.saveAll(entities);
    }

    /**
     * Deletes all demo data associated with the current user's company.
     * This includes breaking many-to-many relationships between documents and orders.
     */
    @Transactional
    public void deleteDemoData() {
        Company ownerCompany = companyService.getCurrentCompanyOrThrow();

        List<Company> companies = companyRepository.findAllByOwnerCompany(ownerCompany);

        for (Company company : companies) {
            for (Document doc : company.getDocuments()) {
                doc.getOrders().clear(); // Breaks ManyToMany relationship (join table)
                documentRepository.save(doc);
            }
        }

        companyRepository.deleteAll(companies);
    }

    /**
     * Reads a JSON file from resources and deserializes it into the specified type.
     *
     * @param <T>     the type of the returned object
     * @param path    the resource path to the JSON file
     * @param typeRef the Jackson TypeReference representing the target type
     * @return deserialized object from JSON
     * @throws RuntimeException if resource is not found or reading/parsing fails
     */
    private <T> T readJsonFromResource(String path, TypeReference<T> typeRef) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + path);
            }

            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

            return objectMapper.readValue(is, typeRef);
        } catch (Exception e) {
            throw new RuntimeException("Error reading JSON from " + path, e);
        }
    }
}
