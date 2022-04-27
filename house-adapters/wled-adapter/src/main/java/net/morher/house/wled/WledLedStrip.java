package net.morher.house.wled;

import static java.util.stream.Collectors.toList;

import java.util.List;

import net.morher.house.api.device.DeviceId;
import net.morher.house.api.device.DeviceInfo;
import net.morher.house.api.entity.light.LightEntity;
import net.morher.house.api.entity.light.LightOptions;
import net.morher.house.api.entity.light.LightState;
import net.morher.house.api.entity.light.LightState.PowerState;
import net.morher.house.api.entity.light.LightStateHandler;
import net.morher.house.wled.presets.Preset;
import net.morher.house.wled.presets.PresetManager;

public class WledLedStrip {
    private final String id;
    private final DeviceId deviceId;
    private final WledNode node;
    private final int segmentId;
    private final LightStateHandler handler;
    private final PresetManager presets;
    private LedStripState state = new LedStripState();

    public WledLedStrip(
            String id,
            DeviceId deviceId,
            WledNode node,
            int segmentId,
            LightEntity lightEntity,
            PresetManager presets) {

        this.id = id;
        this.deviceId = deviceId;
        this.node = node;
        this.segmentId = segmentId;
        this.presets = presets;

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setManufacturer("Wled");
        this.handler = new LightStateHandler(lightEntity, deviceInfo, this::onLampState);
        List<String> effects = presets.getLedStripPresets(id).stream().map(Preset::getName).collect(toList());
        handler.updateOptions(new LightOptions(true, effects));
    }

    public String getId() {
        return id;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public void onLampState(LightState updatedState) {
        if (updatedState.getState() != null) {
            state.setPowerOn(PowerState.ON.equals(updatedState.getState()));
        }
        if (updatedState.getBrightness() != null) {
            state.setBrightness(updatedState.getBrightness());
        }
        Preset preset = getPreset(updatedState.getEffect());
        if (preset != null) {
            state.apply(preset.getState(), true);
        }
        node.updateSegment(segmentId, state);
    }

    public LedStripState getState() {
        return state;
    }

    private Preset getPreset(String presetId) {
        for (Preset preset : presets.getLedStripPresets(id)) {
            if (preset.getName().equals(presetId)) {
                return preset;
            }
        }
        return null;
    }
}
