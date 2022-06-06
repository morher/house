package net.morher.house.epson.projector;

import net.morher.house.api.devicetypes.AudioVideoDevice;
import net.morher.house.api.devicetypes.GeneralDevice;
import net.morher.house.api.entity.Device;
import net.morher.house.api.entity.DeviceInfo;
import net.morher.house.api.entity.number.DecimalEntity;
import net.morher.house.api.entity.number.NumberOptions;
import net.morher.house.api.entity.sensor.SensorEntity;
import net.morher.house.api.entity.sensor.SensorOptions;
import net.morher.house.api.entity.sensor.SensorType;
import net.morher.house.api.entity.switches.SwitchEntity;
import net.morher.house.api.entity.switches.SwitchOptions;

public class EpsonProjectorDevice {
    private final SwitchEntity power;
    private final SwitchEntity avmute;
    private final DecimalEntity volume;
    private final SensorEntity<Integer> lampHours;

    public EpsonProjectorDevice(Device device) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setManufacturer("Epson");
        device.setDeviceInfo(deviceInfo);

        power = device.entity(GeneralDevice.POWER, new SwitchOptions());
        avmute = device.entity(AudioVideoDevice.AV_MUTE, new SwitchOptions());
        volume = device.entity(AudioVideoDevice.VOLUME, new NumberOptions(0.0, 1.0, .05));
        lampHours = device.entity(AudioVideoDevice.LAMP_HOURS, new SensorOptions(SensorType.DURATION_H));
    }

    public void publishPower(boolean powerState) {
        this.power.publishState(powerState);
    }

    public void publishAvMute(boolean avmuteState) {
        this.avmute.publishState(avmuteState);
    }

    public void publishVolume(double volume) {
        this.volume.publishState(volume);
    }

    public void publishLampHours(int lampHours) {
        this.lampHours.publishState(lampHours);
    }

    public void registerCommandListener(ProjectorManager listener) {
        power.command().subscribe(listener::commandPower);
        avmute.command().subscribe(listener::commandAvMute);
        volume.command().subscribe(listener::commandVolume);

    }
}
