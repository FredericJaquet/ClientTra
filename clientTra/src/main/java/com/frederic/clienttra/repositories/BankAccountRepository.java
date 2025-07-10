package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.BankAccount;
import com.frederic.clienttra.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    List<BankAccount> findByCompany_IdCompany(Integer idCompany);
    Optional<BankAccount> findByIdBankAccountAndCompany_idCompany(Integer id, Integer idCompany);
}
