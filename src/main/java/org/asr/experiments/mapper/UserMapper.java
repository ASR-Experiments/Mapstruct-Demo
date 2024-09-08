package org.asr.experiments.mapper;

import lombok.extern.java.Log;
import org.asr.experiments.dto.request.UserRequest;
import org.asr.experiments.dto.response.AddressResponse;
import org.asr.experiments.dto.response.ExternalProfileResponse;
import org.asr.experiments.dto.response.UserResponse;
import org.asr.experiments.entity.AddressEntity;
import org.asr.experiments.entity.ExternalProfileEntity;
import org.asr.experiments.entity.UserEntity;
import org.asr.experiments.util.PasswordUtil;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Mapper class to convert UserRequest to UserEntity and UserEntity to UserResponse
 */
@Log
public record UserMapper() {
    /**
     * Convert UserRequest to UserEntity
     *
     * @param request UserRequest object
     * @return UserEntity object
     */
    public static Optional<UserEntity> userToEntity(UserRequest request) {
        return Optional
                .ofNullable(request)
                .map(dto -> UserEntity.builder()
                        .name(joinWords(" ", dto.getFirstName(), dto.getMiddleName(), dto.getLastName()))
                        .email(dto.getEmail())
                        .password(dto.getPassword())
                        .phoneNumber(joinWords("-", dto.getPhoneCode(), dto.getPhoneNumber()))
                        .dateOfBirth(Optional.of(dto)
                                .map(UserRequest::getDateOfBirth)
                                .map(LocalDate::parse)
                                .orElse(null))
                        .gender(dto.getGender())
                        .profilePictureUrl(Optional.ofNullable(dto.getProfilePictureUrl())
                                .map(URI::create)
                                .map(uri -> {
                                    try {
                                        return uri.toURL();
                                    } catch (MalformedURLException e) {
                                        log.warning("Invalid URL: " + uri);
                                        return null;
                                    }
                                })
                                .orElse(null))
                        .addressSet(Optional.of(dto)
                                .map(UserMapper::addressToEntity)
                                .filter(set -> !set.isEmpty())
                                .orElse(null))
                        .connection(Optional.of(ExternalProfileEntity.builder()
                                        .linkedinProfileUrl(dto.getLinkedinProfileUrl())
                                        .githubProfileUrl(dto.getGithubProfileUrl())
                                        .twitterProfileUrl(dto.getTwitterProfileUrl())
                                        .facebookProfileUrl(dto.getFacebookProfileUrl())
                                        .instagramProfileUrl(dto.getInstagramProfileUrl())
                                        .stackoverflowProfileUrl(dto.getStackoverflowProfileUrl())
                                        .mediumProfileUrl(dto.getMediumProfileUrl())
                                        .youtubeProfileUrl(dto.getYoutubeProfileUrl())
                                        .websiteUrl(dto.getWebsiteUrl())
                                        .blogUrl(dto.getBlogUrl())
                                        .otherProfileUrlSet(dto.getOtherProfileUrlSet())
                                        .build())
                                .filter(profile -> !profile.isEmpty())
                                .orElse(null))
                        .build());
    }

    private static String joinWords(String deli, String... words) {
        return Optional.of(Stream.of(words)
                        .filter(name -> name != null && !name.isBlank())
                        .collect(Collectors.joining(deli)))
                .filter(x -> !x.isBlank())
                .orElse(null);
    }

    private static Set<AddressEntity> addressToEntity(UserRequest dto) {
        AddressEntity permanentAddress = AddressEntity.builder()
                .houseNumber(dto.getPermanentHouseNumber())
                .street(dto.getPermanentStreet())
                .locality(dto.getPermanentLocality())
                .latitude(dto.getPermanentLatitude())
                .longitude(dto.getPermanentLongitude())
                .country(dto.getPermanentCountry())
                .city(dto.getPermanentCity())
                .state(dto.getPermanentState())
                .postalCode(dto.getPermanentPostalCode())
                .type("permanent")
                .build();
        AddressEntity currentAddress = AddressEntity.builder()
                .houseNumber(dto.getCurrentHouseNumber())
                .street(dto.getCurrentStreet())
                .locality(dto.getCurrentLocality())
                .latitude(dto.getCurrentLatitude())
                .longitude(dto.getCurrentLongitude())
                .city(dto.getCurrentCity())
                .state(dto.getCurrentState())
                .country(dto.getCurrentCountry())
                .postalCode(dto.getCurrentPostalCode())
                .type("current")
                .build();
        Set<AddressEntity> addressSet = new HashSet<>();
        if (!permanentAddress.isEmpty())
            addressSet.add(permanentAddress);
        if (!currentAddress.isEmpty())
            addressSet.add(currentAddress);
        return addressSet;
    }

    /**
     * Convert UserEntity to UserResponse
     *
     * @param entity UserEntity object
     * @return UserResponse object
     */
    public static Optional<UserResponse> userToDto(UserEntity entity) {
        if (entity == null) {
            return Optional.empty();
        }
        Set<AddressResponse> addressSet = null;
        if (entity.getAddressSet() != null && !entity.getAddressSet().isEmpty()) {
            addressSet = entity.getAddressSet()
                    .stream()
                    .map(UserMapper::mapAddress)
                    .collect(Collectors.toSet());
        }
        return Optional.ofNullable(UserResponse.builder()
                .userId(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .dateOfBirth(entity.getDateOfBirth())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .password(PasswordUtil.hashPassword(entity.getPassword()))
                .connection(ExternalProfileResponse.builder()
                        .linkedinProfileUrl(entity.getConnection().getLinkedinProfileUrl())
                        .githubProfileUrl(entity.getConnection().getGithubProfileUrl())
                        .twitterProfileUrl(entity.getConnection().getTwitterProfileUrl())
                        .facebookProfileUrl(entity.getConnection().getFacebookProfileUrl())
                        .instagramProfileUrl(entity.getConnection().getInstagramProfileUrl())
                        .stackoverflowProfileUrl(entity.getConnection().getStackoverflowProfileUrl())
                        .mediumProfileUrl(entity.getConnection().getMediumProfileUrl())
                        .youtubeProfileUrl(entity.getConnection().getYoutubeProfileUrl())
                        .websiteUrl(entity.getConnection().getWebsiteUrl())
                        .blogUrl(entity.getConnection().getBlogUrl())
                        .otherProfileUrlSet(entity.getConnection().getOtherProfileUrlSet())
                        .build())
                .addressSet(addressSet)
                .build());
    }

    /**
     * Map AddressEntity to AddressResponse
     *
     * @param addressEntity AddressEntity object
     * @return AddressResponse object
     */
    private static AddressResponse mapAddress(AddressEntity addressEntity) {
        return AddressResponse.builder()
                .houseNumber(addressEntity.getHouseNumber())
                .street(addressEntity.getStreet())
                .locality(addressEntity.getLocality())
                .latitude(addressEntity.getLatitude())
                .longitude(addressEntity.getLongitude())
                .type(addressEntity.getType())
                .city(addressEntity.getCity())
                .state(addressEntity.getState())
                .country(addressEntity.getCountry())
                .postalCode(addressEntity.getPostalCode())
                .build();
    }
}
