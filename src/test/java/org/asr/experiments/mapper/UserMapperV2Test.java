package org.asr.experiments.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asr.experiments.dto.request.UserRequest;
import org.asr.experiments.entity.UserEntity;
import org.asr.experiments.util.ObjectUtil;
import org.asr.experiments.util.PropertyUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperV2Test {

    @Test
    void testUserToEntity_whenRequestIsEmpty_thenEntityShouldBeEmpty() {
        // Arrange
        UserRequest request = new UserRequest();

        // Act
        UserEntity entity = UserMapperV2.INSTANCE.userToEntity(request);

        // Assert
        assertNotNull(entity);
        assertNull(entity.getName());
        assertNull(entity.getEmail());
        assertNull(entity.getPassword());
        assertNull(entity.getPhoneNumber());
        assertNull(entity.getDateOfBirth());
        assertNull(entity.getGender());
        assertNull(entity.getProfilePictureUrl());
        assertNull(entity.getAddressSet());
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
        UserEntity userEntity = UserMapperV2.INSTANCE.userToEntity(userRequest);
        // Assertions
        assertNotNull(userEntity);
        assertNotNull(userEntity.getName());
        assertNotNull(userEntity.getEmail());
        assertNotNull(userEntity.getPassword());
        assertNotNull(userEntity.getPhoneNumber());
        assertNotNull(userEntity.getDateOfBirth());
        assertNotNull(userEntity.getGender());
        assertNotNull(userEntity.getProfilePictureUrl());
        assertNotNull(userEntity.getAddressSet());
        assertNotNull(userEntity.getConnection());
        userEntity.getAddressSet()
                .forEach(address -> {
                            assertNotNull(address.getHouseNumber());
                            assertNotNull(address.getStreet());
                            assertNotNull(address.getCity());
                            assertNotNull(address.getState());
                            assertNotNull(address.getCountry());
                            assertNotNull(address.getPostalCode());
                            assertNotNull(address.getType());
                        }
                );
        assertNotNull(userEntity.getConnection().getBlogUrl());
        assertNotNull(userEntity.getConnection().getGithubProfileUrl());
        assertNotNull(userEntity.getConnection().getLinkedinProfileUrl());
        assertNotNull(userEntity.getConnection().getTwitterProfileUrl());
        assertNotNull(userEntity.getConnection().getFacebookProfileUrl());
        assertNotNull(userEntity.getConnection().getInstagramProfileUrl());
    }
}