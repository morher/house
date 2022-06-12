package net.morher.house.api.mqtt.payload;

import java.io.IOException;
import java.util.function.Function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public class JsonMessage {
    public static PayloadFormat<JsonNode> toJsonNode() {
        return new JsonMapper<>(new ObjectMapper().readerFor(JsonNode.class));
    }

    public static <T> PayloadFormat<T> toType(Class<T> targetClass) {
        return new JsonMapper<>(new ObjectMapper().readerFor(targetClass));
    }

    public static <T> PayloadFormat<T> toType(TypeReference<T> typeReference) {
        return new JsonMapper<>(new ObjectMapper().readerFor(typeReference));
    }

    public static <T> PayloadFormat<T> toListOf(Class<T> itemClass) {
        return new JsonMapper<>(new ObjectMapper().readerForListOf(itemClass));
    }

    public static <T> PayloadFormat<T> toMapOf(Class<T> valueClass) {
        return new JsonMapper<>(new ObjectMapper().readerForMapOf(valueClass));
    }

    private static class JsonMapper<T> implements Function<byte[], T>, PayloadFormat<T> {
        private final ObjectMapper mapper = new ObjectMapper();
        private final ObjectReader reader;

        public JsonMapper(ObjectReader reader) {
            this.reader = reader;
        }

        @Override
        public T apply(byte[] payload) {
            return deserialize(payload);
        }

        @Override
        public byte[] serialize(T object) {
            try {
                return mapper.writeValueAsBytes(object);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize message", e);
            }
        }

        @Override
        public T deserialize(byte[] payload) {
            try {
                return reader.readValue(payload);

            } catch (IOException e) {
                throw new RuntimeException("Failed to deserialize message", e);
            }
        }

    }
}
