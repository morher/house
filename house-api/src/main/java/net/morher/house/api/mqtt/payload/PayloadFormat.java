package net.morher.house.api.mqtt.payload;

public interface PayloadFormat<T> {
    byte[] serialize(T value);

    T deserialize(byte[] payload);
}
