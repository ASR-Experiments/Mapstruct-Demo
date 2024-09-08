package org.asr.experiments.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserRequest {
    String firstName;
    String middleName;
    String lastName;
    String email;
    String password;
    String phoneCode;
    String phoneNumber;
    String dateOfBirth;
    String gender;
    String profilePictureUrl;
    String permanentHouseNumber;
    String permanentStreet;
    String permanentLocality;
    String permanentCity;
    String permanentState;
    String permanentPostalCode;
    String permanentCountry;
    String permanentLatitude;
    String permanentLongitude;
    String currentHouseNumber;
    String currentStreet;
    String currentLocality;
    String currentCity;
    String currentState;
    String currentPostalCode;
    String currentCountry;
    String currentLatitude;
    String currentLongitude;
    String linkedinProfileUrl;
    String githubProfileUrl;
    String twitterProfileUrl;
    String facebookProfileUrl;
    String instagramProfileUrl;
    String stackoverflowProfileUrl;
    String mediumProfileUrl;
    String youtubeProfileUrl;
    String websiteUrl;
    String blogUrl;
    Set<String> otherProfileUrlSet;
}
