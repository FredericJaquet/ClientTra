package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.read.CompanyOwnerDTO;
import com.frederic.clienttra.dto.update.UpdateCompanyOwnerRequestDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.exceptions.CompanyNotFoundException;
import com.frederic.clienttra.exceptions.CompanyNotFoundForUserException;
import com.frederic.clienttra.exceptions.LogoNotLoadedException;
import com.frederic.clienttra.mappers.CompanyMapper;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Transactional(readOnly = true)
    @Override
    public Optional<Company> getCurrentCompany() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            int currentCompanyId = userDetails.getIdCompany();
            return companyRepository.findById(currentCompanyId);
        }

        return Optional.empty();
    }

    @Transactional(readOnly = true)
    @Override
    public Company getCurrentCompanyOrThrow() {
        return getCurrentCompany().orElseThrow(CompanyNotFoundForUserException::new);
    }

    @Transactional(readOnly = true)
    public Optional<CompanyOwnerDTO> getCompanyOwnerDTO(){
        Company company=getCurrentCompanyOrThrow();
        CompanyOwnerDTO companyOwnerDTO=companyMapper.toCompanyOwnerDTO(company);
        return Optional.of(companyOwnerDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Company getCompanyById(Integer id){
        return companyRepository.findByIdCompany(id)
                .orElseThrow(CompanyNotFoundException::new);
    }

    @Transactional
    @Override
    public void updateCompanyOwner(UpdateCompanyOwnerRequestDTO dto) {
        Company company = getCurrentCompanyOrThrow();

        //TODO Convertir en CreateCustomerRequestDTO y pasar el DtoValidator

        companyMapper.updateEntity(company,dto);

        companyRepository.save(company);
    }

    @Transactional
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

}
