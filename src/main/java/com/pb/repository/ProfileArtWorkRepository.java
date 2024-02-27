package com.pb.repository;

import com.pb.model.ProfileArtworkEntity;
import com.pb.model.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileArtWorkRepository extends JpaRepository<ProfileArtworkEntity,Long> {

}
