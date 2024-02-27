package com.pb.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    LocalDateTime createdDate;
    @Column(name = "deleted_date")
    LocalDateTime deletedDate;
    @LastModifiedDate
    @Column(name = "updated_date", nullable = false)
    LocalDateTime updatedDate;
    
    @Column(name = "is_active")
    Boolean isActive = true;
}
