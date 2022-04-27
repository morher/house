package net.morher.house.wled.presets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.morher.house.wled.LedStripState;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Preset {
    private String name;
    private LedStripState state;
}
