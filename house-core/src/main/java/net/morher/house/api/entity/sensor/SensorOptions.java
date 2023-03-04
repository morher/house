package net.morher.house.api.entity.sensor;

import net.morher.house.api.entity.EntityCategory;
import net.morher.house.api.entity.common.EntityOptions;

public class SensorOptions extends EntityOptions {
  private String deviceClass;
  private String unit;
  private EntityCategory category;

  public SensorOptions(SensorType sensorType) {
    this.deviceClass = sensorType.getSensorDeviceClass();
    this.unit = sensorType.getUnit();
  }

  public SensorOptions(SensorType sensorType, EntityCategory category) {
    this(sensorType);
    this.category = category;
  }

  public SensorOptions(String deviceClass, String unit) {
    this.deviceClass = deviceClass;
    this.unit = unit;
  }

  public String getDeviceClass() {
    return deviceClass;
  }

  public void setDeviceClass(String deviceClass) {
    this.deviceClass = deviceClass;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public EntityCategory getCategory() {
    return category;
  }

  public void setCategory(EntityCategory category) {
    this.category = category;
  }
}
