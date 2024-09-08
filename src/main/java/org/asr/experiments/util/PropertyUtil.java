package org.asr.experiments.util;

import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;
import org.asr.experiments.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Log
@UtilityClass
public class PropertyUtil {

    public static boolean loadProperties(Properties properties, String propertiesFilePath) throws IOException {
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream(propertiesFilePath)) {
            if (input == null) {
                log.info("Sorry, unable to find application.properties");
                return true;
            }
            properties.load(input);
        }
        return false;
    }
}
