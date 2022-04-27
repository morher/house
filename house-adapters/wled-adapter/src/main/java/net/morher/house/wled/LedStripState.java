package net.morher.house.wled;

import java.util.function.Consumer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class LedStripState {
    private Boolean powerOn;
    private Integer brightness;
    private LedColor color1;
    private LedColor color2;
    private LedColor color3;
    private Integer effect;
    private Integer speed;
    private Integer intensity;
    private Integer palette;
    private Boolean selected;

    public void apply(LedStripState state, boolean styleOnly) {
        if (!styleOnly) {
            useIfNotNull(state.getPowerOn(), this::setPowerOn);
            useIfNotNull(state.getColor1(), this::setColor1);
        }
        useIfNotNull(state.getColor1(), this::setColor1);
        useIfNotNull(state.getColor2(), this::setColor2);
        useIfNotNull(state.getColor3(), this::setColor3);
        useIfNotNull(state.getEffect(), this::setEffect);
        useIfNotNull(state.getSpeed(), this::setSpeed);
        useIfNotNull(state.getIntensity(), this::setIntensity);
        useIfNotNull(state.getPalette(), this::setPalette);
    }

    private static <T> void useIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
