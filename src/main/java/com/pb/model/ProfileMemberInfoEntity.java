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
@Table(name = "profile_member_info")
@SQLDelete(sql =
        "UPDATE profile_member_info SET deleted_date=now() " +
                "WHERE id = ?")
@Where(clause = "deleted_date IS NULL")
public class ProfileMemberInfoEntity extends BaseEntity{

// Member Information //

    @Column(name = "title")
    String title;

    @Column(name = "isBroker")
    Boolean isBroker;

    @Column(name = "phone")
    String phone;

    @Column(name = "country")
    String country;

    @Column(name = "address")
    String address;

    @Column(name = "email")
    String email;

    @Column(name = "website")
    String website;

    @ManyToOne
    @JoinColumn(name = "member_timezone_id")
    TimezoneEntity memberTimezoneEntity;

    @ManyToOne
    @JoinColumn(name = "department_id") // retailer
    DepartmentEntity departmentEntity;


}
