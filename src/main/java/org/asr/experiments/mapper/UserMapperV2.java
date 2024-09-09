package org.asr.experiments.mapper;

import org.asr.experiments.dto.request.UserRequest;
import org.asr.experiments.dto.response.UserResponse;
import org.asr.experiments.entity.AddressEntity;
import org.asr.experiments.entity.ExternalProfileEntity;
import org.asr.experiments.entity.UserEntity;
import org.asr.experiments.util.PasswordUtil;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(
        builder = @Builder,
        imports = {PasswordUtil.class},
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE
)
public interface UserMapperV2 {

    UserMapperV2 INSTANCE = org.mapstruct.factory.Mappers.getMapper(UserMapperV2.class);

    //    @Mapping(target = "email")
//    @Mapping(target = "password")
    @Mapping(target = "gender")
    @Mapping(target = "phoneNumber", expression = "java(UserMapperV2.joinWords(\"-\", request.getPhoneCode(), request.getPhoneNumber()))")
    @Mapping(target = "addressSet", expression = "java(this.mapAddressSet(request))")
    @Mapping(target = "name", expression = "java(UserMapperV2.joinWords(\" \", request.getFirstName(), request.getMiddleName(), request.getLastName()))")
    @Mapping(target = "connection", source = ".")
    UserEntity userToEntity(UserRequest request);

    default Set<AddressEntity> mapAddressSet(UserRequest request) {
        Set<AddressEntity> addressSet = new HashSet<>();
        AddressEntity permanentAddress = UserMapperV2.INSTANCE.permanentAddressToEntity(request);
        AddressEntity currentAddress = UserMapperV2.INSTANCE.currentAddressToEntity(request);

        if (!permanentAddress.isEmpty())
            addressSet.add(permanentAddress);
        if (!currentAddress.isEmpty())
            addressSet.add(currentAddress);
        return addressSet.isEmpty() ? null : addressSet;
    }

    @Mapping(target = "houseNumber", source = "currentHouseNumber")
    @Mapping(target = "street", source = "currentStreet")
    @Mapping(target = "locality", source = "currentLocality")
    @Mapping(target = "city", source = "currentCity")
    @Mapping(target = "state", source = "currentState")
    @Mapping(target = "country", source = "currentCountry")
    @Mapping(target = "postalCode", source = "currentPostalCode")
    @Mapping(target = "latitude", source = "currentLatitude")
    @Mapping(target = "longitude", source = "currentLongitude")
    @Mapping(target = "type", constant = "current")
    AddressEntity currentAddressToEntity(UserRequest request);

    @Mapping(target = "houseNumber", source = "permanentHouseNumber")
    @Mapping(target = "street", source = "permanentStreet")
    @Mapping(target = "locality", source = "permanentLocality")
    @Mapping(target = "city", source = "permanentCity")
    @Mapping(target = "state", source = "permanentState")
    @Mapping(target = "country", source = "permanentCountry")
    @Mapping(target = "postalCode", source = "permanentPostalCode")
    @Mapping(target = "latitude", source = "permanentLatitude")
    @Mapping(target = "longitude", source = "permanentLongitude")
    @Mapping(target = "type", constant = "permanent")
    AddressEntity permanentAddressToEntity(UserRequest request);

    ExternalProfileEntity connectionToEntity(UserRequest request);

    static String joinWords(String deli, String... words) {
        return Optional.of(Stream.of(words)
                        .filter(name -> name != null && !name.isBlank())
                        .collect(Collectors.joining(deli)))
                .filter(x -> !x.isBlank())
                .orElse(null);
    }

    @Mapping(target = "password", expression = "java(PasswordUtil.hashPassword(entity.getPassword()))")
    UserResponse userToDto(UserEntity entity);
}
