package org.asr.experiments;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.asr.experiments.dto.request.UserRequest;
import org.asr.experiments.dto.response.UserResponse;
import org.asr.experiments.entity.UserEntity;
import org.asr.experiments.mapper.UserMapperV2;
import org.asr.experiments.util.ObjectUtil;
import org.asr.experiments.util.PropertyUtil;

import java.io.IOException;
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
        UserEntity userEntity = UserMapperV2.INSTANCE.userToEntity(userRequest);
        if (userEntity != null) {
            log.info(userEntity.toString());
            // Perform some more logic
            // To Response Logic
            UserResponse response = UserMapperV2.INSTANCE.userToDto(userEntity);
            if (response != null) {
                log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
                return;
            }
        }
        log.severe("Error in converting UserEntity to UserResponse");
    }
}