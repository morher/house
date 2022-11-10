package net.morher.house.api.entity.sensor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.morher.house.api.entity.EntityCategory;
import net.morher.house.api.entity.common.EntityOptions;

@Data
@EqualsAndHashCode(callSuper = true)
public class BinarySensorOptions extends EntityOptions {
    private String deviceClass;
    private EntityCategory category;

    public BinarySensorOptions(BinarySensorType type) {
        this.deviceClass = type.getSensorDeviceClass();
    }

    public BinarySensorOptions(BinarySensorType type, EntityCategory category) {
        this(type);
        this.category = category;
    }
}
