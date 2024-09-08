package org.asr.experiments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.java.Log;
import org.asr.experiments.dto.request.UserRequest;
import org.asr.experiments.dto.response.UserResponse;
import org.asr.experiments.entity.UserEntity;
import org.asr.experiments.mapper.UserMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@Log
public class Main {
    public static void main(String[] args) throws IOException {

        // Reading logic
        Properties properties = new Properties();

        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                log.info("Sorry, unable to find application.properties");
                return;
            }
            properties.load(input);
        }

        String request = properties.getProperty("sample.request");
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(javaTimeModule);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        UserRequest userRequest = objectMapper.readValue(request, UserRequest.class);
        // Business Logic
        UserEntity userEntity = UserMapper.userToEntity(userRequest);
        log.info(userEntity.toString());
        // Perform some more logic
        // Business Logic
        Optional<UserResponse> response = UserMapper.userToDto(userEntity);
        if (response.isPresent()) {
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.get()));
            return;
        }
        log.severe("Error in converting UserEntity to UserResponse");
    }
}