package net.morher.house.shelly.controller;

import net.morher.house.api.devicetypes.GeneralDevice;
import net.morher.house.api.devicetypes.LampDevice;
import net.morher.house.api.entity.Device;
import net.morher.house.api.entity.DeviceId;
import net.morher.house.api.entity.DeviceInfo;
import net.morher.house.api.entity.DeviceManager;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.shelly.config.ShellyConfig;
import net.morher.house.shelly.config.ShellyConfig.ShellyLampConfig;
import net.morher.house.shelly.config.ShellyConfig.ShellyNodeConfig;
import net.morher.house.shelly.config.ShellyConfig.ShellyRelayConfig;
import net.morher.house.shelly.config.ShellyConfig.ShellySwitchConfig;

public class ShellyController {
    private final HouseMqttClient client;
    private final DeviceManager deviceManager;

    public ShellyController(HouseMqttClient client, DeviceManager deviceManager) {
        this.client = client;
        this.deviceManager = deviceManager;
    }

    public void configure(ShellyConfig config) {
        config.getNodes()
                .forEach(this::configureNode);
    }

    private void configureNode(String nodeName, ShellyNodeConfig config) {
        configureRelay(nodeName, 0, config.getRelay0());
        configureRelay(nodeName, 1, config.getRelay1());
    }

    private void configureRelay(String nodeName, int relayIndex, ShellyRelayConfig relayConfig) {
        if (relayConfig == null) {
            return;
        }
        configureLamp(nodeName, relayIndex, relayConfig.getLamp());
        configureSwitch(nodeName, relayIndex, relayConfig.getSwitchConfig());
    }

    private void configureLamp(String nodeName, int relayIndex, ShellyLampConfig lampConfig) {
        if (lampConfig == null) {
            return;
        }
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setManufacturer("Shelly");

        DeviceId deviceId = lampConfig.getDevice().toDeviceId();
        Device device = deviceManager.device(deviceId);
        device.setDeviceInfo(deviceInfo);

        new ShellyLamp(client, nodeName, relayIndex, device.entity(LampDevice.LIGHT));
    }

    private void configureSwitch(String nodeName, int relayIndex, ShellySwitchConfig switchConfig) {
        if (switchConfig == null) {
            return;
        }
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setManufacturer("Shelly");

        Device device = deviceManager.device(switchConfig.getDevice().toDeviceId());
        device.setDeviceInfo(deviceInfo);

        new ShellySwitch(client, nodeName, relayIndex, device.entity(GeneralDevice.POWER));
    }
}
