package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Integer> {
    List<Phone> findByCompany_IdCompany(Integer idCompany);
    Optional<Phone> findByIdPhoneAndCompany_idCompany(Integer id, Integer idCompany);
}
