package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);
    List<User> findAllByCompany_IdCompany(int idCompany);
    Optional<User> findByIdUserAndCompany_IdCompany(int idUser, int idCompany);
    int countByCompany_IdCompanyAndRole_RoleName(int idCompany, String roleName);
    List<User> findAllByCompany_IdCompanyAndEnabledTrue(int idCompany);

}
