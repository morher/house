package net.morher.house.api.mqtt.client;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class DefaultClientFactory implements MqttClientFactory {

  @Override
  public IMqttAsyncClient connect(
      MqttOptions options, MqttAvailabilityPolicy availability, MqttCallback mqttCallback)
      throws MqttException {
    IMqttAsyncClient client = createClient(options);
    MqttConnectOptions connectOptions = createConnectOptions(options, availability);
    connect(client, mqttCallback, connectOptions);
    publishAvailable(client, availability);

    return client;
  }

  protected MqttConnectOptions createConnectOptions(
      MqttOptions options, MqttAvailabilityPolicy availability) {
    MqttConnectOptions connectOptions = new MqttConnectOptions();
    connectOptions.setAutomaticReconnect(false);
    connectOptions.setCleanSession(true);
    connectOptions.setUserName(options.getUsername());
    connectOptions.setPassword(options.getPassword().toCharArray());
    connectOptions.setConnectionTimeout(10);
    if (availability != null) {
      connectOptions.setWill(
          availability.getTopic(),
          availability.getOfflinePayload(),
          availability.getQos(),
          availability.isRetain());
    }
    return connectOptions;
  }

  protected IMqttAsyncClient createClient(MqttOptions options) throws MqttException {
    return new MqttAsyncClient(options.getServerUrl(), options.getClientId(), null);
  }

  protected void connect(
      IMqttAsyncClient client, MqttCallback mqttCallback, MqttConnectOptions connectOptions)
      throws MqttException {
    client.setCallback(mqttCallback);
    client.connect(connectOptions).waitForCompletion();
  }

  protected void publishAvailable(IMqttAsyncClient client, MqttAvailabilityPolicy availability)
      throws MqttException {
    if (availability != null) {
      client
          .publish(
              availability.getTopic(),
              availability.getOnlinePayload(),
              availability.getQos(),
              availability.isRetain())
          .waitForCompletion();
    }
  }
}
