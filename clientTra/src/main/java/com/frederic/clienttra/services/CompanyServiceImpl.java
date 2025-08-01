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

/**
 * Implementation of {@link CompanyService} interface
 * that manages operations related to the authenticated user's company.
 */
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    /**
     * Retrieves the current authenticated user's company, if available.
     *
     * @return an {@link Optional} containing the current {@link Company} or empty if not authenticated or company not found
     */
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

    /**
     * Retrieves the current authenticated user's company.
     *
     * @return the current {@link Company}
     * @throws CompanyNotFoundForUserException if no company is found for the authenticated user
     */
    @Transactional(readOnly = true)
    @Override
    public Company getCurrentCompanyOrThrow() {
        return getCurrentCompany().orElseThrow(CompanyNotFoundForUserException::new);
    }

    /**
     * Retrieves the current authenticated user's company information
     * mapped to a {@link CompanyOwnerDTO}.
     *
     * @return an {@link Optional} containing the {@link CompanyOwnerDTO} or empty if no company found
     */
    @Transactional(readOnly = true)
    public Optional<CompanyOwnerDTO> getCompanyOwnerDTO() {
        Company company = getCurrentCompanyOrThrow();
        CompanyOwnerDTO companyOwnerDTO = companyMapper.toCompanyOwnerDTO(company);
        return Optional.of(companyOwnerDTO);
    }

    /**
     * Retrieves a company by its ID.
     *
     * @param id the ID of the company to retrieve
     * @return the {@link Company} with the specified ID
     * @throws CompanyNotFoundException if no company is found with the given ID
     */
    @Transactional(readOnly = true)
    @Override
    public Company getCompanyById(Integer id) {
        return companyRepository.findByIdCompany(id)
                .orElseThrow(CompanyNotFoundException::new);
    }

    /**
     * Updates the owner information of the current authenticated user's company.
     *
     * @param dto the DTO containing updated company owner information
     */
    @Transactional
    @Override
    public void updateCompanyOwner(UpdateCompanyOwnerRequestDTO dto) {
        Company company = getCurrentCompanyOrThrow();

        // TODO: Convert to CreateCustomerRequestDTO and validate using DtoValidator

        companyMapper.updateEntity(company, dto);

        companyRepository.save(company);
    }

    /**
     * Uploads and stores a logo file for the current authenticated user's company.
     * The logo is saved in a predefined folder with a unique filename,
     * and the relative path is stored in the company entity.
     *
     * @param file the logo file to upload
     * @throws IllegalArgumentException if the file is null or empty
     * @throws LogoNotLoadedException if an IOException occurs during file storage
     */
    @Transactional
    @Override
    public void uploadCompanyLogo(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Empty file");
        }

        Company company = getCurrentCompanyOrThrow();

        try {
            String filename = "logo_" + company.getIdCompany() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path destinationPath = Paths.get("uploads/logos", filename).toAbsolutePath().normalize();

            Files.createDirectories(destinationPath.getParent());

            Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

            // Save only the relative path
            company.setLogoPath("uploads/logos/" + filename);
            companyRepository.save(company);

        } catch (IOException e) {
            throw new LogoNotLoadedException();
        }
    }
}
