package net.morher.house.wled.presets;

import java.util.List;
import java.util.Map;

import net.morher.house.wled.config.WledConfiguration.WledPresetConfig;

public interface PresetManager {
    void configure(Map<String, WledPresetConfig> presets);

    List<Preset> getLedStripPresets(String stripId);
}
