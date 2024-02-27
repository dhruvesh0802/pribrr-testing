package com.pb.repository;

import com.pb.constants.UserType;
import com.pb.model.BannerEntity;
import com.pb.model.CategoryEntity;
import com.pb.model.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
    Page<ProductEntity> findAllByUserEntityId(Pageable pageable, Long userId);
}
