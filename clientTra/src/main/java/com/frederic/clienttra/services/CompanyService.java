package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.update.UpdateCompanyOwnerRequestDTO;
import com.frederic.clienttra.entities.Company;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Service interface for managing company-related operations.
 */
public interface CompanyService {

    /**
     * Retrieves the current authenticated user's company, if available.
     *
     * @return an {@link Optional} containing the current {@link Company} or empty if not found
     */
    Optional<Company> getCurrentCompany();

    /**
     * Retrieves the current authenticated user's company.
     *
     * @return the current {@link Company}
     * @throws RuntimeException if the current company is not found or not authenticated
     */
    Company getCurrentCompanyOrThrow();

    /**
     * Retrieves a company by its ID.
     *
     * @param idCompany the ID of the company
     * @return the {@link Company} with the specified ID
     * @throws RuntimeException if the company with the given ID does not exist
     */
    Company getCompanyById(Integer idCompany);

    /**
     * Updates the owner information of a company.
     *
     * @param dto the data transfer object containing the updated owner information
     */
    void updateCompanyOwner(UpdateCompanyOwnerRequestDTO dto);

    /**
     * Uploads and associates a logo image file with the current company.
     *
     * @param file the {@link MultipartFile} representing the logo image to upload
     */
    void uploadCompanyLogo(MultipartFile file);

}
