package com.pb.model;

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
@Table(name = "profile")
@SQLDelete(sql =
        "UPDATE profile SET deleted_date=now() " +
                "WHERE id = ?")
@Where(clause = "deleted_date IS NULL")
public class ProfileEntity extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "company_detail_id")
    ProfileCompanyDetailsEntity profileCompanyDetailsEntity; // check req fields

    @ManyToOne
    @JoinColumn(name = "member_info_id")
    ProfileMemberInfoEntity profileMemberInfoEntity;

    @ManyToOne
    @JoinColumn(name = "category_product_id")
    ProfileCategoryAndProductEntity profileCategoryAndProductEntity;

    @ManyToOne
    @JoinColumn(name = "artwork_id")
    ProfileArtworkEntity profileArtworkEntity;

    @ManyToOne
    @JoinColumn(name = "qa_id")
    ProfileQAEntity profileQAEntity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity userEntity;
}
