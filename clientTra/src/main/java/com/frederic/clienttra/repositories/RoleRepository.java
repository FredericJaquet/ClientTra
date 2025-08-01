package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Role} entities.
 * <p>
 * Extends JpaRepository to provide basic CRUD operations and
 * defines a custom method to find roles by their name.
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Finds a role entity by its role name.
     *
     * @param role the name of the role to find
     * @return an Optional containing the Role if found, or empty otherwise
     */
    Optional<Role> findByRoleName(String role);
}
