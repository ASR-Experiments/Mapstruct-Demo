package org.asr.experiments;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.asr.experiments.dto.request.UserRequest;
import org.asr.experiments.dto.response.UserResponse;
import org.asr.experiments.entity.UserEntity;
import org.asr.experiments.mapper.UserMapper;
import org.asr.experiments.util.ObjectUtil;
import org.asr.experiments.util.PropertyUtil;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@Log
public class Main {
    public static void main(String[] args) throws IOException {

        // Reading logic
        Properties properties = new Properties();

        if (PropertyUtil.loadProperties(properties, "application.properties")) return;

        String request = properties.getProperty("sample.request");
        ObjectMapper objectMapper = ObjectUtil.getObjectMapper();
        UserRequest userRequest = objectMapper.readValue(request, UserRequest.class);
        // From Request Logic
        Optional<UserEntity> userEntity = UserMapper.userToEntity(userRequest);
        if (userEntity.isPresent()) {
            log.info(userEntity.get().toString());
            // Perform some more logic
            // To Response Logic
            Optional<UserResponse> response = UserMapper.userToDto(userEntity.get());
            if (response.isPresent()) {
                log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.get()));
                return;
            }
        }
        log.severe("Error in converting UserEntity to UserResponse");
    }
}