package com.pb.model;

import com.pb.constants.UserType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity
@Table(name = "product")
@SQLDelete(sql =
        "UPDATE product SET deleted_date=now() " +
                "WHERE id = ?")
@Where(clause = "deleted_date IS NULL")
public class ProductEntity extends BaseEntity{

    String name;

    @ManyToOne
    @JoinColumn(name = "department_id")
    DepartmentEntity departmentEntity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    CategoryEntity categoryEntity;

    String description;

    @Column(name = "minimum_order_qnt")
    String minimumOrderQuantity;
    
    @Column(name = "moq_type")
    Integer moqType;

    @Column(name = "case_dimension")
    String caseDimension;

    @Column(name = "case_Weight")
    String caseWeight;

    @Column(name = "avg_unit_size")
    String avgUnitSize;

    @Column(name = "avg_unit_weight")
    String avgUnitWeight;

    @Column(name = "media_url")
    String mediaUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity userEntity;
}
