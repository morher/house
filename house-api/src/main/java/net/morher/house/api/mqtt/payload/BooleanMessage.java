package net.morher.house.api.mqtt.payload;

import java.util.ArrayList;
import java.util.List;

public class BooleanMessage implements PayloadFormat<Boolean> {
    private static final String[] DEFAULT_TRUE_VALUES = { "true", "on", "1" };
    private final String falsePayload;
    private final String truePayload;
    private final List<String> otherTrueValues = new ArrayList<>();

    public BooleanMessage(String falsePayload, String truePayload, String... otherTrueValues) {
        this.falsePayload = falsePayload;
        this.truePayload = truePayload;

        if (otherTrueValues != null) {
            for (String otherTrueValue : otherTrueValues) {
                this.otherTrueValues.add(otherTrueValue.toLowerCase());
            }
        }
    }

    public static BooleanMessage onOff() {
        return new BooleanMessage("OFF", "ON", DEFAULT_TRUE_VALUES);
    }

    @Override
    public byte[] serialize(Boolean value) {
        return value
                ? truePayload.getBytes()
                : falsePayload.getBytes();
    }

    @Override
    public Boolean deserialize(byte[] payload) {
        String payloadStr = new String(payload);

        return truePayload.equalsIgnoreCase(payloadStr)
                || otherTrueValues.contains(payloadStr.toLowerCase());
    }
}
