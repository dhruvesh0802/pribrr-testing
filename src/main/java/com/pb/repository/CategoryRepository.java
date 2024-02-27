package com.pb.repository;

import com.pb.model.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {
    Optional<CategoryEntity> findByName(String name);
    
    Optional<CategoryEntity> findByNameAndIdNot(String name, Long id);
    
    Page<CategoryEntity> findAllByNameContainingOrderByNameAsc(Pageable pageRequestOfMaxInteger, String filteredName);

    List<CategoryEntity> findAllByDepartmentEntityId(Long departmentId);
}
