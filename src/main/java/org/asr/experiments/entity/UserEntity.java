package org.asr.experiments.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.net.URL;
import java.time.LocalDate;
import java.util.Set;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity extends AbstractEntity {
    String name;
    String email;
    String password;
    Set<AddressEntity> addressSet;
    String phoneNumber;
    LocalDate dateOfBirth;
    String gender;
    URL profilePictureUrl;
    @Builder.Default
    String role = "user";
    @Builder.Default
    Boolean isActive = Boolean.TRUE;
    ExternalProfileEntity connection;
}
