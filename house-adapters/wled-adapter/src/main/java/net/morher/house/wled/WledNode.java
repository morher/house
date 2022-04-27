package net.morher.house.wled;

import java.util.ArrayList;

import lombok.extern.slf4j.Slf4j;
import net.morher.house.api.mqtt.client.MqttTopicManager;
import net.morher.house.wled.to.WledNodeState;
import net.morher.house.wled.to.WledSegment;

@Slf4j
public class WledNode {
    private final MqttTopicManager<WledNodeState> topic;

    public WledNode(MqttTopicManager<WledNodeState> topic) {
        this.topic = topic;
    }

    public synchronized void updateSegment(int segmentId, LedStripState state) {
        log.debug("Update " + topic.getTopic() + " segment " + segmentId);

        WledNodeState nodeState = new WledNodeState();
        nodeState.setSegments(new ArrayList<>());
        WledSegment seg = new WledSegment();
        seg.setId(segmentId);
        seg.setPowerOn(state.getPowerOn());
        seg.setBrightness(state.getBrightness());
        seg.setColors(createColorsArray(state));
        seg.setPalette(state.getPalette());
        seg.setIntensity(state.getIntensity());
        seg.setSpeed(state.getSpeed());
        seg.setEffect(state.getEffect());
        nodeState.getSegments().add(seg);
        nodeState.setVerboseResponse(false);

        topic.publish(nodeState, false);
    }

    private static int[][] createColorsArray(LedStripState state) {
        if (state.getColor1() != null
                || state.getColor2() != null
                || state.getColor3() != null) {
            int[][] colors = new int[3][];
            colors[0] = createColorArray(state.getColor1());
            return colors;
        }
        return null;
    }

    private static int[] createColorArray(LedColor color) {
        int[] arr = { 0, 0, 0 };
        if (color != null) {
            arr[0] = color.getRed();
            arr[1] = color.getGreen();
            arr[2] = color.getBlue();
        }
        return arr;
    }
}
