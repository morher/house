package net.morher.house.test.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestConfigLoader<T> {
    private final Class<T> configClass;

    public T fromYaml(String yaml) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(yaml, configClass);

        } catch (Exception e) {
            throw new RuntimeException("Cannot load config", e);
        }
    }
}
