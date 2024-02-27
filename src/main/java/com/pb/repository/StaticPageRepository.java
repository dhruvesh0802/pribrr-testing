package com.pb.repository;

import com.pb.constants.UserType;
import com.pb.model.BannerEntity;
import com.pb.model.EmailTemplateEntity;
import com.pb.model.StaticPageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaticPageRepository extends JpaRepository<StaticPageEntity,Long> {
    StaticPageEntity findByPageName(String pageName);
}
