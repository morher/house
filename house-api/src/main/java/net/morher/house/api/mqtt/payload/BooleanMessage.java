package net.morher.house.api.mqtt.payload;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

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

    public static BooleanMessage onOffLowerCase() {
        return new BooleanMessage("off", "on", DEFAULT_TRUE_VALUES);
    }

    public PayloadFormat<Boolean> inverted() {
        return new InvertedBooleanMessage(this);
    }

    public PayloadFormat<Boolean> inverted(boolean isInverted) {
        return isInverted
                ? inverted()
                : this;
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

    @RequiredArgsConstructor
    private static class InvertedBooleanMessage implements PayloadFormat<Boolean> {
        private final PayloadFormat<Boolean> format;

        @Override
        public byte[] serialize(Boolean value) {
            return format.serialize(!value);
        }

        @Override
        public Boolean deserialize(byte[] payload) {
            return !format.deserialize(payload);
        }

    }
}
