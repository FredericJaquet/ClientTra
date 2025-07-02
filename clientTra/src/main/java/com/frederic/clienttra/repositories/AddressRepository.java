package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findByCompany_IdCompany(Integer idCompany);
    Optional<Address> findByIdAddressAndCompany_idCompany(Integer idAddress, Integer idCompany);
    Integer countByCompany_IdCompany(Integer idCompany);

}
