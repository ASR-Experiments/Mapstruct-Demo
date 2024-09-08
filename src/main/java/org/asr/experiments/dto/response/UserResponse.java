package org.asr.experiments.dto.response;

import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@SuperBuilder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserResponse {
    String userId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String createdBy;
    String updatedBy;
    String name;
    String email;
    String password;
    Set<AddressResponse> addressSet;
    String phoneNumber;
    LocalDate dateOfBirth;
    String gender;
    URL profilePictureUrl;
    String role;
    Boolean isActive;
    ExternalProfileResponse connection;
}
