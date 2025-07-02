package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Address;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactPersonRepository extends JpaRepository<ContactPerson, Integer> {
    List<ContactPerson> findByCompany_IdCompany(Integer idCompany);
    Optional<ContactPerson> findByIdContactPersonAndCompany_idCompany(Integer id, Integer idCompany);
}
