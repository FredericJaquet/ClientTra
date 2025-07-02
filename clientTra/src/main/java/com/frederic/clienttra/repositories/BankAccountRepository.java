package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    List<BankAccount> findByCompany_IdCompany(Integer idCompany);
    Optional<BankAccount> findByIdBankAccountAndCompany_idCompany(Integer id, Integer idCompany);
}
