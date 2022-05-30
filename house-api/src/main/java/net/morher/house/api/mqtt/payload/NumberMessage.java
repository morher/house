package net.morher.house.api.mqtt.payload;

public class NumberMessage {
    public static PayloadFormat<Integer> integer() {
        return JsonMessage.toType(Integer.class);
    }

    public static PayloadFormat<Double> decimal() {
        return JsonMessage.toType(Double.class);
    }
}
