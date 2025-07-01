package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Address;
import com.frederic.clienttra.entities.BankAccount;
import com.frederic.clienttra.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    Optional<BankAccount> findByIdBankAccountAndCompany(Integer id, Company company);
}
