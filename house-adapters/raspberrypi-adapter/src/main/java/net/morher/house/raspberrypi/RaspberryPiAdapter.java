package net.morher.house.raspberrypi;

import net.morher.house.api.context.HouseMqttContext;
import net.morher.house.api.entity.DeviceManager;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.raspberrypi.blinds.BlindsRemoteController;
import net.morher.house.raspberrypi.config.RaspberryPiAdapterConfig;
import net.morher.house.raspberrypi.config.RaspberryPiConfig;

public class RaspberryPiAdapter {
    public static void main(String[] args) throws Exception {
        new RaspberryPiAdapter().run(new HouseMqttContext("raspberrypi-adapter"));
    }

    public void run(HouseMqttContext ctx) {
        HouseMqttClient client = ctx.client();
        DeviceManager deviceManager = ctx.deviceManager();

        RaspberryPiConfig config = ctx.loadAdapterConfig(RaspberryPiAdapterConfig.class).getRaspberrypi();

        new BlindsRemoteController(deviceManager)
                .configure(config.getBlindsRemotes());
        ;
    }

}
