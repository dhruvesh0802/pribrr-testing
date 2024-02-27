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
@Table(name = "profile_company_details")
@SQLDelete(sql =
        "UPDATE profile_company_details SET deleted_date=now() " +
                "WHERE id = ?")
@Where(clause = "deleted_date IS NULL")
public class ProfileCompanyDetailsEntity extends BaseEntity{

// Company Details //

    @Column(name = "business_type") // retailer
    Long businessType; //  1-Discount Chain, 2-General-Department Store, 3-Grocery, 4-Convenient Store, 5-Pet Supplies, 6-Other

    @ManyToOne
    @JoinColumn(name = "category_id") // suplier
    CategoryEntity categoryEntity;

    @Column(name = "company_location")
    String companyLocation; // country

    @Column(name = "company_address")
    String companyAddress;

    @Column(name = "company_phone")
    String companyPhone;

    @Column(name = "company_email")
    String companyEmail;

    @Column(name = "company_website")
    String companyWebsite;

    @ManyToOne
    @JoinColumn(name = "company_timezone_id")
    TimezoneEntity companyTimezoneEntity;

    @Column(name = "company_logo_url")
    String companyLogoURL;

    @Column(name = "company_type")
    Long companyType; // 1-LLC,2-Public,3-partnership,4-Sole propriter

    @Column(name = "ceo_name")
    String ceoName;

    @Column(name = "annual_sale")
    Double annualSale;

    @Column(name = "annual_revenue")
    String annualRevenue;

    @Column(name = "no_of_employee")
    String noOfEmployee;

    @Column(name = "key_competitors")
    String keyCompetitors;


    // Vendor company Profile

    @Column(name = "no_of_locations")
    String noOfLocations;

}
