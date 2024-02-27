package com.pb.repository;

import com.pb.model.ProfileMemberInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileMemberInfoRepository extends JpaRepository<ProfileMemberInfoEntity,Long> {

}
