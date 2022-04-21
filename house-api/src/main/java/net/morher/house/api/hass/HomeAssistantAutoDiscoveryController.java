package net.morher.house.api.hass;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import net.morher.house.api.entity.Entity;
import net.morher.house.api.entity.EntityListener;
import net.morher.house.api.mqtt.client.HouseMqttClient;

@Slf4j
public class HomeAssistantAutoDiscoveryController implements EntityListener {
    private final List<EntityAnnouncer> announcers = new ArrayList<>();

    public HomeAssistantAutoDiscoveryController(HouseMqttClient mqtt) {
        announcers.add(new SwitchEntityAnnouncer(mqtt));
    }

    @Override
    public void onEntityUpdated(Entity entity) {
        if (entity.getDeviceInfo() != null) {
            try {
                for (EntityAnnouncer announcer : announcers) {
                    announcer.announce(entity);
                }
            } catch (Exception e) {
                log.error("Exception while announcing entity {}", entity, e);
            }
        }
    }
}
