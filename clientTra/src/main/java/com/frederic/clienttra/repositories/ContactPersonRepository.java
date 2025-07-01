package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Address;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactPersonRepository extends JpaRepository<ContactPerson, Integer> {
    Optional<ContactPerson> findByIdContactPersonAndCompany(Integer id, Company company);
}
