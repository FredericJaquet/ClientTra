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

    public ProviderDetailsDTO toProviderDetailsDTO(Provider entity){
        return ProviderDetailsDTO.builder()
                .idProvider(entity.getIdProvider())
                .defaultLanguage(entity.getDefaultLanguage())
                .defaultVAT(entity.getDefaultVat())
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

    public Provider toEntity(CreateProviderRequestDTO dto){
        return Provider.builder()
                .duedate(dto.getDuedate())
                .defaultLanguage(dto.getDefaultLanguage())
                .defaultVat(dto.getDefaultVAT())
                .defaultWithholding(dto.getDefaultWithholding())
                .europe(dto.getEurope())
                .enabled(true)
                .build();
    }

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

    public List<Provider> toEntities(List<DemoCompanyDTO> dtos, Company ownerCompany){
        return dtos.stream()
                .map(dto -> toEntity(dto, ownerCompany))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void updateEntity(Provider entity, UpdateProviderRequestDTO dto){
        if (dto.getDuedate() != null) {
            entity.setDuedate(dto.getDuedate());
        }
        if (dto.getDefaultLanguage() != null) {
            entity.setDefaultLanguage(dto.getDefaultLanguage());
        }
        if (dto.getDefaultVAT() != null) {
            entity.setDefaultVat(dto.getDefaultVAT());
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

    public BaseCompanyMinimalDTO toMinimalDTO(ProviderMinimalProjection entity){
        return BaseCompanyMinimalDTO.builder()
                .idCompany(entity.getIdCompany())
                .comName(entity.getComName())
                .vatNumber(entity.getVatNumber())
                .build();
    }

    public List<BaseCompanyMinimalDTO> toMinimalDTOs(List<ProviderMinimalProjection> entities){
        return entities.stream()
                .map(this::toMinimalDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private <T, R> List<R> safeMapToDTO(List<T> list, Function<T, R> mapper) {
        return list == null ? List.of() :
                list.stream()
                        .filter(Objects::nonNull)
                        .map(mapper)
                        .collect(Collectors.toCollection(ArrayList::new));
    }

}
