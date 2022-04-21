package net.morher.house.api.config;

import java.io.File;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class ConfigurationLoader {
    private final String configFile;

    public ConfigurationLoader(String configFile) {
        this.configFile = configFile;
    }

    public <X> X loadConfig(Class<X> configClass) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(new File(configFile), configClass);

        } catch (Exception e) {
            throw new RuntimeException("Cannot load config", e);
        }
    }
}