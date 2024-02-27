package com.pb.repository;

import com.pb.model.SubCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity,Long> {
    Optional<SubCategoryEntity> findByName(String name);
    
    Optional<SubCategoryEntity> findByNameAndIdNot(String name, Long id);
    
    Page<SubCategoryEntity> findAllByNameContaining(Pageable pageRequestOfMaxInteger, String filteredName);
}
