package net.morher.house.tasmota.lamp;

import lombok.RequiredArgsConstructor;
import net.morher.house.api.devicetypes.LampDevice;
import net.morher.house.api.entity.DeviceId;
import net.morher.house.api.entity.DeviceManager;
import net.morher.house.api.entity.light.LightEntity;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.tasmota.config.TasmotaConfiguration;
import net.morher.house.tasmota.config.TasmotaConfiguration.DimmerChannel;
import net.morher.house.tasmota.config.TasmotaConfiguration.LampConfiguration;

@RequiredArgsConstructor
public class TasmotaLampController {
    private final HouseMqttClient client;
    private final DeviceManager deviceManager;

    public void configure(TasmotaConfiguration config) {
        for (LampConfiguration lampConfiguration : config.getLamps()) {
            configure(lampConfiguration);
        }
    }

    private void configure(LampConfiguration config) {
        DeviceId deviceId = config.getDevice().toDeviceId();

        LightEntity light = deviceManager.device(deviceId).entity(LampDevice.LIGHT);

        TasmotaLampDevice lamp = new TasmotaLampDevice(config.getNode(), light, client);
        for (DimmerChannel dimmer : config.getDimmers()) {
            lamp.addDimmerChannel(dimmer.getDimmer(), dimmer.getChannel());
        }
    }
}
