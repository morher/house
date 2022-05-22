package net.morher.house.buttons.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.morher.house.api.config.DeviceName;
import net.morher.house.api.entity.DeviceId;

public class DeviceDefaults {
    private final List<DeviceName> defaultDevices;

    public DeviceDefaults(List<DeviceName> defaultDevices) {
        this.defaultDevices = defaultDevices;
    }

    public List<DeviceId> getDevices(
            Collection<DeviceName> specifiedDevices,
            Collection<String> references) {

        List<DeviceId> devices = new ArrayList<>();
        for (DeviceName deviceName : specifiedDevices) {
            devices.add(deviceName.toDeviceId());
        }
        // TODO: references
        if (specifiedDevices.isEmpty()
                && references.isEmpty()) {
            for (DeviceName deviceName : defaultDevices) {
                devices.add(deviceName.toDeviceId());
            }
        }

        return devices;
    }

}
