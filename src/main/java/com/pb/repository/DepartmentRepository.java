package com.pb.repository;

import com.pb.model.DepartmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity,Long> {
    Optional<DepartmentEntity> findByName(String name);
    
    DepartmentEntity findByNameAndIdNot(String name, Long id);
    
    Page<DepartmentEntity> findAllByNameContainingAndIsActiveTrue(Pageable pageable,String name);
    Page<DepartmentEntity> findAllByNameContaining(Pageable pageable,String name);
}
