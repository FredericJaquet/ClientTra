package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Phone} entities.
 * <p>
 * Extends JpaRepository to provide CRUD operations.
 * Includes methods to find phones by company and by phone ID scoped to a company.
 */
public interface PhoneRepository extends JpaRepository<Phone, Integer> {

    /**
     * Finds all phones associated with a given company ID.
     *
     * @param idCompany the ID of the company
     * @return list of phones for the specified company
     */
    List<Phone> findByCompany_IdCompany(Integer idCompany);

    /**
     * Finds a phone by its ID and the ID of the company it belongs to.
     * Useful to ensure the phone belongs to the specified company.
     *
     * @param id the phone ID
     * @param idCompany the company ID
     * @return optional containing the phone if found
     */
    Optional<Phone> findByIdPhoneAndCompany_idCompany(Integer id, Integer idCompany);
}
