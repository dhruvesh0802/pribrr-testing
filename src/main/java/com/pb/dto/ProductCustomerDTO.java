package com.pb.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCustomerDTO {

    Long userId;
    String productId;
    String customerId;
    String subscriptionId;
    String paymentId;
}
