package net.morher.house.api.hass;

import static java.util.Objects.requireNonNullElse;
import static java.util.Objects.requireNonNullElseGet;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.morher.house.api.entity.DeviceId;
import net.morher.house.api.entity.DeviceInfo;
import net.morher.house.api.entity.Entity;
import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.common.ConfigurableEntity;
import net.morher.house.api.entity.common.EntityOptions;
import net.morher.house.api.mqtt.MqttNamespace;
import net.morher.house.api.mqtt.client.HouseMqttClient;

@Slf4j
public abstract class BaseEntityAnnouncer<E extends Entity> implements EntityAnnouncer {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Map<String, String> DEFAULT_ICONS = buildDefaultIconsMap();
    private final HouseMqttClient mqtt;
    private final Class<E> entityType;

    private static Map<String, String> buildDefaultIconsMap() {
        HashMap<String, String> icons = new HashMap<>();
        icons.put("Power", "hass:power");
        icons.put("AV-mute", "hass:projector-screen-off");
        icons.put("Volume", "hass:volume-high");

        return icons;
    }

    public BaseEntityAnnouncer(HouseMqttClient mqtt, Class<E> entityType) {
        this.mqtt = mqtt;
        this.entityType = entityType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void announce(Entity entity) {
        if (entityType.isInstance(entity)) {
            announceEntity((E) entity);
        }
    }

    protected abstract void announceEntity(E entity);

    protected final void announceEntity(BaseEntityConfig config) {
        log.info("Announcing: {}", config.getName());
        mqtt.publish(entityAnnouncementTopic(config), encodePayload(config), true);
    }

    private String entityAnnouncementTopic(BaseEntityConfig config) {
        return "homeassistant/" + config.getEntityClass() + "/" + config.getUniqueId() + "/config";
    }

    private byte[] encodePayload(BaseEntityConfig config) {
        try {
            return mapper.writeValueAsBytes(config);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid payload", e);
        }
    }

    protected void fillDefaults(ConfigurableEntity<?> entity, BaseEntityConfig entityConfig) {
        entityConfig.setDevice(deviceConfig(entity));
        entityConfig.setName(entityIdToName(entity.getId()));
        entityConfig.setUniqueId(entityIdToUniqueId(entity.getId()));
        entityConfig.setAvailabilityTopic(clientAvailabilityTopic());
        entityConfig.setIcon(icon(entity, DEFAULT_ICONS));
    }

    protected String clientAvailabilityTopic() {
        return mqtt.getAvailabilityTopic();
    }

    protected DeviceConfig deviceConfig(Entity entity) {
        DeviceInfo deviceInfo = entity.getDeviceInfo();
        DeviceId deviceId = entity.getId().getDevice();
        DeviceConfig device = new DeviceConfig();
        device.setName(requireNonNullElseGet(deviceInfo.getName(), () -> deviceIdToName(deviceId)));
        device.setManufacturer(requireNonNullElse(deviceInfo.getManufacturer(), "House"));
        device.setModel(requireNonNullElse(deviceInfo.getModel(), "MQTT Adapter"));
        device.setArea(requireNonNullElse(deviceInfo.getSuggestedArea(), deviceId.getRoomName()));
        device.getIdentifiers().add(device.getName());
        return device;
    }

    protected String icon(ConfigurableEntity<?> entity, Map<String, String> defaultIcons) {
        EntityOptions options = entity.getOptions();
        if (options != null && options.getIcon() != null) {
            return "hass:" + options.getIcon();
        }
        return defaultIcons.get(entity.getId().getEntity());
    }

    public static String deviceIdToUniqueId(DeviceId id) {
        StringBuilder sb = new StringBuilder();
        if (id.getRoomName() != null) {
            sb.append(MqttNamespace.normalize(id.getRoomName()));
            sb.append("-");
        }
        sb.append(MqttNamespace.normalize(id.getDeviceName()));
        return sb.toString();
    }

    public static String entityIdToUniqueId(EntityId id) {
        String deviceUniqueId = deviceIdToUniqueId(id.getDevice());
        if (id.getEntity() != null) {
            return deviceUniqueId + "-" + MqttNamespace.normalize(id.getEntity());
        }
        return deviceUniqueId;
    }

    public static String deviceIdToName(DeviceId id) {
        StringBuilder sb = new StringBuilder();
        if (id.getRoomName() != null) {
            sb.append(id.getRoomName());
            sb.append(" - ");
        }
        sb.append(id.getDeviceName());
        return sb.toString();
    }

    public static String entityIdToName(EntityId id) {
        String deviceName = deviceIdToName(id.getDevice());
        if (id.getEntity() != null) {
            return deviceName + " - " + id.getEntity();
        }
        return deviceName;
    }
}
