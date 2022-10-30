package net.morher.house.presence.room;

import static net.morher.house.api.config.DeviceName.combine;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import lombok.RequiredArgsConstructor;
import net.morher.house.api.config.DeviceName;
import net.morher.house.api.entity.Device;
import net.morher.house.api.entity.DeviceId;
import net.morher.house.api.entity.DeviceInfo;
import net.morher.house.api.entity.DeviceManager;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.MqttTopicManager;
import net.morher.house.api.mqtt.payload.BooleanMessage;
import net.morher.house.presence.config.PresenceConfig;
import net.morher.house.presence.config.PresenceConfig.MotionSensorConfig;
import net.morher.house.presence.config.PresenceConfig.RoomConfig;

@RequiredArgsConstructor
public class RoomPresenceController {
    private final ScheduledExecutorService scheduler;
    private final DeviceManager deviceManager;
    private final HouseMqttClient mqtt;

    public void configure(PresenceConfig config) {
        for (Map.Entry<String, RoomConfig> room : config.getRooms().entrySet()) {
            configureRoom(room.getKey(), room.getValue());
        }
    }

    private void configureRoom(String roomName, RoomConfig config) {
        DeviceId deviceId = combine(
                config.getDevice(),
                new DeviceName(roomName, "Presence detector"))
                        .toDeviceId();

        Device device = deviceManager.device(deviceId);
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setManufacturer("House");
        deviceInfo.setModel("Presence Adapter");
        device.setDeviceInfo(deviceInfo);

        Room room = new Room(scheduler, device)
                .allExitsObserved(config.isAllExitsObserved())
                .cooldown(Duration.ofSeconds(config.getCooldown()));
        configureMotionSensors(room, config.getMotionSensors());
    }

    private void configureMotionSensors(Room room, Collection<MotionSensorConfig> motionSensors) {
        for (MotionSensorConfig sensorConfig : motionSensors) {
            configureMotionSensor(room, sensorConfig);
        }
    }

    private void configureMotionSensor(Room room, MotionSensorConfig config) {
        MqttTopicManager<Boolean> topic = mqtt.topic(config.getTopic(), BooleanMessage.onOff());
        room.addMotionSensor(new MotionSensor(topic, scheduler, config.getOnDelayMs(), config.getOffDelayMs()));
    }

}
