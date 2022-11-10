package net.morher.house.api.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MqttConfiguration {
    private String serverUrl;
    private String username;
    private String password;
    private String clientIdPrefix;
}
