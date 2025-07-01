package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateAddressRequestDTO;
import com.frederic.clienttra.dto.create.CreateBankAccountRequestDTO;
import com.frederic.clienttra.dto.create.CreatePhoneRequestDTO;
import com.frederic.clienttra.dto.read.CompanyOwnerDTO;
import com.frederic.clienttra.dto.update.UpdateAddressRequestDTO;
import com.frederic.clienttra.dto.update.UpdateBankAccountRequestDTO;
import com.frederic.clienttra.dto.update.UpdateCompanyOwnerRequestDTO;
import com.frederic.clienttra.dto.update.UpdatePhoneRequestDTO;
import com.frederic.clienttra.entities.Address;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.exceptions.AddressNotFoundException;
import com.frederic.clienttra.exceptions.CompanyNotFoundForUserException;
import com.frederic.clienttra.exceptions.LogoNotLoadedException;
import com.frederic.clienttra.mappers.AddressMapper;
import com.frederic.clienttra.mappers.BankAccountMapper;
import com.frederic.clienttra.mappers.CompanyMapper;
import com.frederic.clienttra.mappers.PhoneMapper;
import com.frederic.clienttra.repositories.AddressRepository;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.security.CustomUserDetails;
import com.frederic.clienttra.utils.validators.DtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.*;

@Service
@RequiredArgsConstructor
public class CompanyOwnerServiceImpl implements CompanyOwnerService {

    private final AddressRepository addressRepository;
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final PhoneMapper phoneMapper;
    private final BankAccountMapper bankAccountMapper;
    private final AddressMapper addressMapper;
    private final DtoValidator dtoValidator;

    @Override
    public Optional<Company> getCurrentCompany() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            int currentCompanyId = userDetails.getIdCompany();
            return companyRepository.findById(currentCompanyId);
        }

        return Optional.empty();
    }

    @Override
    public Company getCurrentCompanyOrThrow() {
        return getCurrentCompany().orElseThrow(CompanyNotFoundForUserException::new);
    }

    public Optional<CompanyOwnerDTO> getCompanyOwnerDTO(){
        Company company=getCurrentCompanyOrThrow();
        CompanyOwnerDTO companyOwnerDTO=companyMapper.toCompanyOwnerDTO(company);
        return Optional.of(companyOwnerDTO);
    }

    @Override
    public void updateCompanyOwner(UpdateCompanyOwnerRequestDTO dto) {
        Company company = getCurrentCompanyOrThrow();

        companyMapper.updateEntity(company,dto);

        updatePhones(dto.getPhones(), company);
        updateAddresses(dto.getAddresses(), company);
        updateBankAccounts(dto.getBankAccounts(), company);

        companyRepository.save(company);
    }

    @Override
    public void uploadCompanyLogo(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Archivo vacío");
        }

        Company company = getCurrentCompanyOrThrow();

        try {
            String filename = "logo_" + company.getIdCompany() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path destinationPath = Paths.get("uploads/logos", filename).toAbsolutePath().normalize();

            Files.createDirectories(destinationPath.getParent());

            Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

            // Guardar solo el path relativo
            company.setLogoPath("uploads/logos/" + filename);
            companyRepository.save(company);

        } catch (IOException e) {
            throw new LogoNotLoadedException();
        }
    }

    private void updatePhones(List<UpdatePhoneRequestDTO> dtos, Company company) {
        updateCollection(
                dtos,
                company.getPhones(),
                phoneMapper::toEntity,
                phoneMapper::updateEntity,
                company::addPhone,
                (entity, dto) -> entity.getIdPhone() != null && entity.getIdPhone().equals(dto.getIdPhone()),
                dto -> dto.getIdPhone() != null,
                (dto, entity) -> {
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
                    CreateAddressRequestDTO createDto = addressMapper.toCreateAddressRequestDTO(dto, entity);
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
                    CreateBankAccountRequestDTO createDto = bankAccountMapper.toCreateBankAccountRequestDTO(dto, entity);
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
    }

}
