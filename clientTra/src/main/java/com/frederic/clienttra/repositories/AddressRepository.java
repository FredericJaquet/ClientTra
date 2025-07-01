package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Address;
import com.frederic.clienttra.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findByCompany_IdCompany(Integer idCompany);
    Optional<Address> findByIdAddressAndCompany_idCompany(Integer id, Integer idCompany);
}
