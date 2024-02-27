package com.pb.repository;

import com.pb.constants.UserType;
import com.pb.model.BannerEntity;
import com.pb.model.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<BannerEntity,Long> {
    


    Page<BannerEntity> findAllByLocation(Pageable pageRequestOfMaxInteger, String location);
}
