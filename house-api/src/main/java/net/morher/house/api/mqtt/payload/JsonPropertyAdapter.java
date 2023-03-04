package net.morher.house.api.mqtt.payload;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonPropertyAdapter<T> implements PayloadFormat<T> {
  private static final PayloadFormat<JsonNode> JSON_NODE = JsonMessage.toJsonNode();
  private final PayloadFormat<T> format;
  private final String[] pathArr;

  public JsonPropertyAdapter(PayloadFormat<T> format, String path) {
    this.format = format;
    this.pathArr = path.split("\\.");
  }

  @Override
  public byte[] serialize(T value) {
    throw new UnsupportedOperationException("Cannot serialize into a JSON property");
  }

  @Override
  public T deserialize(byte[] payload) {
    JsonNode currentNode = JSON_NODE.deserialize(payload);
    for (String key : pathArr) {
      if (!(currentNode instanceof ObjectNode)) {
        log.trace("Current node is not an object {}", currentNode);
        return null;
      }
      ObjectNode node = (ObjectNode) currentNode;
      currentNode = node.get(key);
      if (currentNode == null) {
        log.trace("Key '{}' not resent in {} ", key, currentNode);
        return null;
      }
    }
    return format.deserializeFromJson(currentNode);
  }
}
