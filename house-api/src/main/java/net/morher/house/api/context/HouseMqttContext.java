package net.morher.house.api.context;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import net.morher.house.api.config.ConfigurationLoader;
import net.morher.house.api.config.MqttConfiguration;
import net.morher.house.api.device.DeviceManager;
import net.morher.house.api.entity.EntityManager;
import net.morher.house.api.hass.HomeAssistantAutoDiscoveryController;
import net.morher.house.api.mqtt.MqttNamespace;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.HouseMqttClientImpl;
import net.morher.house.api.mqtt.client.MqttOptions;

public class HouseMqttContext {
    private static final String ENV_CONFIG_PATH = "HOUSE_CONFIG_PATH";
    private static final String ENV_CONFIG_MQTT = "HOUSE_CONFIG_MQTT";
    private static final String ENV_CONFIG_ADAPTER = "HOUSE_CONFIG_ADAPTER";
    private String adapterName;

    private HouseMqttClient client;
    private HomeAssistantAutoDiscoveryController hass;
    private EntityManager entityManager;

    private DeviceManager deviceManager;

    public static MqttOptions loadDefaultOptions(String adapterName) {
        MqttConfiguration config = new ConfigurationLoader(configFile(ENV_CONFIG_MQTT, "mqtt-client.yaml").toString())
                .loadConfig(MqttConfiguration.class);

        MqttOptions options = new MqttOptions();
        options.setServerUrl(config.getServerUrl());
        options.setUsername(config.getUsername());
        options.setPassword(config.getPassword());
        String clientId = adapterName;
        if (config.getClientIdPrefix() != null) {
            clientId = config.getClientIdPrefix() + "-" + clientId;
        }
        options.setClientId(clientId);
        return options;
    }

    public HouseMqttContext(String adapterName) {
        this(loadDefaultOptions(adapterName));
        this.adapterName = adapterName;
    }

    public HouseMqttContext(MqttOptions options) {
        client = new HouseMqttClientImpl(options, MqttNamespace.defaultNamespace());
    }

    public HouseMqttClient client() {
        return client;
    }

    public HomeAssistantAutoDiscoveryController homeAssistantAutoDiscoveryController() {
        if (hass == null) {
            hass = new HomeAssistantAutoDiscoveryController(client());
        }
        return hass;
    }

    public EntityManager entityManager() {
        if (entityManager == null) {
            entityManager = new EntityManager(client);
            entityManager.entityChanges().subscribe(homeAssistantAutoDiscoveryController());
        }
        return entityManager;
    }

    public DeviceManager deviceManager() {
        if (deviceManager == null) {
            deviceManager = new DeviceManager(entityManager());
        }
        return deviceManager;
    }

    public <T> T loadAdapterConfig(Class<T> configType) {
        return new ConfigurationLoader(configFile(ENV_CONFIG_ADAPTER, adapterName + ".yaml").toString())
                .loadConfig(configType);
    }

    private static Path configFile(String configFileEnv, String defaultFilename) {
        Path filePath = Paths.get(Objects.requireNonNullElse(System.getenv(configFileEnv), defaultFilename));

        if (!filePath.isAbsolute()) {
            String configDir = System.getenv(ENV_CONFIG_PATH);
            if (configDir != null) {
                return Paths.get(configDir).resolve(filePath);
            }
        }
        return filePath;
    }
}
