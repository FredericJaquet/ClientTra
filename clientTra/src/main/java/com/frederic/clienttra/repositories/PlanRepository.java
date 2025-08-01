package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Plan} entities.
 * <p>
 * Extends JpaRepository to provide CRUD operations.
 */
public interface PlanRepository extends JpaRepository<Plan, Integer> {

    /**
     * Finds a plan by its name.
     *
     * @param plan the name of the plan
     * @return an Optional containing the Plan if found, or empty otherwise
     */
    Optional<Plan> findByPlanName(String plan);
}
