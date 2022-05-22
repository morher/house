package net.morher.house.api.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.morher.house.api.entity.DeviceId;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DeviceName {
    private String room;
    private String name;

    public DeviceId toDeviceId() {
        return new DeviceId(room, name);
    }
}
