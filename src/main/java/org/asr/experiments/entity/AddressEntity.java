package org.asr.experiments.entity;

import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AddressEntity {
    String houseNumber;
    String street;
    String locality;
    String city;
    String state;
    String postalCode;
    String country;
    String latitude;
    String longitude;
    String type;
}
