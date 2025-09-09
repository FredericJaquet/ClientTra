package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 * <p>
 * Extends JpaRepository to provide basic CRUD operations and defines
 * custom query methods to find users by username, company, role, and status.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their username.
     *
     * @param userName the username to search for
     * @return an Optional containing the user if found, or empty otherwise
     */
    Optional<User> findByUserName(String userName);

    /**
     * Retrieves all users belonging to a specific company.
     *
     * @param idCompany the ID of the company
     * @return list of users belonging to the company
     */
    List<User> findAllByCompany_IdCompanyAndIdUserNot(int idCompany, int idUser);

    /**
     * Finds a user by their ID and company ID.
     *
     * @param idUser    the ID of the user
     * @param idCompany the ID of the company
     * @return an Optional containing the user if found, or empty otherwise
     */
    Optional<User> findByIdUserAndCompany_IdCompany(int idUser, int idCompany);

    boolean existsByUserName(@NotBlank String UserName);

    /**
     * Counts the number of users in a company that have a specific role.
     *
     * @param idCompany the ID of the company
     * @param roleName  the role name to filter users
     * @return the count of users matching the criteria
     */
    int countByCompany_IdCompanyAndRole_RoleName(int idCompany, String roleName);

    /**
     * Finds all enabled users for a given company.
     *
     * @param idCompany the ID of the company
     * @return list of enabled users belonging to the company
     */
    List<User> findAllByCompany_IdCompanyAndEnabledTrue(int idCompany);
}
