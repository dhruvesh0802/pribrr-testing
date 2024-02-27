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
@Table(name = "profile_artwork")
@SQLDelete(sql =
        "UPDATE profile_artwork SET deleted_date=now() " +
                "WHERE id = ?")
@Where(clause = "deleted_date IS NULL")
public class ProfileArtworkEntity extends BaseEntity{

// Artwork Information //
    @Column(name = "product_catalog_path")
    String productCatalogPath;
    @Column(name = "artwork_label")
    String artworkLabel; // 1,2,3,4 comma seprate

    @Column(name = "ad_agency_name")
    String adAgencyName; // if 2 option selected in artwork label

    @Column(name = "artwork_cost")
    Long artworkCostPreference; // 1,2,3,4

    @Column(name = "artwork_requirements")
    Boolean artworkRequirements;
// if yes on above
    @Column(name = "artwork_requirement_path")
    String artworkRequirementPath;

    @Column(name = "printer_specs_path")
    String printerSpecsPath;

    @Column(name = "dielines_path")
    String dielinesPath;

// till here
    @Column(name = "artwork_contact")
    String artworkContact; // phone

    @Column(name = "artwork_email")
    String artworkEmail;



}
