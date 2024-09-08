package org.asr.experiments.mapper;

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
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Mapper class to convert UserRequest to UserEntity and UserEntity to UserResponse
 */
public class UserMapper {
    /**
     * Convert UserRequest to UserEntity
     *
     * @param dto UserRequest object
     * @return UserEntity object
     */
    public static UserEntity userToEntity(UserRequest dto) throws MalformedURLException {
        return UserEntity.builder()
                .name(new StringJoiner(" ")
                        .add(dto.getFirstName())
                        .add(dto.getMiddleName())
                        .add(dto.getLastName())
                        .toString())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneCode() + dto.getPhoneNumber())
                .dateOfBirth(LocalDate.parse(dto.getDateOfBirth()))
                .gender(dto.getGender())
                .profilePictureUrl(URI.create(dto.getProfilePictureUrl()).toURL())
                .addressSet(addressToEntity(dto))
                .connection(ExternalProfileEntity.builder()
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
                .build();
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
        return Set.of(permanentAddress, currentAddress);
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
