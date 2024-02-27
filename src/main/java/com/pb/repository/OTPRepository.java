package com.pb.repository;

import com.pb.model.OTPEntity;
import com.pb.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTPEntity,Long> {
    
    @Modifying
    @Query(value = "DELETE from otp where user_id=:userId",nativeQuery = true)
    void deleteAllByUserId(Long userId);
    
    Optional<OTPEntity> findByUserEntity(UserEntity userEntity);
}
