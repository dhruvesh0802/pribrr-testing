package com.pb.dto;

import com.pb.model.UserEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionDTO {
     String customerId;
     String productId;
     String subscriptionId;
     String nextSubscriptionId;
     String nextProductId;
     String paymentId;
     Long id;
     UserEntity userEntity;
     LocalDate nextSubscriptionStartDate;
}
