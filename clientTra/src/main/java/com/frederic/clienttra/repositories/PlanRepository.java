package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Integer> {
    Optional<Plan> findByPlanName(String plan);
}
