package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.create.CreateProviderRequestDTO;
import com.frederic.clienttra.dto.demo.DemoCompanyDTO;
import com.frederic.clienttra.dto.read.BaseCompanyMinimalDTO;
import com.frederic.clienttra.dto.read.ProviderDetailsDTO;
import com.frederic.clienttra.dto.read.ProviderForListDTO;
import com.frederic.clienttra.dto.update.UpdateProviderRequestDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Provider;
import com.frederic.clienttra.projections.ProviderListProjection;
import com.frederic.clienttra.projections.ProviderMinimalProjection;
import com.frederic.clienttra.repositories.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Provider entities, DTOs and projections.
 */
@Component
@RequiredArgsConstructor
public class ProviderMapper {

    private final AddressMapper addressMapper;
    private final CompanyMapper companyMapper;
    private final PhoneMapper phoneMapper;
    private final BankAccountMapper bankAccountMapper;
    private final ContactPersonMapper contactPersonMapper;
    private final SchemeMapper schemeMapper;
    private final CompanyRepository companyRepository;

    /**
     * Converts a list of ProviderListProjection to a list of ProviderForListDTO.
     *
     * @param entities list of ProviderListProjection
     * @return list of ProviderForListDTO
     */
    public List<ProviderForListDTO> toProviderForListDTOS(List<ProviderListProjection> entities){
        return entities.stream()
                .map(p -> ProviderForListDTO.builder()
                        .idProvider(p.getIdProvider())
                        .comName(p.getComName())
                        .vatNumber(p.getVatNumber())
                        .email(p.getEmail())
                        .web(p.getWeb())
                        .enabled(p.getEnabled())
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ProviderForListDTO toProviderForListDTO(Provider entity){
        return ProviderForListDTO.builder()
                .idProvider(entity.getIdProvider())
                .comName(entity.getCompany().getComName())
                .vatNumber(entity.getCompany().getVatNumber())
                .email(entity.getCompany().getEmail())
                .web(entity.getCompany().getWeb())
                .enabled(entity.getEnabled())
                .build();
    }

    /**
     * Converts a Provider entity to ProviderDetailsDTO, including nested related data.
     *
     * @param entity the Provider entity
     * @return the detailed Provider DTO
     */
    public ProviderDetailsDTO toProviderDetailsDTO(Provider entity){
        return ProviderDetailsDTO.builder()
                .idCompany(entity.getCompany().getIdCompany())
                .idProvider(entity.getIdProvider())
                .defaultLanguage(entity.getDefaultLanguage())
                .defaultVat(entity.getDefaultVat())
                .defaultWithholding(entity.getDefaultWithholding())
                .duedate(entity.getDuedate())
                .europe(entity.getEurope())
                .enable(entity.getEnabled())
                .vatNumber(entity.getCompany().getVatNumber())
                .comName(entity.getCompany().getComName())
                .legalName(entity.getCompany().getLegalName())
                .web(entity.getCompany().getWeb())
                .email(entity.getCompany().getEmail())
                .addresses(safeMapToDTO(entity.getCompany().getAddresses(), addressMapper::toAddressDTO))
                .phones(safeMapToDTO(entity.getCompany().getPhones(), phoneMapper::toPhoneDTO))
                .bankAccounts(safeMapToDTO(entity.getCompany().getBankAccounts(), bankAccountMapper::toBankAccountDTO))
                .contactPersons(safeMapToDTO(entity.getCompany().getContactPersons(), contactPersonMapper::toContactPersonDTO))
                .schemes(safeMapToDTO(entity.getCompany().getSchemes(), schemeMapper::toDto))
                .build();
    }

    /**
     * Converts a CreateProviderRequestDTO to a Provider entity.
     *
     * @param dto the create provider request DTO
     * @return the new Provider entity
     */
    public Provider toEntity(CreateProviderRequestDTO dto){
        return Provider.builder()
                .duedate(dto.getDuedate())
                .defaultLanguage(dto.getDefaultLanguage())
                .defaultVat(dto.getDefaultVat())
                .defaultWithholding(dto.getDefaultWithholding())
                .europe(dto.getEurope())
                .enabled(true)
                .build();
    }

    /**
     * Converts a DemoCompanyDTO to a Provider entity including saving the associated Company entity.
     *
     * @param dto the demo company DTO
     * @param ownerCompany the owning company
     * @return the new Provider entity with persisted Company
     */
    public Provider toEntity(DemoCompanyDTO dto, Company ownerCompany){
        Company company = companyMapper.toEntity(dto,ownerCompany);
        Company savedCompany =  companyRepository.save(company);

        return Provider.builder()
                .duedate(dto.getDuedate())
                .defaultLanguage(dto.getDefaultLanguage())
                .defaultVat(dto.getDefaultVAT())
                .defaultWithholding(dto.getDefaultWithholding())
                .europe(dto.getEurope())
                .enabled(true)
                .company(savedCompany)
                .ownerCompany(ownerCompany)
                .build();
    }

    /**
     * Converts a list of DemoCompanyDTOs to a list of Provider entities.
     *
     * @param dtos list of demo company DTOs
     * @param ownerCompany the owning company
     * @return list of Provider entities
     */
    public List<Provider> toEntities(List<DemoCompanyDTO> dtos, Company ownerCompany){
        return dtos.stream()
                .map(dto -> toEntity(dto, ownerCompany))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Updates a Provider entity using values from UpdateProviderRequestDTO.
     * Only non-null fields are updated.
     *
     * @param entity the Provider entity to update
     * @param dto the update provider request DTO
     */
    public void updateEntity(Provider entity, UpdateProviderRequestDTO dto){
        if (dto.getDuedate() != null) {
            entity.setDuedate(dto.getDuedate());
        }
        if (dto.getDefaultLanguage() != null) {
            entity.setDefaultLanguage(dto.getDefaultLanguage());
        }
        if (dto.getDefaultVat() != null) {
            entity.setDefaultVat(dto.getDefaultVat());
        }
        if (dto.getDefaultWithholding() != null) {
            entity.setDefaultWithholding(dto.getDefaultWithholding());
        }
        if (dto.getEurope() != null) {
            entity.setEurope(dto.getEurope());
        }
        if (dto.getEnabled() != null) {
            entity.setEnabled(dto.getEnabled());
        }
    }

    /**
     * Converts a ProviderMinimalProjection to a minimal BaseCompanyMinimalDTO.
     *
     * @param entity the ProviderMinimalProjection
     * @return the minimal company DTO
     */
    public BaseCompanyMinimalDTO toMinimalDTO(ProviderMinimalProjection entity){
        return BaseCompanyMinimalDTO.builder()
                .idCompany(entity.getIdCompany())
                .comName(entity.getComName())
                .vatNumber(entity.getVatNumber())
                .build();
    }

    /**
     * Converts a list of ProviderMinimalProjection to a list of minimal BaseCompanyMinimalDTOs.
     *
     * @param entities list of ProviderMinimalProjection
     * @return list of minimal company DTOs
     */
    public List<BaseCompanyMinimalDTO> toMinimalDTOs(List<ProviderMinimalProjection> entities){
        return entities.stream()
                .map(this::toMinimalDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Helper method to safely map lists that may be null to DTOs.
     *
     * @param list the source list (may be null)
     * @param mapper function to map each element
     * @param <T> source type
     * @param <R> target type
     * @return list of mapped DTOs or empty list if source is null
     */
    private <T, R> List<R> safeMapToDTO(List<T> list, Function<T, R> mapper) {
        return list == null ? List.of() :
                list.stream()
                        .filter(Objects::nonNull)
                        .map(mapper)
                        .collect(Collectors.toCollection(ArrayList::new));
    }

}
