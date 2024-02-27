package com.pb.repository;

import com.pb.model.EmailTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplateEntity,Long> {
    EmailTemplateEntity findByTitle(String title);
}
