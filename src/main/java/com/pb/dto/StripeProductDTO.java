package com.pb.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StripeProductDTO {
     String id;
     boolean active;
     String default_price;
     String description;
     String name;
     List<String> features;
}
