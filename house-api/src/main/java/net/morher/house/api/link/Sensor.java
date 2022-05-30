package net.morher.house.api.link;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.morher.house.api.entity.sensor.SensorType;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Sensor {
    String entity();

    SensorType type() default SensorType.NONE;
}
