package net.morher.house.api.mqtt.payload;

import com.fasterxml.jackson.databind.JsonNode;

public interface PayloadFormat<T> {
  byte[] serialize(T value);

  T deserialize(byte[] payload);

  default T deserializeFromJson(JsonNode jsonNode) {
    return deserialize(jsonNode.asText().getBytes());
  }

  default PayloadFormat<T> inJsonField(String fieldPath) {
    if (fieldPath == null || fieldPath.isBlank()) {
      return this;
    }
    return new JsonPropertyAdapter<>(this, fieldPath);
  }
}
