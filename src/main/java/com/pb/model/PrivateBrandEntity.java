package com.pb.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity
@Table(name = "private_brand")
public class PrivateBrandEntity extends BaseEntity{

    String name;
    String logoUrl;
    @ManyToOne
    @JoinColumn(name = "cat_prod_id")
    private ProfileCategoryAndProductEntity profileCategoryAndProduct;
}
