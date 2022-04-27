package net.morher.house.wled.frontend;

import net.morher.house.wled.LedColor;
import net.morher.house.wled.LedStripState;

public class LedStripStateTO {
    public Boolean powerOn;

    public Integer brightness;

    public LedColor color1;

    public LedColor color2;

    public LedColor color3;

    public Integer effect;

    public Integer speed;

    public Integer intensity;

    public Integer palette;

    public Boolean selected;

    public static LedStripStateTO from(LedStripState state) {
        return new LedStripStateTO();
    }
}
