package net.morher.house.api.mqtt.client;

import lombok.Data;

@Data
public class Message {
    private final String topic;
    private final byte[] payload;
    private final int qos;
    private final boolean retain;
}