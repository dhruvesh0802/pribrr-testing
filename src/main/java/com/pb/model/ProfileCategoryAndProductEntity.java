package com.pb.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity
@Table(name = "profile_category_and_product")
@SQLDelete(sql =
        "UPDATE profile_category_and_product SET deleted_date=now() " +
                "WHERE id = ?")
@Where(clause = "deleted_date IS NULL")
public class ProfileCategoryAndProductEntity extends BaseEntity{


// Category & Product Information //

    @Column(name = "minimum_order")
    Integer minimumOrder; // 1-Case pack,2-Units

    @Column(name = "average_time_require")
    Integer averageTimeRequire; // 1-30,2-60,3-90,4-120

    @Column(name = "pickup_required")
    Integer pickupRequired;

    @Column(name = "pickup_details")
    String pickupDetails;

    @Column(name = "pickup_points")
    String pickupPoints;

    @Column(name = "edi")
    Boolean EDI;

    @Column(name = "work_with_allowance")
    Boolean workWithAllowance;

    @Column(name = "work_with_deadnet")
    Boolean workWithDeadnet;

    @Column(name = "factory_audit_report_path")
    String factoryAuditReportPath;

    @Column(name = "certificate_of_insurance_path")
    String certificateOfInsurancePath;

    @Column(name = "other_certificate_path")
    String otherCertificatePath;

    @Column(name = "document_and_forum_path")
    String documentAndForumPath;

    @Column(name = "nda_path")
    String NDAPath;

    @OneToMany(mappedBy = "profileCategoryAndProduct")
    private List<PrivateBrandEntity> privateBrand;

    @Column(name = "other_link")
    String otherLink;
}
