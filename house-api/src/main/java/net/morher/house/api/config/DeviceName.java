package net.morher.house.api.config;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.morher.house.api.entity.DeviceId;
import net.morher.house.api.entity.EntityId;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DeviceName {
    private String room;
    private String name;
    private String entity;

    public DeviceName(String room, String name) {
        this(room, name, null);
    }

    public DeviceId toDeviceId() {
        return new DeviceId(room, name);
    }

    public EntityId toEntityId(String defaultEntityName) {
        return new EntityId(toDeviceId(), Objects.requireNonNullElse(entity, defaultEntityName));
    }

    public static DeviceName combine(DeviceName... deviceNames) {
        String room = null;
        String name = null;
        String entity = null;

        for (DeviceName deviceName : deviceNames) {
            if (deviceName != null) {
                if (deviceName.getRoom() != null && room == null) {
                    room = deviceName.getRoom();
                }
                if (deviceName.getName() != null && name == null) {
                    name = deviceName.getName();
                }
                if (deviceName.getEntity() != null && entity == null) {
                    entity = deviceName.getEntity();
                }
            }
        }
        return new DeviceName(room, name, entity);
    }
}
