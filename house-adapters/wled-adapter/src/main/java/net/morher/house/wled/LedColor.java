package net.morher.house.wled;

import org.silentsoft.csscolor4j.Color;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class LedColor {
    private final Color color;

    @JsonCreator
    public LedColor(String cssColor) {
        color = Color.valueOf(cssColor);
    }

    public int getRed() {
        return color.getRed();
    }

    public int getBlue() {
        return color.getBlue();
    }

    public int getGreen() {
        return color.getGreen();
    }

    @Override
    @JsonValue
    public String toString() {
        return color.getHex();
    }
}
