package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface ContactPersonRepository extends JpaRepository<ContactPerson, Integer> {
    List<ContactPerson> findByCompany_IdCompany(Integer idCompany);
    Optional<ContactPerson> findByIdContactPersonAndCompany_idCompany(Integer id, Integer idCompany);
}
