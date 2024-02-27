package com.pb.repository;

import com.pb.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByEmailAndDeletedDateIsNull(String email);
    Optional<UserEntity> findByEmailAndPasswordAndUserType(String email,String password,String userType);

    Optional<UserEntity> findByEmailAndPasswordAndUserTypeNot(String email,String password,String userType);

    @Query(value = "SELECT * FROM user WHERE user_type = :type AND email LIKE %:email% AND deleted_date IS NULL",nativeQuery = true)
    Page<UserEntity> findAllByUserTypeAndEmailContaining(String type,String email,Pageable pageable);
    
    
    Optional<UserEntity> findByIdAndPassword(Long id, String password);

    List<UserEntity> findByUserTypeAndDeletedDateIsNullAndIsActiveTrue(String type);
    long countByUserTypeAndDeletedDateIsNull(String userType);
}
