package net.morher.house.wled;

import net.morher.house.api.context.HouseAdapter;
import net.morher.house.api.context.HouseMqttContext;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.wled.config.WledAdapterConfiguration;
import net.morher.house.wled.config.WledConfiguration;
import net.morher.house.wled.frontend.WledFrontendServer;
import net.morher.house.wled.frontend.auth.SimpleUserManager;

public class WledAdapter implements HouseAdapter {

    public static void main(String[] args) throws Exception {
        new WledAdapter().run(new HouseMqttContext("wled-adapter"));
    }

    @Override
    public void run(HouseMqttContext ctx) {
        HouseMqttClient client = ctx.client();
        WledConfiguration config = ctx.loadAdapterConfig(WledAdapterConfiguration.class).getWled();

        WledControllerImpl wled = new WledControllerImpl(client, ctx.deviceManager());
        wled.configure(config);

        WledFrontendServer frontend = new WledFrontendServer(wled, new SimpleUserManager(config));
        frontend.run();
    }

}
