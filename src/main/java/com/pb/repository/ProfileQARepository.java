package com.pb.repository;

import com.pb.model.ProfileEntity;
import com.pb.model.ProfileQAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileQARepository extends JpaRepository<ProfileQAEntity,Long> {

}
