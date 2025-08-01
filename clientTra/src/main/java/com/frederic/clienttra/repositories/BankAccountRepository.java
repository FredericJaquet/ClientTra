package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link BankAccount} entities.
 * <p>
 * Provides methods to retrieve bank accounts associated with a specific company.
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

    /**
     * Retrieves all bank accounts associated with a given company.
     *
     * @param idCompany the ID of the company
     * @return list of bank accounts
     */
    List<BankAccount> findByCompany_IdCompany(Integer idCompany);

    /**
     * Retrieves a specific bank account by its ID and associated company ID.
     *
     * @param id the ID of the bank account
     * @param idCompany the ID of the company
     * @return optional containing the bank account if found
     */
    Optional<BankAccount> findByIdBankAccountAndCompany_idCompany(Integer id, Integer idCompany);
}
