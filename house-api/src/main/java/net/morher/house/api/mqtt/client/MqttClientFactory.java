package net.morher.house.api.mqtt.client;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface MqttClientFactory {
    IMqttAsyncClient connect(MqttOptions options, MqttAvailabilityPolicy availabilityPolicy, MqttCallback mqttCallback) throws MqttException;
}
