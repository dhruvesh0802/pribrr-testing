package com.pb.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity
@Table(name = "otp")
public class OTPEntity extends BaseEntity {
    
    Integer otp;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    UserEntity userEntity;
}
