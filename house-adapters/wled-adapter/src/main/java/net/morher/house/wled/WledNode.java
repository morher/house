package net.morher.house.wled;

import java.util.ArrayList;

import lombok.extern.slf4j.Slf4j;
import net.morher.house.api.mqtt.client.Topic;
import net.morher.house.wled.to.WledNodeState;
import net.morher.house.wled.to.WledSegment;

@Slf4j
public class WledNode {
    private final Topic<WledNodeState> topic;

    public WledNode(Topic<WledNodeState> topic) {
        this.topic = topic;
    }

    public synchronized void updateSegment(int segmentId, LedStripState state) {
        log.debug("Update " + topic.getTopic() + " segment " + segmentId, false);

        WledNodeState nodeState = new WledNodeState();
        nodeState.setPowerOn(true);
        nodeState.setBrightness(255);
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

        topic.publish(nodeState);
    }

    private static int[][] createColorsArray(LedStripState state) {
        if (state.getColor1() != null
                || state.getColor2() != null
                || state.getColor3() != null) {
            int[][] colors = new int[3][];
            colors[0] = createColorArray(state.getColor1());
            colors[1] = createColorArray(state.getColor2());
            colors[2] = createColorArray(state.getColor3());
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
