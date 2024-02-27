package com.pb.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
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
@Table(name = "sub_category")
@SQLDelete(sql =
        "UPDATE sub_category SET deleted_date=now() " +
                "WHERE id = ?")
@Where(clause = "deleted_date IS NULL")
public class SubCategoryEntity extends BaseEntity{
    
    String name;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    CategoryEntity categoryEntity;
}
