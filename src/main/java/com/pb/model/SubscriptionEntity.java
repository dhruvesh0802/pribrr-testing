package com.pb.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Table(name = "subscription")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    String customerId;
    String productId;
    String subscriptionId;
    String nextSubscriptionId;
    String nextProductId;
    String paymentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity userEntity;
}
