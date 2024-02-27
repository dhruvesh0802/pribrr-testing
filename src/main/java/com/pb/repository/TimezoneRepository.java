package com.pb.repository;

import com.pb.model.CountryEntity;
import com.pb.model.ProfileEntity;
import com.pb.model.TimezoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimezoneRepository extends JpaRepository<TimezoneEntity,Long> {
    List<TimezoneEntity> findAllByIsActiveTrue();
}
