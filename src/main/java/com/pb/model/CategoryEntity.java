package com.pb.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity
@Table(name = "category")
@SQLDelete(sql =
        "UPDATE category SET deleted_date=now() " +
                "WHERE id = ?")
@Where(clause = "deleted_date IS NULL")
public class CategoryEntity extends BaseEntity{
    
    String name;
    
    @ManyToOne
    @JoinColumn(name = "department_id")
    DepartmentEntity departmentEntity;
}
