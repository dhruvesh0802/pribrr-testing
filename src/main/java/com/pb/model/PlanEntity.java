package com.pb.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity
@Table(name = "plans")
@SQLDelete(sql =
        "UPDATE plans SET deleted_date=now() " +
                "WHERE id = ?")
@Where(clause = "deleted_date IS NULL")
public class PlanEntity extends BaseEntity{
    String name;
    String benefits;
    Float price;
}
