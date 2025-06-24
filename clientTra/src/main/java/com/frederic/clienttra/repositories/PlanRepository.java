package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Plan;
import com.frederic.clienttra.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {
    Optional<Plan> findByPlanName(String plan);
}
