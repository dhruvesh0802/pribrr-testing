package com.pb.repository;

import com.pb.model.ProfileCompanyDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileCompanyDetailsRepository extends JpaRepository<ProfileCompanyDetailsEntity,Long> {

}
