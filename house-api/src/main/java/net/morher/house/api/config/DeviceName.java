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
}
