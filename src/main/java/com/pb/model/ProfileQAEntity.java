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
@Table(name = "profile_qa")
@SQLDelete(sql =
        "UPDATE profile_qa SET deleted_date=now() " +
                "WHERE id = ?")
@Where(clause = "deleted_date IS NULL")
public class ProfileQAEntity extends BaseEntity{

// Quality Assurance Information //

    @Column(name = "qa_contact")
    String qaContact; // phone

    @Column(name = "qa_email")
    String qaEmail;

    @Column(name = "qa_lab")
    Integer qaLab; //1,2,3 - other

    @Column(name = "qa_lab_name")
    String qaLabName;// if other selected in QaLav

    @Column(name = "sourcing_partners")
    String sourcingPartners;

}
