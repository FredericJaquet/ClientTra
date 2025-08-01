package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing {@link Address} entities.
 * <p>
 * Provides methods to retrieve and count addresses associated with a specific company.
 */
public interface AddressRepository extends JpaRepository<Address, Integer> {

    /**
     * Retrieves all addresses for a given company.
     *
     * @param idCompany the ID of the company
     * @return list of addresses
     */
    List<Address> findByCompany_IdCompany(Integer idCompany);

    /**
     * Retrieves a specific address by its ID and associated company.
     *
     * @param idAddress the ID of the address
     * @param idCompany the ID of the company
     * @return optional containing the address if found
     */
    Optional<Address> findByIdAddressAndCompany_idCompany(Integer idAddress, Integer idCompany);

    /**
     * Counts how many addresses are associated with a specific company.
     *
     * @param idCompany the ID of the company
     * @return the number of addresses
     */
    Integer countByCompany_IdCompany(Integer idCompany);
}
