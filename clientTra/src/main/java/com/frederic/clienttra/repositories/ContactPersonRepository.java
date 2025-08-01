package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link ContactPerson} entities.
 * <p>
 * Provides methods to retrieve and query contact persons associated with companies.
 */
public interface ContactPersonRepository extends JpaRepository<ContactPerson, Integer> {

    /**
     * Retrieves all contact persons associated with a specific company by its ID.
     *
     * @param idCompany the ID of the company
     * @return list of contact persons linked to the company
     */
    List<ContactPerson> findByCompany_IdCompany(Integer idCompany);

    /**
     * Retrieves a contact person by its ID and the associated company's ID.
     *
     * @param id the ID of the contact person
     * @param idCompany the ID of the company
     * @return an optional containing the contact person if found
     */
    Optional<ContactPerson> findByIdContactPersonAndCompany_idCompany(Integer id, Integer idCompany);
}
