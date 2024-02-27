package com.pb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pb.constants.UserType;
import com.pb.constants.ValidationMessageConstant;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity
@Table(name = "banner")
@SQLDelete(sql =
        "UPDATE banner SET deleted_date=now() " +
                "WHERE id = ?")
@Where(clause = "deleted_date IS NULL")
public class BannerEntity extends BaseEntity{

    String title;
    @Column(name = "redirect_url")
    String redirectUrl;
    
    @Column(name = "path")
    String bannerPath;
    
    @Column(name = "location")
    String location;

    @Column(name = "description")
    String description;
}
