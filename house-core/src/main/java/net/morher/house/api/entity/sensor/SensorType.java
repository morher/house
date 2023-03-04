package net.morher.house.api.entity.sensor;

public enum SensorType {
  BATTERY("battery", "%"),
  HUMIDITY("humidity", "%"),
  ILLUMINANCE_LM("illuminance", "lm"),
  ILLUMINANCE_LX("illuminance", "lx"),
  TEMPERATURE_C("temperature", "�C"),
  DURATION_S("duration", "seconds"),
  DURATION_M("duration", "minutes"),
  DURATION_H("duration", "hours"),
  DURATION_D("duration", "days"),
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
