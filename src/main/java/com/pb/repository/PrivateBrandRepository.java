package com.pb.repository;

import com.pb.constants.UserType;
import com.pb.model.BannerEntity;
import com.pb.model.PrivateBrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivateBrandRepository extends JpaRepository<PrivateBrandEntity,Long> {
    
}
