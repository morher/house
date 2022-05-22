package net.morher.house.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class DeviceId {
    private final String roomName;
    private final String deviceName;

    public DeviceId(String roomName, String deviceName) {
        if (deviceName == null || deviceName.isBlank()) {
            throw new IllegalArgumentException("Device name cannot be null or blank");
        }
        if (roomName != null && roomName.isBlank()) {
            throw new IllegalArgumentException("Room name can be null, but not blank");
        }
        this.roomName = roomName;
        this.deviceName = deviceName;
    }

}
