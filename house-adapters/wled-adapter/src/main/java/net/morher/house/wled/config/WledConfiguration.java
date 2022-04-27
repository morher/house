package net.morher.house.wled.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import net.morher.house.api.config.DeviceName;
import net.morher.house.wled.LedColor;

@Data
public class WledConfiguration {
    private List<WledLampConfig> lamps = new ArrayList<>();
    private Map<String, WledPresetConfig> presets = new HashMap<>();

    @Data
    public static class WledLampConfig {
        private String id;
        private DeviceName device;
        private String topic;
        private Integer segment;
    }

    @Data
    public static class WledPresetConfig {
        private WledPresetStateConfig style;
    }

    @Data
    public static class WledPresetStateConfig {
        private List<LedColor> colors = new ArrayList<>();
        private Integer palette;
        private Integer intensity;
        private Integer speed;
        private Integer effect;
    }
}