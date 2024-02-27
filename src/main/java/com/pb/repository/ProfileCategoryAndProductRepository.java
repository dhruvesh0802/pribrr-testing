package com.pb.repository;

import com.pb.model.ProfileCategoryAndProductEntity;
import com.pb.model.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileCategoryAndProductRepository extends JpaRepository<ProfileCategoryAndProductEntity,Long> {

}
