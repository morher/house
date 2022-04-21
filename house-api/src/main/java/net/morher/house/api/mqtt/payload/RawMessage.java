package net.morher.house.api.mqtt.payload;

public class RawMessage {
    public static PayloadFormat<String> toStr() {
        return new PayloadFormat<String>() {

            @Override
            public byte[] serialize(String value) {
                return value.getBytes();
            }

            @Override
            public String deserialize(byte[] payload) {
                return new String(payload);
            }
        };
    }
}
