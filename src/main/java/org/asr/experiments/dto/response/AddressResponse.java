package org.asr.experiments.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AddressResponse {
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
