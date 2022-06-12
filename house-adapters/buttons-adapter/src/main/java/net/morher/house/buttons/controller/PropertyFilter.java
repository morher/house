package net.morher.house.buttons.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;
import net.morher.house.api.mqtt.client.MqttMessageListener.ParsedMqttMessageListener;

@Slf4j
public class PropertyFilter implements ParsedMqttMessageListener<JsonNode> {
    private final String[] pathArr;
    private final ParsedMqttMessageListener<String> listener;

    public PropertyFilter(String path, ParsedMqttMessageListener<String> listener) {
        this.pathArr = path.split("\\.");
        this.listener = listener;
    }

    @Override
    public void onMessage(String topic, JsonNode root, int qos, boolean retained) {
        log.trace("Received message: {}", root);
        JsonNode currentNode = root;
        for (String key : pathArr) {
            if (!(currentNode instanceof ObjectNode)) {
                log.trace("Current node is not an object {}", currentNode);
                return;
            }
            ObjectNode node = (ObjectNode) currentNode;
            currentNode = node.get(key);
            if (currentNode == null) {
                log.trace("Key '{}' not resent in {} ", key, currentNode);
                return;
            }
        }
        String data = currentNode.asText();
        log.trace("Forward value to listener: {}", data);
        listener.onMessage(topic, data, qos, retained);
    }

}