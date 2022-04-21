package net.morher.house.api.mqtt.client;

import lombok.Data;

@Data
public class MqttOptions {
    private String serverUrl;
    private String username;
    private String password;
    private String clientId;
    private int reconnectInterval = 10; // Seconds
}
