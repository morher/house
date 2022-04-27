package net.morher.house.wled;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import net.morher.house.api.device.Device;
import net.morher.house.api.device.DeviceId;
import net.morher.house.api.device.DeviceManager;
import net.morher.house.api.entity.light.LightEntity;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.MqttTopicManager;
import net.morher.house.api.mqtt.payload.JsonMessage;
import net.morher.house.api.schedule.HouseScheduler;
import net.morher.house.wled.config.WledConfiguration;
import net.morher.house.wled.config.WledConfiguration.WledLampConfig;
import net.morher.house.wled.presets.PresetManager;
import net.morher.house.wled.presets.PresetManagerImpl;
import net.morher.house.wled.to.WledNodeState;

public class WledControllerImpl {
    private final ScheduledExecutorService scheduler = HouseScheduler.get("wled-controller");
    private final HouseMqttClient mqtt;
    private final DeviceManager deviceManager;
    private final Map<String, WledLedStrip> strips = new HashMap<>();
    private final PresetManager presets = new PresetManagerImpl();

    public WledControllerImpl(HouseMqttClient mqtt, DeviceManager deviceManager) {
        this.mqtt = mqtt;
        this.deviceManager = deviceManager;
    }

    public void configure(WledConfiguration config) {
        scheduler.execute(() -> performConfiguration(config));
    }

    private void performConfiguration(WledConfiguration config) {
        presets.configure(config.getPresets());

        for (WledLampConfig lampConfig : config.getLamps()) {
            String id = lampConfig.getId();

            DeviceId deviceId = lampConfig.getDevice().toDeviceId();
            Device<LightEntity> lamp = deviceManager.lightDevice(deviceId);

            WledNode node = findOrCreateNode(lampConfig.getTopic());
            WledLedStrip strip = new WledLedStrip(id, deviceId, node, lampConfig.getSegment(), lamp.getMainEntity(), presets);

            strips.put(id, strip);
        }
    }

    private WledNode findOrCreateNode(String topic) {
        return new WledNode(new MqttTopicManager<>(mqtt, topic + "/api", JsonMessage.toType(WledNodeState.class), null));
    }

    public WledLedStrip getStrip(String stripId) {
        WledLedStrip strip = strips.get(stripId);
        if (strip == null) {
            throw new IllegalArgumentException("Fant ikke wled-strip: " + stripId);
        }
        return strip;
    }

    public List<WledLedStrip> getStrips() {
        return new ArrayList<>(strips.values());
    }
}
