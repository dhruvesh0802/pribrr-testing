package com.pb.repository;

import com.pb.model.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<PlanEntity,Long> {
    PlanEntity findByName(String name);
    
    PlanEntity findByNameAndIdNot(String name, Long id);
}
