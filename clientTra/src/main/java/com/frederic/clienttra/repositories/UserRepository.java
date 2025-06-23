package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);
    List<User> findAllByCompany_IdCompany(int idCompany);
    Optional<User> findByIdUserAndCompany_IdCompany(int idUser, int idCompany);
}
