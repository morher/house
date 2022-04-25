package net.morher.house.api.entity.light;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.With;

@With
@Data
@JsonInclude(Include.NON_NULL)
public class LightState {
    private final PowerState state;
    /**
     * Brightness given as a value between 0 and 255
     */
    private final Integer brightness;
    private final String effect;

    @JsonCreator(mode = Mode.PROPERTIES)
    public LightState(
            @JsonProperty("state") PowerState state,
            @JsonProperty("brightness") Integer brightness,
            @JsonProperty("effect") String effect) {

        this.state = state;
        this.brightness = brightness;
        this.effect = effect;
    }

    public LightState() {
        this(null, null, null);
    }

    public static LightState defaultState() {
        return new LightState(PowerState.OFF, 255, null);
    }

    public LightState modify(LightState updatedState) {
        LightState updated = this;
        if (updatedState.getState() != null) {
            updated = updated.withState(updatedState.getState());
        }
        if (updatedState.getBrightness() != null) {
            updated = updated.withBrightness(updatedState.getBrightness());
        }
        if (updatedState.getEffect() != null) {
            updated = updated.withEffect(updatedState.getEffect());
        }
        return updated;
    }

    public enum PowerState {
        ON, OFF;

        public PowerState toggle() {
            return this == ON
                    ? OFF
                    : ON;
        }
    }
}
