package net.morher.house.api.entity.sensor;

public enum SensorType {
  ANGLE(null, "°"),
  BATTERY("battery", "%"),
  CURRENT("current", "A"),
  CURRENT_MA("current", "mA"),
  DURATION_S("duration", "seconds"),
  DURATION_M("duration", "minutes"),
  DURATION_H("duration", "hours"),
  DURATION_D("duration", "days"),
  HUMIDITY("humidity", "%"),
  ILLUMINANCE_LM("illuminance", "lm"),
  ILLUMINANCE_LX("illuminance", "lx"),
  POWER("power", "W"),
  POWER_KW("power", "kW"),
  TEMPERATURE_C("temperature", "°C"),
  VOLTAGE("voltage", "V"),
  VOLTAGE_MV("voltage", "mV"),
  NONE(null, null);

  private final String sensorDeviceClass;

  private final String unit;

  private SensorType(String sensorDeviceClass, String unit) {
    this.sensorDeviceClass = sensorDeviceClass;
    this.unit = unit;
  }

  public String getSensorDeviceClass() {
    return sensorDeviceClass;
  }

  public String getUnit() {
    return unit;
  }
}
