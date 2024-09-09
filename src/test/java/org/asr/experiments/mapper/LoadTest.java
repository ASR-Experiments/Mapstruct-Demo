package org.asr.experiments.mapper;

import com.github.javafaker.Faker;
import lombok.extern.java.Log;
import org.asr.experiments.dto.request.UserRequest;
import org.asr.experiments.dto.response.UserResponse;
import org.asr.experiments.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log
class LoadTest {
    private static final long ITERATIONS = 2_500_000L;


    @Test
    void test() throws InterruptedException {
        Faker faker = new Faker();
        Random random = new Random();
        Set<UserRequest> requestSet = new LinkedHashSet<>();
        for (long i = 1L; i < ITERATIONS; i++) {
            if (i % 100000 == 0) {
                log.info("Iteration: " + i);
            }
            UserRequest req = UserRequest.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .middleName(faker.name().nameWithMiddle())
                    .email(faker.internet().emailAddress())
                    .phoneCode(faker.phoneNumber().extension())
                    .phoneNumber(faker.phoneNumber().phoneNumber())
                    .blogUrl(faker.internet().url())
                    .password(faker.internet().password())
                    .currentCity(faker.address().city())
                    .currentCountry(faker.address().country())
                    .permanentCity(faker.address().city())
                    .permanentCountry(faker.address().country())
                    .dateOfBirth(LocalDate.ofInstant(faker.date().birthday().toInstant(), ZoneId.systemDefault()).toString())
                    .currentHouseNumber(faker.address().buildingNumber())
                    .currentStreet(faker.address().streetName())
                    .currentPostalCode(faker.address().zipCode())
                    .currentLatitude(faker.address().latitude())
                    .currentLongitude(faker.address().longitude())
                    .permanentHouseNumber(faker.address().buildingNumber())
                    .permanentStreet(faker.address().streetName())
                    .permanentPostalCode(faker.address().zipCode())
                    .permanentLatitude(faker.address().latitude())
                    .permanentLongitude(faker.address().longitude())
                    .permanentLocality(faker.address().stateAbbr())
                    .currentLocality(faker.address().stateAbbr())
                    .githubProfileUrl(faker.internet().url())
                    .linkedinProfileUrl(faker.internet().url())
                    .twitterProfileUrl(faker.internet().url())
                    .facebookProfileUrl(faker.internet().url())
                    .instagramProfileUrl(faker.internet().url())
                    .mediumProfileUrl(faker.internet().url())
                    .youtubeProfileUrl(faker.internet().url())
                    .otherProfileUrlSet(Stream.generate(() -> faker.internet().url()) // Generate random integers between 0 and 99
                            .limit(random.nextInt(5))
                            .collect(Collectors.toSet()))
                    .profilePictureUrl(faker.internet().avatar())
                    .build();
            requestSet.add(req);
        }

        Thread.sleep(5000);

        testLoadOnMapperV1(requestSet);

        Thread.sleep(5000);

        testLoadOnMapperV2(requestSet);
    }

    private static void testLoadOnMapperV2(Set<UserRequest> requestSet) {
        long startTime = System.nanoTime();

        // Perform some operations
        for (UserRequest userRequest : requestSet) {
            UserEntity userEntity = UserMapperV2.INSTANCE.userToEntity(userRequest);
            UserResponse userResponse = UserMapperV2.INSTANCE.userToDto(userEntity);
            Assertions.assertNotNull(userResponse);
        }

        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        log.info("V2 Time taken: " + duration + " nanoseconds");
    }

    private static void testLoadOnMapperV1(Set<UserRequest> requestSet) {
        long endTime;
        long startTime;
        long duration;
        startTime = System.nanoTime();

        // Perform some operations
        for (UserRequest userRequest : requestSet) {
            Optional<UserEntity> userEntity = UserMapper.userToEntity(userRequest);
            userEntity.ifPresentOrElse(entity -> {
                        Optional<UserResponse> userResponse = UserMapper.userToDto(entity);
                        userResponse.ifPresentOrElse(
                                Assertions::assertNotNull,
                                () -> log.warning(entity.toString() + "wasn't able to transform to dto"));
                    },
                    () -> log.warning(userRequest.toString() + "wasn't able to transform to entity"));
        }

        endTime = System.nanoTime();
        duration = endTime - startTime;

        log.info("V1 Time taken: " + duration + " nanoseconds");
    }
}
