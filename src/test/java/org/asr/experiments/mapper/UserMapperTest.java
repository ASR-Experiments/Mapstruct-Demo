package org.asr.experiments.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asr.experiments.dto.request.UserRequest;
import org.asr.experiments.entity.UserEntity;
import org.asr.experiments.util.ObjectUtil;
import org.asr.experiments.util.PropertyUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

class UserMapperTest {

    @Test
    void testUserToEntity_whenRequestIsEmpty_thenEntityShouldBeEmpty() {
        // Arrange
        UserRequest request = new UserRequest();

        // Act
        Optional<UserEntity> userEntity = UserMapper.userToEntity(request);

        // Assert
        userEntity.ifPresentOrElse(entity -> {
                    assertNull(entity.getName());
                    assertNull(entity.getEmail());
                    assertNull(entity.getPassword());
                    assertNull(entity.getPhoneNumber());
                    assertNull(entity.getDateOfBirth());
                    assertNull(entity.getGender());
                    assertNull(entity.getProfilePictureUrl());
                    assertNull(entity.getAddressSet());
                    assertNull(entity.getConnection());
                },
                () -> fail("UserEntity should not be empty"));
    }

    @Test
    void testUserToEntity_whenRequestIsNotEmpty_thenEntityShouldNotBeEmpty() throws IOException {
        // Arrange
        Properties properties = new Properties();
        PropertyUtil.loadProperties(properties, "application.properties");
        String request = properties.getProperty("sample.request");
        ObjectMapper objectMapper = ObjectUtil.getObjectMapper();
        UserRequest userRequest = objectMapper.readValue(request, UserRequest.class);
        // Test
        Optional<UserEntity> userEntity = UserMapper.userToEntity(userRequest);
        // Assertions
        userEntity.ifPresentOrElse(entity -> {
                    assertNotNull(entity.getName());
                    assertNotNull(entity.getEmail());
                    assertNotNull(entity.getPassword());
                    assertNotNull(entity.getPhoneNumber());
                    assertNotNull(entity.getDateOfBirth());
                    assertNotNull(entity.getGender());
                    assertNotNull(entity.getProfilePictureUrl());
                    assertNotNull(entity.getAddressSet());
                    assertNotNull(entity.getConnection());
                },
                () -> fail("UserEntity should not be empty"));
        userEntity
                .map(UserEntity::getAddressSet)
                .filter(x -> !x.isEmpty())
                .ifPresentOrElse(
                        addressSet -> addressSet
                                .forEach(address -> {
                                    assertNotNull(address.getHouseNumber());
                                    assertNotNull(address.getStreet());
                                    assertNotNull(address.getCity());
                                    assertNotNull(address.getState());
                                    assertNotNull(address.getCountry());
                                    assertNotNull(address.getPostalCode());
                                    assertNotNull(address.getType());
                                }),
                        () -> fail("AddressSet should not be empty")
                );
        userEntity
                .map(UserEntity::getConnection)
                .ifPresentOrElse(connection -> {
                            assertNotNull(connection.getBlogUrl());
                            assertNotNull(connection.getGithubProfileUrl());
                            assertNotNull(connection.getLinkedinProfileUrl());
                            assertNotNull(connection.getTwitterProfileUrl());
                            assertNotNull(connection.getFacebookProfileUrl());
                            assertNotNull(connection.getInstagramProfileUrl());
                        },
                        () -> fail("Connection should not be empty")
                );
    }
}