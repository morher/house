package net.morher.house.api.entity.sensor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BinarySensorType {
    BATTERY("battery"),
    BATTERY_CHARGING("battery_charging"),
    CARBON_MONOXIDE("carbon_monoxide"),
    COLD("cold"),
    CONNECTIVITY("connectivity"),
    DOOR("door"),
    GARAGE_DOOR("garage_door"),
    GAS("gas"),
    HEAT("heat"),
    LIGHT("light"),
    LOCK("lock"),
    MOISTURE("moisture"),
    MOTION("motion"),
    MOVING("moving"),
    OCCUPANCY("occupancy"),
    OPENING("opening"),
    PLUG("plug"),
    POWER("power"),
    PRESENCE("presence"),
    PROBLEM("problem"),
    RUNNING("running"),
    SAFETY("safety"),
    SMOKE("smoke"),
    SOUND("sound"),
    TAMPER("tamper"),
    UPDATE("update"),
    VIBRATION("vibration"),
    WINDOW("window");

    private final String sensorDeviceClass;
}
