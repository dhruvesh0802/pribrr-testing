package com.pb.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql =
        "UPDATE user SET deleted_date=now() " +
                "WHERE id = ?")
@Where(clause = "deleted_date IS NULL")
public class UserEntity extends BaseEntity{

    @Column(name = "email")
    String email;
    String password;

    @Column(name = "is_verified")
    Boolean isVerified = false;

    @OneToOne
    @JoinColumn(name = "country_id")
    CountryEntity country;

    @Column(name = "user_type")
    String userType;

    @Column(name = "user_name")
    String userName;

    @Column(name = "company_name")
    String companyName;

    @Column(name = "profile_url")
    String profileUrl;

    @ManyToMany
    @JoinTable(name = "user_network",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "network_id"))
    private Set<UserEntity> network = new HashSet<>();
}
